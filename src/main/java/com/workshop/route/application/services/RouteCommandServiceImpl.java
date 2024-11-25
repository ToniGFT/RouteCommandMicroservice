package com.workshop.route.application.services;

import com.workshop.route.application.dto.RouteUpdateDTO;
import com.workshop.route.application.services.kafka.RouteEventPublisher;
import com.workshop.route.domain.events.RouteDeletedEvent;
import com.workshop.route.domain.exception.RouteNotFoundException;
import com.workshop.route.domain.exception.RouteUpdateException;
import com.workshop.route.domain.exception.RouteValidationException;
import com.workshop.route.domain.model.aggregates.Route;
import com.workshop.route.domain.model.mapper.RouteMapper;
import com.workshop.route.domain.model.validation.RouteValidator;
import com.workshop.route.domain.repository.RouteCommandRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RouteCommandServiceImpl implements RouteCommandService {

    private final RouteCommandRepository routeRepository;
    private final RouteValidator routeValidator;
    private final RouteEventPublisher eventPublisher;

    public RouteCommandServiceImpl(RouteCommandRepository routeRepository,
                                   RouteValidator routeValidator,
                                   RouteEventPublisher eventPublisher) {
        this.routeRepository = routeRepository;
        this.routeValidator = routeValidator;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Mono<Route> createRoute(Route route) {
        return Mono.just(route)
                .doOnNext(routeValidator::validate)
                .onErrorMap(e -> new RouteValidationException("Invalid route data: " + e.getMessage()))
                .flatMap(routeRepository::save)
                .flatMap(savedRoute -> eventPublisher.publishRouteCreatedEvent(
                        RouteMapper.toRouteCreatedEvent(savedRoute)
                ).then(Mono.just(savedRoute)));
    }

    @Override
    public Mono<Route> updateRoute(ObjectId id, RouteUpdateDTO routeUpdateInfo) {
        return routeRepository.findById(id)
                .switchIfEmpty(Mono.error(new RouteNotFoundException("Route not found with id: " + id)))
                .flatMap(existingRoute -> {
                    RouteMapper.updateRouteFromDto(routeUpdateInfo, existingRoute);
                    return routeRepository.save(existingRoute);
                })
                .flatMap(updatedRoute -> eventPublisher.publishRouteUpdatedEvent(
                        RouteMapper.toRouteUpdatedEvent(updatedRoute)
                ).then(Mono.just(updatedRoute)));
    }

    @Override
    public Mono<Void> deleteRoute(ObjectId id) {
        return routeRepository.findById(id)
                .switchIfEmpty(Mono.error(new RouteNotFoundException("Route not found with id: " + id)))
                .flatMap(existingRoute -> routeRepository.deleteById(id)
                        .then(eventPublisher.publishRouteDeletedEvent(
                                RouteDeletedEvent.builder()
                                        .routeId(existingRoute.getRouteId())
                                        .build()
                        )));
    }
}
