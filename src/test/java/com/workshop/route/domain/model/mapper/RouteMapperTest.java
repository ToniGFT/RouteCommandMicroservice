package com.workshop.route.domain.model.mapper;

import com.workshop.route.domain.model.aggregates.Route;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RouteMapper Unit Tests")
class RouteMapperTest {

    @Test
    @DisplayName("Map Route Data - Should Copy Data from Source to Destination Without Changing routeId")
    void mapRouteData_shouldCopyDataFromSourceToDestinationWithoutChangingRouteId() {
        // Arrange
        ObjectId sourceId = new ObjectId("507f1f77bcf86cd799439011");
        ObjectId destinationId = new ObjectId("507f191e810c19729de860ea");

        Route source = Route.builder()
                .routeId(sourceId)
                .routeName("Source Route")
                .build();

        Route destination = Route.builder()
                .routeId(destinationId)
                .routeName("Destination Route")
                .build();

        // Act
        RouteMapper.mapRouteData(source, destination);

        // Assert
        assertThat(destination.getRouteId()).isEqualTo(destinationId);
        assertThat(destination.getRouteName()).isEqualTo("Source Route");
    }
}
