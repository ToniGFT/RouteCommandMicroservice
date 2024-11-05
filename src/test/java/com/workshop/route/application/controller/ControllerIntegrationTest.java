package com.workshop.route.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.route.application.dto.RouteUpdateDTO;
import com.workshop.route.application.response.service.RouteResponseService;
import com.workshop.route.application.services.RouteCommandServiceImpl;
import com.workshop.route.domain.model.aggregates.Route;
import com.workshop.route.domain.model.entities.Schedule;
import com.workshop.route.domain.model.entities.Stop;
import com.workshop.route.domain.model.update.RouteUpdater;
import com.workshop.route.domain.model.validation.RouteValidator;
import com.workshop.route.domain.model.valueobjects.Coordinates;
import com.workshop.route.domain.model.valueobjects.WeekSchedule;
import com.workshop.route.domain.repository.RouteCommandRepository;
import jakarta.validation.Validator;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RouteCommandRepository routeRepository;

    @MockBean
    private RouteUpdater routeUpdater;

    @MockBean
    private RouteResponseService routeResponseService;

    private Route route1;
    private Route route2;

    @BeforeEach
    void setup() {

         route1 = new Route(
                new ObjectId("67287ffc9faf680ded8d2ef8"),
                "Route 1",
                List.of(new Stop(
                        "1",
                        "Main Square",
                        new Coordinates(40.4168, -3.7038),
                        List.of("08:00", "12:00", "18:00")
                )),
                new Schedule(
                        new WeekSchedule(LocalTime.of(8, 0), LocalTime.of(18, 0), 30),  // weekdays
                        new WeekSchedule(LocalTime.of(10, 0), LocalTime.of(14, 0), 60)  // weekends
                )
        );

        route2 = new Route(
                new ObjectId("67287ffc9faf680ded8d2ef8"),
                "Route 2",
                List.of(new Stop(
                        "2",
                        "Central Station",
                        new Coordinates(34.0522, -118.2437),
                        List.of("07:30", "13:15", "17:45")
                )),
                new Schedule(
                        new WeekSchedule(LocalTime.of(7, 30), LocalTime.of(20, 0), 45),
                        new WeekSchedule(LocalTime.of(9, 0), LocalTime.of(15, 0), 90)
                )
        );

    }

    @Test
    void testCreateRoute() {
        when(routeRepository.save(any(Route.class))).thenReturn(Mono.just(route1));
        when(routeResponseService.buildCreatedResponse(any())).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(route1)));

        webTestClient.post()
                .uri("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(route1)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.routeName").isEqualTo(route1.getRouteName())
                .jsonPath("$.stops").isArray()
                .jsonPath("$.schedule").exists();
    }

    @Test
    void testUpdateRoute() {

        when(routeRepository.findById(route1.getRouteId())).thenReturn(Mono.just(route1));
        when(routeUpdater.mapAndValidate(any(RouteUpdateDTO.class), any(Route.class))).thenReturn(Mono.just(route2));
        when(routeRepository.save(any(Route.class))).thenReturn(Mono.just(route2));
        when(routeResponseService.buildOkResponse(any())).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(route2)));

        webTestClient.put()
                .uri("/routes/{idString}", route1.getRouteId().toHexString())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(route2)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Route.class)
                .consumeWith(response -> {
                    Route actualRoute = response.getResponseBody();
                    assert actualRoute != null;
                    assert route2.getRouteName().equals(actualRoute.getRouteName());
                    assert route2.getStops().equals(actualRoute.getStops());
                    assert route2.getSchedule().equals(actualRoute.getSchedule());
                });
    }

    @Test
    void testDeleteRoute() {
        ObjectId routeId = route1.getRouteId();

        when(routeRepository.findById(routeId)).thenReturn(Mono.just(route1));
        when(routeRepository.deleteById(routeId)).thenReturn(Mono.empty());
        when(routeResponseService.buildNoContentResponse()).thenReturn(Mono.just(ResponseEntity.noContent().build()));

        webTestClient.delete()
                .uri("/routes/{idString}", routeId.toHexString())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }
}
