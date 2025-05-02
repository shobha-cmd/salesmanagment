package com.vehicle.salesmanagement.controller;

import com.vehicle.salesmanagement.domain.dto.apirequest.OrderRequest;
import com.vehicle.salesmanagement.domain.dto.apirequest.StockAddRequest;
import com.vehicle.salesmanagement.domain.dto.apirequest.VehicleModelRequest;
import com.vehicle.salesmanagement.domain.dto.apirequest.VehicleVariantRequest;
import com.vehicle.salesmanagement.domain.dto.apiresponse.OrderResponse;
import com.vehicle.salesmanagement.domain.dto.apiresponse.VehicleModelResponse;
import com.vehicle.salesmanagement.domain.entity.model.VehicleModel;
import com.vehicle.salesmanagement.domain.entity.model.VehicleOrderDetails;
import com.vehicle.salesmanagement.domain.entity.model.VehicleVariant;
import com.vehicle.salesmanagement.enums.OrderStatus;
import com.vehicle.salesmanagement.repository.VehicleModelRepository;
import com.vehicle.salesmanagement.repository.VehicleOrderDetailsRepository;
import com.vehicle.salesmanagement.repository.VehicleVariantRepository;
import com.vehicle.salesmanagement.service.VehicleOrderService;
import com.vehicle.salesmanagement.workflow.VehicleOrderWorkflow;
import com.vehicle.salesmanagement.workflow.VehicleCancelWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VehicleOrderController {

    private final WorkflowClient workflowClient;
    private final VehicleOrderDetailsRepository orderRepository;
    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleVariantRepository vehicleVariantRepository;
    private final VehicleOrderService vehicleOrderService;

    @Transactional
    @PostMapping("/api/orders")
    public ResponseEntity<String> placeOrder(@Valid @RequestBody OrderRequest orderRequest) {
        VehicleOrderDetails orderDetails = mapOrderRequestToEntity(orderRequest);
        orderDetails.setOrderStatus(OrderStatus.PENDING);
        orderDetails.setCreatedAt(LocalDateTime.now());
        orderRepository.saveAndFlush(orderDetails);
        log.info("Order saved with ID: {}", orderDetails.getCustomerOrderId());

        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("vehicle-order-task-queue")
                .setWorkflowId("order-" + orderDetails.getCustomerOrderId())
                .build();

        VehicleOrderWorkflow workflow = workflowClient.newWorkflowStub(VehicleOrderWorkflow.class, options);
        WorkflowClient.start(workflow::placeOrder, orderRequest);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Order placed successfully with ID: " + orderDetails.getCustomerOrderId() + ". Workflow started.");
    }

    @PostMapping("/api/orders/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@RequestParam Long customerOrderId) {
        log.info("Canceling order with ID: {}", customerOrderId);
        try {
            WorkflowOptions options = WorkflowOptions.newBuilder()
                    .setTaskQueue("vehicle-order-task-queue")
                    .setWorkflowId("cancel-order-" + customerOrderId)
                    .build();

            VehicleCancelWorkflow workflow = workflowClient.newWorkflowStub(VehicleCancelWorkflow.class, options);
            WorkflowClient.start(workflow::startCancelOrder, customerOrderId);

            OrderResponse response = workflowClient.newUntypedWorkflowStub("cancel-order-" + customerOrderId)
                    .getResult(10, TimeUnit.SECONDS, OrderResponse.class);

            log.info("Cancellation workflow started and completed successfully for order ID: {}", customerOrderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to start or complete cancellation workflow for order ID: {} - {}", customerOrderId, e.getMessage());
            OrderResponse response = vehicleOrderService.cancelOrder(customerOrderId);
            log.info("Fallback: Order canceled directly with ID: {}", customerOrderId);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/addStock")
    public ResponseEntity<String> addStock(@RequestBody StockAddRequest request) {
        vehicleOrderService.addVehicleStock(
                request.getModelId(),
                request.getVariantId(),
                request.getSuffix(),
                request.getFuelType(),
                request.getColour(),
                request.getEngineColour(),
                request.getTransmissionType(),
                request.getVariantName(),
                request.getQuantity(),
                request.getInteriorColour(),
                request.getVinNumber(),
                request.getCreatedBy()
        );
        return ResponseEntity.ok("Stock added successfully");
    }

    @PostMapping("/addVehicleModel")
    public ResponseEntity<String> addModel(@RequestBody VehicleModelRequest request) {
        VehicleModel model = new VehicleModel();
        model.setVehicleModelName(request.getModelName());
        model.setCreatedBy(request.getCreatedBy());
        model.setUpdatedBy(request.getCreatedBy());
        model.setCreatedAt(LocalDateTime.now());
        model.setUpdatedAt(LocalDateTime.now());

        vehicleModelRepository.save(model);
        return ResponseEntity.ok("Vehicle model added successfully with ID: " + model.getVehicleModelId());
    }




    @PostMapping("/addVariant")
    public ResponseEntity<String> addVariant(@RequestBody VehicleVariantRequest request) {
        VehicleVariant saved = vehicleOrderService.addVariantToModel(request);
        return ResponseEntity.ok("Variant added with ID: " + saved.getVehicleVariantId());
    }
    private VehicleOrderDetails mapOrderRequestToEntity(OrderRequest request) {
        VehicleOrderDetails order = new VehicleOrderDetails();
        order.setVehicleModel(vehicleModelRepository.findById(request.getVehicleModelId())
                .orElseThrow(() -> new RuntimeException("Vehicle Model not found: " + request.getVehicleModelId())));
        order.setVehicleVariant(vehicleVariantRepository.findById(request.getVehicleVariantId())
                .orElseThrow(() -> new RuntimeException("Vehicle Variant not found: " + request.getVehicleVariantId())));
        order.setCustomerName(request.getCustomerName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setEmail(request.getEmail());
        order.setPermanentAddress(request.getPermanentAddress());
        order.setCurrentAddress(request.getCurrentAddress());
        order.setAadharNo(request.getAadharNo());
        order.setPanNo(request.getPanNo());
        order.setModelName(request.getModelName());
        order.setFuelType(request.getFuelType());
        order.setColor(request.getColour());
        order.setTransmissionType(request.getTransmissionType());
        order.setVariant(request.getVariant());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(request.getTotalPrice());
        order.setBookingAmount(request.getBookingAmount());
        order.setPaymentMode(request.getPaymentMode());
        return order;
    }
}