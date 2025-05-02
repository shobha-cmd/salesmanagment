package com.vehicle.salesmanagement.workflow;

import com.vehicle.salesmanagement.activity.VehicleOrderActivities;
import com.vehicle.salesmanagement.domain.dto.apirequest.OrderRequest;
import com.vehicle.salesmanagement.domain.dto.apiresponse.OrderResponse;
import com.vehicle.salesmanagement.enums.OrderStatus;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class VehicleOrderWorkflowImpl implements VehicleOrderWorkflow {

    private final VehicleOrderActivities activities;
    private boolean isCanceled = false;

    public VehicleOrderWorkflowImpl() {
        ActivityOptions options = ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(30))
                .setRetryOptions(RetryOptions.newBuilder()
                        .setInitialInterval(Duration.ofSeconds(1))
                        .setMaximumAttempts(3)
                        .build())
                .build();
        this.activities = Workflow.newActivityStub(VehicleOrderActivities.class, options);
    }

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        log.info("Workflow started for customer: {}", orderRequest.getCustomerName());
        try {
            OrderResponse response = activities.checkStockAvailability(orderRequest);

            if (isCanceled) {
                log.info("Order canceled during workflow for customer: {}", orderRequest.getCustomerName());
                return activities.cancelOrder(Long.valueOf(Workflow.getInfo().getWorkflowId().split("-")[1]));
            }

            if (response.getOrderStatus() == OrderStatus.COMPLETED) {
                log.info("Order confirmed for customer: {}", orderRequest.getCustomerName());
                return activities.confirmOrder(response);
            } else if (response.getOrderStatus() == OrderStatus.PENDING) {
                log.info("Stock not available, pending manufacturer dispatch for: {}", orderRequest.getCustomerName());
                Workflow.await(Duration.ofHours(24), () -> isCanceled);
                if (isCanceled) {
                    log.info("Order canceled while pending for customer: {}", orderRequest.getCustomerName());
                    return activities.cancelOrder(Long.valueOf(Workflow.getInfo().getWorkflowId().split("-")[1]));
                }
                return response;
            }

            log.warn("Unknown status or no action taken for customer: {}", orderRequest.getCustomerName());
            return new OrderResponse();
        } catch (Exception e) {
            log.error("Workflow failed for customer {}: {}", orderRequest.getCustomerName(), e.getMessage(), e);
            return new OrderResponse();
        }
    }

    @Override
    public void cancelOrder(Long orderId) {
        log.info("Received cancel signal for order ID: {}", orderId);
        isCanceled = true;
    }
}