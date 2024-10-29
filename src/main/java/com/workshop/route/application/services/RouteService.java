package com.workshop.route.application.services;

import com.workshop.route.domain.model.agregates.Route;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RouteService {
    Mono<Route> createRoute(Route route);
    Mono<Route> getRouteById(ObjectId  id);
    Mono<Route> updateRoute(ObjectId  id, Route route);
    Mono<Void> deleteRoute(ObjectId id);
    Flux<Route> getAllRoutes();
}

