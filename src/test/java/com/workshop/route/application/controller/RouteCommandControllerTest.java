package com.workshop.route.application.controller;

import com.workshop.route.application.response.service.RouteResponseService;
import com.workshop.route.application.services.RouteCommandService;
import com.workshop.route.domain.model.aggregates.Route;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName("RouteCommandController Unit Tests")
class RouteCommandControllerTest {

    @Mock
    private RouteCommandService routeCommandService;

    @Mock
    private RouteResponseService routeResponseService;

    @InjectMocks
    private RouteCommandController routeCommandController;

    private Route route;
    private ObjectId objectId;
    private String stringId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectId = new ObjectId("507f1f77bcf86cd799439011");
        stringId = "507f1f77bcf86cd799439011";
        route = Route.builder()
                .routeId(objectId)
                .routeName("Test Route")
                .build();
    }

    @Test
    @DisplayName("Create Route - Should Return Created Response")
    void createRoute_shouldReturnCreatedResponse() {
        // given
        when(routeCommandService.createRoute(any(Route.class))).thenReturn(Mono.just(route));
        when(routeResponseService.buildCreatedResponse(route))
                .thenReturn(Mono.just(ResponseEntity.status(201).body(route)));

        // when
        Mono<ResponseEntity<Route>> result = routeCommandController.createRoute(route);

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getBody() != null && response.getBody().getRouteName().equals("Test Route"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Update Route - Should Return OK Response")
    void updateRoute_shouldReturnOkResponse() {
        // given
        when(routeCommandService.updateRoute(eq(objectId), any(Route.class))).thenReturn(Mono.just(route));
        when(routeResponseService.buildOkResponse(route))
                .thenReturn(Mono.just(ResponseEntity.ok(route)));

        // when
        Mono<ResponseEntity<Route>> result = routeCommandController.updateRoute(stringId, route);

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getBody() != null && response.getBody().getRouteId().equals(objectId))
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete Route - Should Return No Content Response")
    void deleteRoute_shouldReturnNoContentResponse() {
        // given
        when(routeCommandService.deleteRoute(objectId)).thenReturn(Mono.empty());
        when(routeResponseService.buildNoContentResponse())
                .thenReturn(Mono.just(ResponseEntity.noContent().build()));

        // when
        Mono<ResponseEntity<Void>> result = routeCommandController.deleteRoute(stringId);

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }
}
