package com.workshop.route.domain.model.validation;

import com.workshop.route.domain.model.aggregates.Route;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RouteValidator {

    private final Validator validator;

    public RouteValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(Route route) {
        Set<ConstraintViolation<Route>> violations = validator.validate(route);
        if (!violations.isEmpty()) {
            String errorMessages = formatValidationErrors(violations);
            throw new IllegalArgumentException("Validation errors: " + errorMessages);
        }
    }

    private String formatValidationErrors(Set<ConstraintViolation<Route>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }
}
