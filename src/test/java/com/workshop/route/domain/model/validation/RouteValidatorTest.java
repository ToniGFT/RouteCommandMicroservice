package com.workshop.route.domain.model.validation;

import com.workshop.route.domain.model.aggregates.Route;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("RouteValidator Unit Tests")
class RouteValidatorTest {

    @Mock
    private Validator validator;

    @InjectMocks
    private RouteValidator routeValidator;

    private Route validRoute;
    private Route invalidRoute;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validRoute = Route.builder()
                .routeName("Valid Route")
                .build();

        invalidRoute = Route.builder()
                .routeName("")
                .build();
    }

    @Test
    @DisplayName("Validate - Should Pass for Valid Route")
    void validate_shouldPassForValidRoute() {
        // Arrange
        when(validator.validate(validRoute)).thenReturn(Collections.emptySet());

        // Act & Assert
        routeValidator.validate(validRoute);

        verify(validator, times(1)).validate(validRoute);
    }

    @Test
    @DisplayName("Validate - Should Throw Exception for Invalid Route")
    void validate_shouldThrowExceptionForInvalidRoute() {
        // Arrange
        ConstraintViolation<Route> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Route name must not be empty");
        Set<ConstraintViolation<Route>> violations = Collections.singleton(violation);

        when(validator.validate(invalidRoute)).thenReturn(violations);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> routeValidator.validate(invalidRoute));
        verify(validator, times(1)).validate(invalidRoute);
        assert exception.getMessage().contains("Validation errors: Route name must not be empty");
    }
}
