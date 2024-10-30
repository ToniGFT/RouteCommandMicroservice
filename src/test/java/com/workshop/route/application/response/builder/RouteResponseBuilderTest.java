package com.workshop.route.application.response.builder;

import com.workshop.route.domain.model.aggregates.Route;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RouteResponseBuilder Unit Tests")
class RouteResponseBuilderTest {

    @Test
    @DisplayName("Generate Created Response - Should Return 201 Created with Route")
    void generateCreatedResponse_shouldReturnCreatedResponse() {
        // given
        ObjectId objectId = new ObjectId("507f1f77bcf86cd799439011");
        Route route = Route.builder()
                .routeId(objectId)
                .routeName("Test Route")
                .build();

        // when
        ResponseEntity<Route> response = RouteResponseBuilder.generateCreatedResponse(route);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getRouteId()).isEqualTo(objectId);
        assertThat(response.getBody().getRouteName()).isEqualTo("Test Route");
    }

    @Test
    @DisplayName("Generate OK Response - Should Return 200 OK with Route")
    void generateOkResponse_shouldReturnOkResponse() {
        // given
        ObjectId objectId = new ObjectId("507f1f77bcf86cd799439011");
        Route route = Route.builder()
                .routeId(objectId)
                .routeName("Test Route")
                .build();

        // when
        ResponseEntity<Route> response = RouteResponseBuilder.generateOkResponse(route);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getRouteId()).isEqualTo(objectId);
        assertThat(response.getBody().getRouteName()).isEqualTo("Test Route");
    }

    @Test
    @DisplayName("Generate No Content Response - Should Return 204 No Content")
    void generateNoContentResponse_shouldReturnNoContentResponse() {
        // when
        ResponseEntity<Void> response = RouteResponseBuilder.generateNoContentResponse();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }
}
