package com.workshop.route.application.services;

import com.workshop.route.domain.model.agregates.Route;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RouteService {
    Mono<Route> createRoute(Route route);
    Mono<Route> getRouteById(String id);
    Mono<Route> updateRoute(String id, Route route);
    Mono<Void> deleteRoute(String id);
    Flux<Route> getAllRoutes();
}

