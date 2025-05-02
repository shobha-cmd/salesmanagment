package com.vehicle.salesmanagement.domain.dto.apirequest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleVariantRequest {
    private Long modelId;
    private String variant;
    private String suffix;
    private String safetyFeature;
    private String color;
    private String engineColour;
    private String transmissionType;
    private String interiorColour;
    private String vinNumber;
    private String engineCapacity;
    private String fuelType;
    private BigDecimal price;
    private Integer yearOfManufacture;
    private String bodyType;
    private BigDecimal fuelTankCapacity;
    private Integer seatingCapacity;
    private String maxPower;
    private String maxTorque;
    private String topSpeed;
    private String wheelBase;
    private String width;
    private String length;
    private String infotainment;
    private String comfort;
    private Integer numberOfAirBags;
    private BigDecimal mileageCity;
    private BigDecimal mileageHighway;
    private String createdBy;
}