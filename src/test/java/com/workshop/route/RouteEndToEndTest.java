package com.workshop.route;

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
public class RouteEndToEndTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RouteCommandRepository routeRepository;

    private Route route;
    private RouteUpdateDTO routeUpdateInfo;
    private final ObjectId fixedRouteId = new ObjectId("507f1f77bcf86cd799439011"); // Fixed ID for all operations

    @BeforeEach
    void setupDatabase() {
        routeRepository.deleteAll().block();

        Stop stop1 = Stop.builder()
                .stopId("1")
                .stopName("Central Station")
                .coordinates(Coordinates.builder().latitude(40.712776).longitude(-74.005974).build())
                .arrivalTimes(List.of("08:00", "08:30", "09:00"))
                .build();

        Stop stop2 = Stop.builder()
                .stopId("2")
                .stopName("North Plaza")
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
                .routeId(fixedRouteId)
                .routeName("End-to-End Test Route")
                .stops(List.of(stop1, stop2))
                .schedule(schedule)
                .build();

        routeUpdateInfo = RouteUpdateDTO.builder()
                .routeName("Updated End-to-End Test Route")
                .stops(List.of(stop1, stop2))
                .schedule(schedule)
                .build();

        routeRepository.save(route).block();
    }

    @Test
    @DisplayName("End-to-End Test: Create, Update, and Delete Route")
    void endToEndRouteFlow() {
        // given
        // Create a route with a fixed ID
        webTestClient.post()
                .uri("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(route)
                .exchange()

                // then
                .expectStatus().isCreated()
                .expectBody(Route.class)
                .value(createdRoute -> {
                    assert createdRoute.getRouteName().equals(route.getRouteName());
                });

        // given
        // Update the created route with the fixed ID
        routeUpdateInfo.setRouteName("Updated E2E Route Name");

        // when
        webTestClient.put()
                .uri("/routes/{id}", fixedRouteId.toHexString())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(routeUpdateInfo)
                .exchange()

                // then
                .expectStatus().isOk()
                .expectBody(Route.class)
                .value(updatedRoute -> {
                    assert updatedRoute.getRouteName().equals(routeUpdateInfo.getRouteName());
                });

        // when
        // Delete the updated route with the fixed ID
        webTestClient.delete()
                .uri("/routes/{id}", fixedRouteId.toHexString())
                .exchange()

                // then
                .expectStatus().isNoContent();
    }
}
