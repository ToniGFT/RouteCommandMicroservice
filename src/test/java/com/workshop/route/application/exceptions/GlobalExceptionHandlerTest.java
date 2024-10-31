package com.workshop.route.application.exceptions;

import com.workshop.route.domain.exception.RouteNotFoundException;
import com.workshop.route.domain.exception.RouteValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Handle MethodArgumentNotValidException")
    void testHandleValidationExceptions() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "field", "must not be null");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).contains("Validation failed");
        assertThat(response.getBody().getMessage()).contains("must not be null");
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getErrorId()).isNotNull();
    }


    @Test
    @DisplayName("Handle RouteNotFoundException")
    void testHandleRouteNotFoundException() {
        // Arrange
        RouteNotFoundException exception = new RouteNotFoundException("Route not found");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleRouteNotFoundException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ROUTE_NOT_FOUND");
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Route not found");
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getErrorId()).isNotNull();
    }

    @Test
    @DisplayName("Handle RouteValidationException")
    void testHandleRouteValidationException() {
        // Arrange
        RouteValidationException exception = new RouteValidationException("Invalid route data");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleRouteValidationException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ROUTE_VALIDATION_ERROR");
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid route data");
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getErrorId()).isNotNull();
    }

    @Test
    @DisplayName("Handle Generic Exception")
    void testHandleGenericException() {
        // Arrange
        Exception exception = new Exception("Unexpected error");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getMessage()).contains("An unexpected error occurred");
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getErrorId()).isNotNull();
    }
}
