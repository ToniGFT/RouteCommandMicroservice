package com.workshop.route.application.services;

import com.workshop.route.domain.model.aggregates.Route;
import com.workshop.route.domain.model.update.RouteUpdater;
import com.workshop.route.domain.model.validation.RouteValidator;
import com.workshop.route.domain.repository.RouteCommandRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RouteCommandServiceImpl implements RouteCommandService {

    private final RouteCommandRepository routeRepository;
    private final RouteValidator routeValidator;
    private final RouteUpdater routeUpdater;

    public RouteCommandServiceImpl(RouteCommandRepository routeRepository, RouteValidator routeValidator, RouteUpdater routeUpdater) {
        this.routeRepository = routeRepository;
        this.routeValidator = routeValidator;
        this.routeUpdater = routeUpdater;
    }

    @Override
    public Mono<Route> createRoute(Route route) {
        routeValidator.validate(route);
        return routeRepository.save(route);
    }

    @Override
    public Mono<Route> updateRoute(ObjectId id, Route route) {
        return routeRepository.findById(id)
                .flatMap(existingRoute -> routeUpdater.mapAndValidate(route, existingRoute))
                .flatMap(routeRepository::save);
    }

    @Override
    public Mono<Void> deleteRoute(ObjectId id) {
        return routeRepository.deleteById(id);
    }
}
