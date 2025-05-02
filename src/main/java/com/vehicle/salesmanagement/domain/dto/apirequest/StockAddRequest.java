package com.vehicle.salesmanagement.domain.dto.apirequest;

import lombok.Data;

@Data
public class StockAddRequest {
    private Long modelId;
    private Long variantId;
    private String suffix;
    private String fuelType;
    private String colour;
    private String engineColour;
    private String transmissionType;
    private String variantName;
    private Integer quantity;
    private String interiorColour;
    private String vinNumber;
    private String createdBy;
    private String updatedBy;
}