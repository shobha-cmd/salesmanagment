package com.vehicle.salesmanagement.domain.dto.apirequest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {
    @NotNull(message = "Vehicle model ID cannot be null")
    private Long vehicleModelId;

    @NotNull(message = "Vehicle variant ID cannot be null")
    private Long vehicleVariantId;

    @NotBlank(message = "Customer name cannot be blank")
    private String customerName;

    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    private String permanentAddress;

    private String currentAddress;

    @NotBlank(message = "Aadhar number cannot be blank")
    private String aadharNo;

    @NotBlank(message = "PAN number cannot be blank")
    private String panNo;

    @NotBlank(message = "model name cannot be blank")
    private String modelName;

    private String fuelType;

    private String colour;

    private String transmissionType;

    private String variant;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "Total price cannot be null")
    private BigDecimal totalPrice;

    @NotNull(message = "Booking amount cannot be null")
    private BigDecimal bookingAmount;

    @NotBlank(message = "Payment mode cannot be blank")
    private String paymentMode;

    // Default constructor required by Jackson
    public OrderRequest() {
    }

    // Constructor with all fields for Jackson deserialization
    @JsonCreator
    public OrderRequest(
            @JsonProperty("vehicleModelId") Long vehicleModelId,
            @JsonProperty("vehicleVariantId") Long vehicleVariantId,
            @JsonProperty("customerName") String customerName,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("email") String email,
            @JsonProperty("permanentAddress") String permanentAddress,
            @JsonProperty("currentAddress") String currentAddress,
            @JsonProperty("aadharNo") String aadharNo,
            @JsonProperty("panNo") String panNo,
            @JsonProperty("modelName") String modelName,
            @JsonProperty("fuelType") String fuelType,
            @JsonProperty("colour") String colour,
            @JsonProperty("transmissionType") String transmissionType,
            @JsonProperty("variant") String variant,
            @JsonProperty("quantity") Integer quantity,
            @JsonProperty("totalPrice") BigDecimal totalPrice,
            @JsonProperty("bookingAmount") BigDecimal bookingAmount,
            @JsonProperty("paymentMode") String paymentMode) {
        this.vehicleModelId = vehicleModelId;
        this.vehicleVariantId = vehicleVariantId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.permanentAddress = permanentAddress;
        this.currentAddress = currentAddress;
        this.aadharNo = aadharNo;
        this.panNo = panNo;
        this.modelName = modelName;
        this.fuelType = fuelType;
        this.colour = colour;
        this.transmissionType = transmissionType;
        this.variant = variant;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.bookingAmount = bookingAmount;
        this.paymentMode = paymentMode;
    }
}