package com.workshop.route.application.response.service;


import com.workshop.route.domain.model.agregates.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("RouteResponseService Unit Tests")
class RouteResponseServiceTest {

    @InjectMocks
    private RouteResponseService routeResponseService;

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
    @DisplayName("Build Created Response - Should Return Mono with 201 Created")
    void buildCreatedResponse_shouldReturnCreatedResponse() {
        // given
        // when
        Mono<ResponseEntity<Route>> result = routeResponseService.buildCreatedResponse(route);

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.CREATED &&
                        response.getBody() != null &&
                        response.getBody().getRouteId().equals("1"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Build OK Response - Should Return Mono with 200 OK")
    void buildOkResponse_shouldReturnOkResponse() {
        // given
        // when
        Mono<ResponseEntity<Route>> result = routeResponseService.buildOkResponse(route);

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.OK &&
                        response.getBody() != null &&
                        response.getBody().getRouteId().equals("1"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Build No Content Response - Should Return Mono with 204 No Content")
    void buildNoContentResponse_shouldReturnNoContentResponse() {
        // given
        // when
        Mono<ResponseEntity<Void>> result = routeResponseService.buildNoContentResponse();

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.NO_CONTENT &&
                        response.getBody() == null)
                .verifyComplete();
    }

    @Test
    @DisplayName("Build Routes Response - Should Return Flux with 200 OK for Each Route")
    void buildRoutesResponse_shouldReturnOkResponsesForEachRoute() {
        // given
        Flux<Route> routeFlux = Flux.just(route);

        // when
        Flux<ResponseEntity<Route>> result = routeResponseService.buildRoutesResponse(routeFlux);

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.OK &&
                        response.getBody() != null &&
                        response.getBody().getRouteId().equals("1"))
                .verifyComplete();
    }
}

