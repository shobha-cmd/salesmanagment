package com.vehicle.salesmanagement.domain.dto.apiresponse;

import lombok.Data;

@Data
public class VehicleModelResponse {

    private String message;    // The full success message
    private String modelName;  // The name of the added vehicle model
    private Long id;           // The ID assigned to the new vehicle model

    // Constructor for convenience
    public VehicleModelResponse(String modelName, Long id) {
        this.modelName = modelName;
        this.id = id;
        this.message = String.format("Vehicle model '%s' added successfully with ID: %d", modelName, id);
    }

    // Default constructor for Jackson serialization
    public VehicleModelResponse() {}
}