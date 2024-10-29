package com.workshop.route.application.services;

import com.workshop.route.domain.model.agregates.Route;
import com.workshop.route.domain.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("RouteServiceImpl Unit Tests")
class RouteServiceImplTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteServiceImpl routeService;

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
    @DisplayName("Create Route - Should Save and Return Route")
    void createRoute_shouldSaveAndReturnRoute() {
        // given
        when(routeRepository.save(any(Route.class))).thenReturn(Mono.just(route));

        // when
        Mono<Route> result = routeService.createRoute(route);

        // then
        StepVerifier.create(result)
                .expectNextMatches(savedRoute -> savedRoute.getRouteId().equals("1") && savedRoute.getRouteName().equals("Test Route"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Get Route By ID - Should Return Route if Exists")
    void getRouteById_shouldReturnRouteIfExists() {
        // given
        when(routeRepository.findById("1")).thenReturn(Mono.just(route));

        // when
        Mono<Route> result = routeService.getRouteById("1");

        // then
        StepVerifier.create(result)
                .expectNextMatches(fetchedRoute -> fetchedRoute.getRouteId().equals("1") && fetchedRoute.getRouteName().equals("Test Route"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Update Route - Should Update and Return Updated Route")
    void updateRoute_shouldUpdateAndReturnUpdatedRoute() {
        // given
        when(routeRepository.findById("1")).thenReturn(Mono.just(route));
        when(routeRepository.save(any(Route.class))).thenReturn(Mono.just(route));

        // when
        Mono<Route> result = routeService.updateRoute("1", route);

        // then
        StepVerifier.create(result)
                .expectNextMatches(updatedRoute -> updatedRoute.getRouteId().equals("1") && updatedRoute.getRouteName().equals("Test Route"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete Route - Should Delete and Complete")
    void deleteRoute_shouldDeleteAndComplete() {
        // given
        when(routeRepository.deleteById("1")).thenReturn(Mono.empty());

        // when
        Mono<Void> result = routeService.deleteRoute("1");

        // then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    @DisplayName("Get All Routes - Should Return Flux of Routes")
    void getAllRoutes_shouldReturnFluxOfRoutes() {
        // given
        when(routeRepository.findAll()).thenReturn(Flux.just(route));

        // when
        Flux<Route> result = routeService.getAllRoutes();

        // then
        StepVerifier.create(result)
                .expectNextMatches(fetchedRoute -> fetchedRoute.getRouteId().equals("1") && fetchedRoute.getRouteName().equals("Test Route"))
                .verifyComplete();
    }
}
