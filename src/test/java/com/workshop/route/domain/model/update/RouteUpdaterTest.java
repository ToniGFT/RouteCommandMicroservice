package com.workshop.route.domain.model.update;

import com.workshop.route.application.dto.RouteUpdateDTO;
import com.workshop.route.domain.model.aggregates.Route;
import com.workshop.route.domain.model.validation.RouteValidator;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@DisplayName("RouteUpdater Unit Tests")
class RouteUpdaterTest {

    @Mock
    private RouteValidator routeValidator;

    @InjectMocks
    private RouteUpdater routeUpdater;

    private RouteUpdateDTO source;
    private Route target;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ObjectId sourceId = new ObjectId("507f1f77bcf86cd799439011");
        ObjectId targetId = new ObjectId("507f191e810c19729de860ea");

        source = RouteUpdateDTO.builder()
                .routeName("Source Route")
                .build();

        target = Route.builder()
                .routeId(targetId)
                .routeName("Target Route")
                .build();
    }

    @Test
    @DisplayName("Map and Validate - Should Copy Data from Source to Target and Validate Target")
    void mapAndValidate_shouldCopyDataAndValidateTarget() {
        // Arrange
        doNothing().when(routeValidator).validate(target);

        // Act
        Mono<Route> result = routeUpdater.mapAndValidate(source, target);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(updatedRoute ->
                        updatedRoute.getRouteId().equals(target.getRouteId()) &&
                                updatedRoute.getRouteName().equals("Source Route"))
                .verifyComplete();

        verify(routeValidator, times(1)).validate(target);
    }

    @Test
    @DisplayName("Map and Validate - Should Throw Validation Exception if Target is Invalid")
    void mapAndValidate_shouldThrowValidationExceptionIfTargetInvalid() {
        // Arrange
        doThrow(new IllegalArgumentException("Validation error")).when(routeValidator).validate(target);

        // Act & Assert
        StepVerifier.create(routeUpdater.mapAndValidate(source, target))
                .expectErrorMatches(error -> error instanceof IllegalArgumentException &&
                        error.getMessage().equals("Validation error"))
                .verify();

        verify(routeValidator, times(1)).validate(target);
    }


}
