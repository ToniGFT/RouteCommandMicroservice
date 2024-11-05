package com.workshop.route.application.controller;

import com.workshop.route.application.dto.RouteUpdateDTO;
import com.workshop.route.domain.model.aggregates.Route;
import com.workshop.route.domain.model.entities.Schedule;
import com.workshop.route.domain.model.entities.Stop;
import com.workshop.route.domain.model.valueobjects.Coordinates;
import com.workshop.route.domain.model.valueobjects.WeekSchedule;
import com.workshop.route.domain.repository.RouteCommandRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouteCommandControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RouteCommandRepository routeRepository;

    private Route route;
    private RouteUpdateDTO routeUpdateInfo;

    @BeforeEach
    void setupDatabase() {
        routeRepository.deleteAll().block();

        Stop stop1 = Stop.builder()
                .stopId("1")
                .stopName("Estación Central")
                .coordinates(Coordinates.builder().latitude(40.712776).longitude(-74.005974).build())
                .arrivalTimes(List.of("08:00", "08:30", "09:00"))
                .build();

        Stop stop2 = Stop.builder()
                .stopId("2")
                .stopName("Plaza Norte")
                .coordinates(Coordinates.builder().latitude(40.730610).longitude(-73.935242).build())
                .arrivalTimes(List.of("08:15", "08:45", "09:15"))
                .build();

        WeekSchedule weekdaysSchedule = WeekSchedule.builder()
                .startTime(LocalTime.of(6, 0))
                .endTime(LocalTime.of(22, 0))
                .frequencyMinutes(15)
                .build();

        WeekSchedule weekendsSchedule = WeekSchedule.builder()
                .startTime(LocalTime.of(7, 0))
                .endTime(LocalTime.of(20, 0))
                .frequencyMinutes(20)
                .build();

        Schedule schedule = Schedule.builder()
                .weekdays(weekdaysSchedule)
                .weekends(weekendsSchedule)
                .build();

        route = Route.builder()
                .routeId(new ObjectId())
                .routeName("Integration Test Route")
                .stops(List.of(stop1, stop2))
                .schedule(schedule)
                .build();

        routeUpdateInfo = RouteUpdateDTO.builder()
                .routeName("Updated Integration Test Route")
                .stops(List.of(stop1, stop2))
                .schedule(schedule)
                .build();

        routeRepository.save(route).block();
    }

    @Test
    @DisplayName("Create Route - Should return Created status")
    void createRoute_shouldReturnCreatedStatus() {
        // given
        Route newRoute = Route.builder()
                .routeId(new ObjectId())
                .routeName("New Route for Creation Test")
                .stops(route.getStops())
                .schedule(route.getSchedule())
                .build();

        // when
        webTestClient.post()
                .uri("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newRoute)
                .exchange()

                // then
                .expectStatus().isCreated()
                .expectBody(Route.class)
                .value(createdRoute -> {
                    assert createdRoute.getRouteName().equals(newRoute.getRouteName());
                    assert createdRoute.getRouteId() != null;
                });
    }

    @Test
    @DisplayName("Update Route - Should return OK status")
    void updateRoute_shouldReturnOkStatus() {
        // given
        Route existingRoute = routeRepository.findAll().blockFirst();
        assert existingRoute != null;
        String existingRouteId = existingRoute.getRouteId().toHexString();

        // when
        webTestClient.put()
                .uri("/routes/{id}", existingRouteId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(routeUpdateInfo)
                .exchange()

                // then
                .expectStatus().isOk()
                .expectBody(Route.class)
                .value(updatedRoute -> {
                    assert updatedRoute.getRouteName().equals(routeUpdateInfo.getRouteName());
                });
    }

    @Test
    @DisplayName("Delete Route - Should return No Content status")
    void deleteRoute_shouldReturnNoContentStatus() {
        // given
        Route existingRoute = routeRepository.findAll().blockFirst();
        assert existingRoute != null;
        String existingRouteId = existingRoute.getRouteId().toHexString();

        // when
        webTestClient.delete()
                .uri("/routes/{id}", existingRouteId)
                .exchange()

                // then
                .expectStatus().isNoContent();
    }
}
