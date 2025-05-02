package com.vehicle.salesmanagement.domain.dto.apirequest;

import lombok.Data;

@Data
public class VehicleModelRequest {
    private String modelName;  // Required field
    private String createdBy;
    private String updatedBy;// Optional, defaults to "admin" if not provided
}