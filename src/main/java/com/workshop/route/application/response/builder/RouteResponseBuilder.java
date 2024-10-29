package com.workshop.route.application.response.builder;

import com.workshop.route.domain.model.agregates.Route;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RouteResponseBuilder {

    public static ResponseEntity<Route> generateCreatedResponse(Route route) {
        return ResponseEntity.status(HttpStatus.CREATED).body(route);
    }

    public static ResponseEntity<Route> generateOkResponse(Route route) {
        return ResponseEntity.ok(route);
    }

    public static ResponseEntity<Void> generateNoContentResponse() {
        return ResponseEntity.noContent().build();
    }
}
