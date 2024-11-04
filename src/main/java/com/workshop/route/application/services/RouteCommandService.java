package com.workshop.route.application.services;

import com.workshop.route.application.dto.RouteUpdateDTO;
import com.workshop.route.domain.model.aggregates.Route;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface RouteCommandService {
    Mono<Route> createRoute(Route route);

    Mono<Route> updateRoute(ObjectId id, RouteUpdateDTO routeUpdateInfo);

    Mono<Void> deleteRoute(ObjectId id);
}

