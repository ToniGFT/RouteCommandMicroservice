package com.workshop.route.application.controller;

import com.workshop.route.application.response.service.RouteResponseService;
import com.workshop.route.application.services.RouteService;
import com.workshop.route.domain.model.agregates.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName("RouteController Unit Tests")
class RouteControllerTest {

    @Mock
    private RouteService routeService;

    @Mock
    private RouteResponseService routeResponseService;

    @InjectMocks
    private RouteController routeController;

    private Route route;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        route = Route.builder()
                .routeId("1")
                .routeName("Test Route")
                .build();
    }

    @Test
    @DisplayName("Create Route - Should Return Created Response")
    void createRoute_shouldReturnCreatedResponse() {
        // given
        when(routeService.createRoute(any(Route.class))).thenReturn(Mono.just(route));
        when(routeResponseService.buildCreatedResponse(route))
                .thenReturn(Mono.just(ResponseEntity.status(201).body(route)));

        // when
        Mono<ResponseEntity<Route>> result = routeController.createRoute(route);

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getBody() != null && response.getBody().getRouteName().equals("Test Route"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Get Route By ID - Should Return OK Response")
    void getRouteById_shouldReturnOkResponse() {
        // given
        when(routeService.getRouteById("1")).thenReturn(Mono.just(route));
        when(routeResponseService.buildOkResponse(route))
                .thenReturn(Mono.just(ResponseEntity.ok(route)));

        // when
        Mono<ResponseEntity<Route>> result = routeController.getRouteById("1");

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getBody() != null && response.getBody().getRouteId().equals("1"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Update Route - Should Return OK Response")
    void updateRoute_shouldReturnOkResponse() {
        // given
        when(routeService.updateRoute(eq("1"), any(Route.class))).thenReturn(Mono.just(route));
        when(routeResponseService.buildOkResponse(route))
                .thenReturn(Mono.just(ResponseEntity.ok(route)));

        // when
        Mono<ResponseEntity<Route>> result = routeController.updateRoute("1", route);

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getBody() != null && response.getBody().getRouteId().equals("1"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete Route - Should Return No Content Response")
    void deleteRoute_shouldReturnNoContentResponse() {
        // given
        when(routeService.deleteRoute("1")).thenReturn(Mono.empty());
        when(routeResponseService.buildNoContentResponse())
                .thenReturn(Mono.just(ResponseEntity.noContent().build()));

        // when
        Mono<ResponseEntity<Void>> result = routeController.deleteRoute("1");

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    @DisplayName("Get All Routes - Should Return OK Response for Each Route")
    void getAllRoutes_shouldReturnOkResponse() {
        // given
        when(routeService.getAllRoutes()).thenReturn(Flux.just(route));
        when(routeResponseService.buildRoutesResponse(any(Flux.class)))
                .thenReturn(Flux.just(ResponseEntity.ok(route)));

        // when
        Flux<ResponseEntity<Route>> result = routeController.getAllRoutes();

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getBody() != null && response.getBody().getRouteId().equals("1"))
                .verifyComplete();
    }
}
