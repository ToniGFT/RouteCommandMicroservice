package com.workshop.route.application.services;

import com.workshop.route.domain.model.aggregates.Route;
import com.workshop.route.domain.model.entities.Schedule;
import com.workshop.route.domain.model.entities.Stop;
import com.workshop.route.domain.model.update.RouteUpdater;
import com.workshop.route.domain.model.validation.RouteValidator;
import com.workshop.route.domain.model.valueobjects.Coordinates;
import com.workshop.route.domain.model.valueobjects.WeekSchedule;
import com.workshop.route.domain.repository.RouteCommandRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("RouteCommandServiceImpl Unit Tests")
class RouteCommandServiceImplTest {

    @Mock
    private RouteCommandRepository routeCommandRepository;

    @Mock
    private RouteValidator routeValidator;

    @Mock
    private RouteUpdater routeUpdater;

    @InjectMocks
    private RouteCommandServiceImpl routeService;

    private Route route;
    private Route invalidRoute;
    private ObjectId objectId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectId = new ObjectId("507f1f77bcf86cd799439011");

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
                .routeId(objectId)
                .routeName("Ruta Centro-Norte")
                .stops(List.of(stop1, stop2))
                .schedule(schedule)
                .build();

        invalidRoute = Route.builder().routeName("").build();
    }

    @Test
    @DisplayName("Test createRoute - Successful Creation")
    void createRoute_Success() {
        // Arrange
        doNothing().when(routeValidator).validate(route);
        when(routeCommandRepository.save(route)).thenReturn(Mono.just(route));

        // Act & Assert
        StepVerifier.create(routeService.createRoute(route))
                .expectNext(route)
                .verifyComplete();

        verify(routeValidator, times(1)).validate(route);
        verify(routeCommandRepository, times(1)).save(route);
    }

    @Test
    @DisplayName("Test createRoute - Validation Failure")
    void createRoute_ValidationFailure() {
        // Arrange
        doThrow(new IllegalArgumentException("Validation error")).when(routeValidator).validate(invalidRoute);

        // Act & Assert
        StepVerifier.create(Mono.fromCallable(() -> routeService.createRoute(invalidRoute)))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Validation error"))
                .verify();

        verify(routeValidator, times(1)).validate(invalidRoute);
        verify(routeCommandRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test updateRoute - Successful Update")
    void updateRoute_Success() {
        // Arrange
        when(routeCommandRepository.findById(route.getRouteId())).thenReturn(Mono.just(route));
        when(routeUpdater.mapAndValidate(route, route)).thenReturn(Mono.just(route));
        when(routeCommandRepository.save(route)).thenReturn(Mono.just(route));

        // Act & Assert
        StepVerifier.create(routeService.updateRoute(route.getRouteId(), route))
                .expectNext(route)
                .verifyComplete();

        verify(routeUpdater, times(1)).mapAndValidate(route, route);
        verify(routeCommandRepository, times(1)).save(route);
    }

    @Test
    @DisplayName("Test updateRoute - Route Not Found")
    void updateRoute_NotFound() {
        // Arrange
        when(routeCommandRepository.findById(objectId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(routeService.updateRoute(objectId, route))
                .verifyComplete();

        verify(routeCommandRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test deleteRoute - Successful Deletion")
    void deleteRoute_Success() {
        // Arrange
        when(routeCommandRepository.deleteById(route.getRouteId())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(routeService.deleteRoute(route.getRouteId()))
                .verifyComplete();

        verify(routeCommandRepository, times(1)).deleteById(route.getRouteId());
    }
}
