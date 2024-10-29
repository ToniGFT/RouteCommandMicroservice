package com.workshop.route.application.services;

import com.workshop.route.domain.model.agregates.Route;
import com.workshop.route.domain.model.entities.Schedule;
import com.workshop.route.domain.model.entities.Stop;
import com.workshop.route.domain.model.valueobjects.Coordinates;
import com.workshop.route.domain.model.valueobjects.WeekSchedule;
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
import jakarta.validation.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("RouteServiceImpl Unit Tests")
class RouteServiceImplTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private RouteServiceImpl routeService;

    private Route route;

    private Route invalidRoute;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Stop stop1 = Stop.builder()
                .stopId("1")
                .stopName("Estación Central")
                .coordinates(new Coordinates(40.712776, -74.005974))
                .arrivalTimes(List.of("08:00", "08:30", "09:00"))
                .build();

        Stop stop2 = Stop.builder()
                .stopId("2")
                .stopName("Plaza Norte")
                .coordinates(new Coordinates(40.730610, -73.935242))
                .arrivalTimes(List.of("08:15", "08:45", "09:15"))
                .build();

        Stop stop3 = Stop.builder()
                .stopId("3")
                .stopName("Terminal Norte")
                .coordinates(new Coordinates(40.748817, -73.985428))
                .arrivalTimes(List.of("08:30", "09:00", "09:30"))
                .build();

        List<Stop> stops = List.of(stop1, stop2, stop3);

        WeekSchedule weekdaysSchedule = new WeekSchedule(
                LocalTime.of(6, 0),
                LocalTime.of(22, 0),
                15
        );

        WeekSchedule weekendsSchedule = new WeekSchedule(
                LocalTime.of(7, 0),
                LocalTime.of(20, 0),
                20
        );

        Schedule schedule = new Schedule(weekdaysSchedule, weekendsSchedule);

        route = Route.builder()
                .routeId("6720c504a8c6119cff20e881")
                .routeName("Ruta Centro-Norte")
                .stops(stops)
                .schedule(schedule)
                .build();

        invalidRoute = Route.builder().build();
    }


    @Test
    @DisplayName("Test createRoute - Successful Creation")
    void createRoute_Success() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(routeRepository.save(route)).thenReturn(Mono.just(route));

        StepVerifier.create(routeService.createRoute(route))
                .expectNext(route)
                .verifyComplete();

        verify(routeRepository, times(1)).save(route);
    }

    @Test
    @DisplayName("Test createRoute - Validation Failure")
    void createRoute_ValidationFailure() {


        ConstraintViolation<Route> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Este es un mensaje de error de validación");
        Set<ConstraintViolation<Route>> violations = Collections.singleton(violation);

        when(validator.validate(invalidRoute)).thenReturn(violations);

        StepVerifier.create(Mono.fromCallable(() -> routeService.createRoute(invalidRoute)))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Errores de validación: Este es un mensaje de error de validación"))
                .verify();
        verify(validator, times(1)).validate(invalidRoute);
    }

    @Test
    @DisplayName("Test getRouteById - Route Found")
    void getRouteById_Found() {
        when(routeRepository.findById("6720c504a8c6119cff20e881")).thenReturn(Mono.just(route));

        StepVerifier.create(routeService.getRouteById("6720c504a8c6119cff20e881"))
                .expectNext(route)
                .verifyComplete();
    }

    @Test
    @DisplayName("Test getRouteById - Route Not Found")
    void getRouteById_NotFound() {
        when(routeRepository.findById("invalidId")).thenReturn(Mono.empty());

        StepVerifier.create(routeService.getRouteById("invalidId"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Test updateRoute - Successful Update")
    void updateRoute_Success() {
        when(routeRepository.findById(route.getRouteId())).thenReturn(Mono.just(route));
        when(routeRepository.save(route)).thenReturn(Mono.just(route));

        StepVerifier.create(routeService.updateRoute(route.getRouteId(), route))
                .expectNext(route)
                .verifyComplete();

        verify(routeRepository, times(1)).save(route);
    }

    @Test
    @DisplayName("Test updateRoute - Route Not Found")
    void updateRoute_NotFound() {
        when(routeRepository.findById("invalidId")).thenReturn(Mono.empty());

        StepVerifier.create(routeService.updateRoute("invalidId", route))
                .verifyComplete();

        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test deleteRoute - Successful Deletion")
    void deleteRoute_Success() {
        when(routeRepository.deleteById(route.getRouteId())).thenReturn(Mono.empty());

        StepVerifier.create(routeService.deleteRoute(route.getRouteId()))
                .verifyComplete();

        verify(routeRepository, times(1)).deleteById(route.getRouteId());
    }

    @Test
    @DisplayName("Test getAllRoutes - Return All Routes")
    void getAllRoutes() {
        when(routeRepository.findAll()).thenReturn(Flux.just(route));

        StepVerifier.create(routeService.getAllRoutes())
                .expectNext(route)
                .verifyComplete();
    }
}
