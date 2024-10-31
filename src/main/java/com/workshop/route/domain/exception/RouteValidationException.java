package com.workshop.route.domain.exception;

public class RouteValidationException extends RuntimeException {
    public RouteValidationException(String message) {
        super(message);
    }
}
