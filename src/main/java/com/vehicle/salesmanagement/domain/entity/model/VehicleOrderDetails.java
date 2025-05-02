package com.vehicle.salesmanagement.domain.entity.model;

import com.vehicle.salesmanagement.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "vehicle_order_details", schema = "sales_tracking")
@AllArgsConstructor
@NoArgsConstructor
public class VehicleOrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_order_id")
    private Long customerOrderId;

    @ManyToOne
    @JoinColumn(name = "vehicle_model_id", nullable = false)
    @NotNull(message = "Vehicle model is required")
    private VehicleModel vehicleModel;

    @ManyToOne
    @JoinColumn(name = "vehicle_variant_id", nullable = false)
    @NotNull(message = "Vehicle variant is required")
    private VehicleVariant vehicleVariant;

    @Column(name = "customer_name", length = 100, nullable = false)
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Column(name = "phone_number", length = 15, nullable = false)
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name = "permanent_address", columnDefinition = "TEXT")
    private String permanentAddress;

    @Column(name = "current_address", columnDefinition = "TEXT")
    private String currentAddress;

    @Column(name = "aadhar_no", length = 20, unique = true, nullable = false)
    @NotBlank(message = "Aadhar number is required")
    private String aadharNo;

    @Column(name = "pan_no", length = 20, unique = true, nullable = false)
    @NotBlank(message = "PAN number is required")
    private String panNo;

    @Column(name = "model_name", length = 100, nullable = false)
    @NotBlank(message = "Model name is required")
    private String modelName;

    @Column(name = "fuel_type", length = 50)
    private String fuelType;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "transmission_type", length = 50)
    private String transmissionType;

    @Column(name = "variant", length = 50)
    private String variant;

    @Column(name = "quantity", nullable = false)
    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @Column(name = "total_price", precision = 15, scale = 2, nullable = false)
    @NotNull(message = "Total price is required")
    private BigDecimal totalPrice;

    @Column(name = "booking_amount", precision = 15, scale = 2, nullable = false)
    @NotNull(message = "Booking amount is required")
    private BigDecimal bookingAmount;

    @Column(name = "payment_mode", length = 50, nullable = false)
    @NotBlank(message = "Payment mode is required")
    private String paymentMode;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // Getters and Setters
    public Long getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(Long customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public VehicleVariant getVehicleVariant() {
        return vehicleVariant;
    }

    public void setVehicleVariant(VehicleVariant vehicleVariant) {
        this.vehicleVariant = vehicleVariant;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(BigDecimal bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}