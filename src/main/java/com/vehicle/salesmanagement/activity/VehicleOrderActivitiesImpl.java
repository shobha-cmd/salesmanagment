package com.vehicle.salesmanagement.activity;

import com.vehicle.salesmanagement.domain.dto.apirequest.OrderRequest;
import com.vehicle.salesmanagement.domain.dto.apiresponse.OrderResponse;
import com.vehicle.salesmanagement.domain.entity.model.VehicleOrderDetails;
import com.vehicle.salesmanagement.enums.OrderStatus;
import com.vehicle.salesmanagement.enums.StockStatus;
import com.vehicle.salesmanagement.repository.VehicleOrderDetailsRepository;
import com.vehicle.salesmanagement.service.VehicleOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleOrderActivitiesImpl implements VehicleOrderActivities {

    private final VehicleOrderDetailsRepository vehicleOrderDetailsRepository;
    private final VehicleOrderService vehicleOrderService;

    @Override
    public void saveOrderDetails(VehicleOrderDetails vehicleOrderDetails) {
        try {
            log.info("Saving order details for customer: {}", vehicleOrderDetails.getCustomerName());
            validateVehicleOrderDetails(vehicleOrderDetails);
            vehicleOrderDetailsRepository.save(vehicleOrderDetails);
            log.info("Order details saved successfully");
        } catch (Exception e) {
            log.error("Failed to save order details: {}", e.getMessage());
            throw new RuntimeException("Failed to save order details: " + e.getMessage(), e);
        }
    }

    @Override
    public OrderResponse checkStockAvailability(OrderRequest orderRequest) {
        try {
            log.info("Checking stock availability for order: {}", orderRequest.getCustomerName());
            validateOrderRequest(orderRequest);
            OrderResponse response = vehicleOrderService.checkAndBlockStock(orderRequest);
            if (response.getOrderStatus() == OrderStatus.PROCESSING) {
                log.info("Stock available in stock_details and successfully blocked for customer: {}", orderRequest.getCustomerName());
                return response;
            } else {
                log.warn("Vehicle not available in stock_details for model: {} and variant: {}",
                        orderRequest.getModelName(), orderRequest.getVariant());
            }

            response = vehicleOrderService.checkAndReserveMddpStock(orderRequest);
            if (response.getOrderStatus() == OrderStatus.PROCESSING) {
                log.info("Stock transferred from mddp_stock to stock_details and blocked for customer: {}", orderRequest.getCustomerName());
                return response;
            } else {
                log.warn("Vehicle not available in mddp_stock for model: {} and variant: {}",
                        orderRequest.getModelName(), orderRequest.getVariant());
            }

            log.info("Placing manufacturer order as stock not available in either table for customer: {}", orderRequest.getCustomerName());
            response = vehicleOrderService.placeManufacturerOrder(orderRequest);
            log.info("🏷 Manufacturer order placed with status: {}", response.getOrderStatus());
            return response;
        } catch (IllegalArgumentException e) {
            log.error("Invalid order request: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Stock check failed for order: {} - {}", orderRequest.getCustomerName(), e.getMessage());
            throw new RuntimeException("Stock check failed: " + e.getMessage(), e);
        }
    }

    @Override
    public OrderResponse confirmOrder(OrderResponse orderResponse) {
        try {
            log.info("Confirming order for customer: {}", orderResponse.getCustomerName());
            OrderResponse confirmed = vehicleOrderService.confirmOrder(orderResponse);
            log.info("Order confirmed: {}", confirmed.getOrderStatus());
            return confirmed;
        } catch (Exception e) {
            log.error("Order confirmation failed: {}", e.getMessage());
            throw new RuntimeException("Order confirmation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public OrderResponse cancelOrder(Long customerOrderId) {
        try {
            log.info("Canceling order with ID: {}", customerOrderId);
            VehicleOrderDetails orderDetails = vehicleOrderDetailsRepository.findById(customerOrderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with ID: " + customerOrderId));

            if (orderDetails.getOrderStatus() == OrderStatus.COMPLETED || orderDetails.getOrderStatus() == OrderStatus.CANCELED) {
                throw new IllegalStateException("Order with ID " + customerOrderId + " cannot be canceled. Current status: " + orderDetails.getOrderStatus());
            }

            OrderResponse response = vehicleOrderService.cancelOrder(customerOrderId);
            log.info("Order canceled successfully with ID: {}", customerOrderId);
            return response;
        } catch (Exception e) {
            log.error("Failed to cancel order with ID: {} - {}", customerOrderId, e.getMessage());
            throw new RuntimeException("Failed to cancel order: " + e.getMessage(), e);
        }
    }

    private void validateOrderRequest(OrderRequest orderRequest) {
        if (orderRequest == null) {
            throw new IllegalArgumentException("Order request cannot be null");
        }
        if (!StringUtils.hasText(orderRequest.getModelName())) {
            throw new IllegalArgumentException("Vehicle model name is required");
        }
    }

    private void validateVehicleOrderDetails(VehicleOrderDetails vehicleOrderDetails) {
        if (vehicleOrderDetails == null) {
            throw new IllegalArgumentException("Vehicle order details cannot be null");
        }
        if (!StringUtils.hasText(vehicleOrderDetails.getModelName())) {
            throw new IllegalArgumentException("Vehicle model name is required in order details");
        }
    }
}