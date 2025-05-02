//package com.vehicle.salesmanagement.errorhandling;
//
//import com.vehicle.salesmanagement.domain.dto.apiresponse.VehicleApiResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ManufacturerAlreadyExistsException.class)
//    public ResponseEntity<VehicleApiResponse<String>> handleManufacturerExistsException(ManufacturerAlreadyExistsException ex) {
//        log.error("Manufacturer already exists: {}", ex.getMessage());
//        VehicleApiResponse<String> response = new VehicleApiResponse<>(
//                HttpStatus.CONFLICT.value(),
//                ex.getMessage(),
//                null
//        );
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<VehicleApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        log.error("Validation error: {}", ex.getMessage());
//
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//            errors.put(error.getField(), error.getDefaultMessage()));
//
//        VehicleApiResponse<Map<String, String>> response = new VehicleApiResponse<>(
//                HttpStatus.BAD_REQUEST.value(),
//                "Validation failed",
//                errors
//        );
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<VehicleApiResponse<String>> handleGenericException(Exception ex) {
//        log.error("Internal server error: {}", ex.getMessage());
//        VehicleApiResponse<String> response = new VehicleApiResponse<>(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "Internal server error",
//                ex.getMessage()
//        );
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
//
//    @ExceptionHandler(CustomerAlreadyExistsException.class)
//    public ResponseEntity<VehicleApiResponse<String>> handleCustomerExistsException(CustomerAlreadyExistsException ex) {
//        VehicleApiResponse<String> response = new VehicleApiResponse<>(HttpStatus.CONFLICT.value(), ex.getMessage(), null);
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
//    }
//    @ExceptionHandler(VehicleOrderException.class)
//    public ResponseEntity<String> handleVehicleOrderException(VehicleOrderException ex) {
//        log.error("Vehicle Order Exception: {}", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//    }
//}
