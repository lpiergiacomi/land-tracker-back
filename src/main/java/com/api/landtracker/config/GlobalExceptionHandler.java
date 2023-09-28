package com.api.landtracker.config;

import com.api.landtracker.utils.ApiResponse;
import com.api.landtracker.utils.exception.DataValidationException;
import com.api.landtracker.utils.exception.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus("error");
        response.setMessage("Se produjo un error interno: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataValidationException(Exception ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus("error");
        response.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataValidationException(HttpException ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus("error");
        response.setMessage(ex.getMessage());

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

}