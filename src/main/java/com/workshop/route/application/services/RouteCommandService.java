package com.workshop.route.application.services;

import com.workshop.route.domain.model.aggregates.Route;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface RouteCommandService {
    Mono<Route> createRoute(Route route);

    Mono<Route> updateRoute(ObjectId id, Route route);

    Mono<Void> deleteRoute(ObjectId id);
}

