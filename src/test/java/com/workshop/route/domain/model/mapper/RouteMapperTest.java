package com.workshop.route.domain.model.mapper;

import com.workshop.route.domain.model.agregates.Route;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RouteMapper Unit Tests")
class RouteMapperTest {

    @Test
    @DisplayName("Map Route Data - Should Copy Data from Source to Destination Without Changing routeId")
    void mapRouteData_shouldCopyDataFromSourceToDestinationWithoutChangingRouteId() {
        // Arrange
        Route source = Route.builder()
                .routeId("1")
                .routeName("Source Route")
                .build();

        Route destination = Route.builder()
                .routeId("2")
                .routeName("Destination Route")
                .build();

        // Act
        RouteMapper.mapRouteData(source, destination);

        // Assert
        assertThat(destination.getRouteId()).isEqualTo("2");
        assertThat(destination.getRouteName()).isEqualTo("Source Route");
    }
}
