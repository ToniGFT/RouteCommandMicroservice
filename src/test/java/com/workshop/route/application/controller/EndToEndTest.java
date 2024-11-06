package com.workshop.route.application.controller;

import com.workshop.route.domain.model.aggregates.Route;
import com.workshop.route.domain.model.entities.Schedule;
import com.workshop.route.domain.model.entities.Stop;
import com.workshop.route.domain.model.valueobjects.Coordinates;
import com.workshop.route.domain.model.valueobjects.WeekSchedule;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Profile("dev")
public class EndToEndTest {

    @Autowired
    private WebTestClient webTestClient;

    private Route testRoute;

    @BeforeEach
    void setUp() {
        testRoute = new Route(
                new ObjectId(),
                "Test Route",
                List.of(
                        new Stop("1", "Central Park", new Coordinates(40.785091, -73.968285), List.of("10:00", "15:00", "20:00"))
                ),
                new Schedule(
                        new WeekSchedule(LocalTime.of(8, 0), LocalTime.of(18, 0), 30),
                        new WeekSchedule(LocalTime.of(10, 0), LocalTime.of(14, 0), 60)
                )
        );
    }

    @Test
    void testCreateUpdateAndDeleteRoute() {
        // 1. Crear una Ruta
        Route createdRoute = webTestClient.post()
                .uri("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testRoute)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody(Route.class)
                .returnResult()
                .getResponseBody();

        assert createdRoute != null;
        String createdRouteId = createdRoute.getRouteId().toHexString();

        System.out.println(createdRoute.getRouteId().toHexString());
        System.out.println(createdRouteId);

        // 2. Actualizar la Ruta
        Route updatedRoute = new Route(
                createdRoute.getRouteId(),
                "Updated Route",
                List.of(
                        new Stop("1", "Main Square", new Coordinates(40.4168, -3.7038), List.of("09:00", "14:00", "19:00"))
                ),
                new Schedule(
                        new WeekSchedule(LocalTime.of(8, 0), LocalTime.of(18, 0), 45),
                        new WeekSchedule(LocalTime.of(10, 0), LocalTime.of(16, 0), 90)
                )
        );

        webTestClient.put()
                .uri("/routes/{id}", createdRouteId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedRoute)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Route.class)
                .consumeWith(response -> {
                    Route actualRoute = response.getResponseBody();
                    assert actualRoute != null;
                    assert actualRoute.getRouteName().equals("Updated Route");
                    assert actualRoute.getSchedule().getWeekdays().getFrequencyMinutes() == 45;
                });

        // 3. Obtener la Ruta Actualizada
        webTestClient.get()
                .uri("/routes/{id}", createdRouteId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Route.class)
                .consumeWith(response -> {
                    Route fetchedRoute = response.getResponseBody();
                    assert fetchedRoute != null;
                    assert fetchedRoute.getRouteName().equals("Updated Route");
                });

        // 4. Eliminar la Ruta
        webTestClient.delete()
                .uri("/routes/{id}", createdRouteId)
                .exchange()
                .expectStatus().isNoContent();

        // 5. Verificar que la Ruta ha sido Eliminada
        webTestClient.get()
                .uri("/routes/{id}", createdRouteId)
                .exchange()
                .expectStatus().isNotFound();
    }
}
