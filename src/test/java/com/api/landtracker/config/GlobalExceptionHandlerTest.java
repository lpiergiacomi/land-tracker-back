package com.api.landtracker.config;

import com.api.landtracker.utils.ApiResponse;
import com.api.landtracker.utils.exception.DataValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleException() {
        Exception ex = new Exception("Internal Server Error");
        ResponseEntity<ApiResponse<Void>> responseEntity = globalExceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("error", responseEntity.getBody().getStatus());
        assertEquals("Se produjo un error interno: Internal Server Error", responseEntity.getBody().getMessage());
    }

    @Test
    void handleDataValidationException() {
        DataValidationException ex = new DataValidationException("Bad Request");
        ResponseEntity<ApiResponse<Void>> responseEntity = globalExceptionHandler.handleDataValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("error", responseEntity.getBody().getStatus());
        assertEquals("Bad Request", responseEntity.getBody().getMessage());
    }

    @Test
    void handleDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Data Integrity Violation");
        ResponseEntity<ApiResponse<Void>> responseEntity = globalExceptionHandler.handleDataIntegrityViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("error", responseEntity.getBody().getStatus());
        assertEquals("Data Integrity Violation", responseEntity.getBody().getMessage());
    }

    @Test
    void handleMaxUploadSizeExceededException() {
        MaxUploadSizeExceededException ex = new MaxUploadSizeExceededException(100);
        ResponseEntity<ApiResponse<Void>> responseEntity = globalExceptionHandler.handleMaxUploadSizeExceededException(ex);

        assertEquals(HttpStatus.EXPECTATION_FAILED, responseEntity.getStatusCode());
        assertEquals("error", responseEntity.getBody().getStatus());
        assertEquals("Maximum upload size of 100 bytes exceeded", responseEntity.getBody().getMessage());
    }

}
