package com.workshop.route.application.services;

import com.workshop.route.domain.model.agregates.Route;
import com.workshop.route.domain.model.mapper.RouteMapper;
import com.workshop.route.domain.repository.RouteRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final Validator validator;

    public RouteServiceImpl(RouteRepository routeRepository, Validator validator) {
        this.routeRepository = routeRepository;
        this.validator = validator;
    }

    private void validateRoute(Route route) {
        Set<ConstraintViolation<Route>> violations = validator.validate(route);
        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Errores de validación: " + errorMessages);
        }
    }

    @Override
    public Mono<Route> createRoute(Route route) {
        validateRoute(route);
        return routeRepository.save(route);
    }

    @Override
    public Mono<Route> getRouteById(String id) {
        return routeRepository.findById(id);
    }

    @Override
    public Mono<Route> updateRoute(String id, Route route) {
        return routeRepository.findById(id)
                .flatMap(existingRoute -> {
                    RouteMapper.mapRouteData(route, existingRoute);
                    validateRoute(existingRoute);
                    return routeRepository.save(existingRoute);
                });
    }

    @Override
    public Mono<Void> deleteRoute(String id) {
        return routeRepository.deleteById(id);
    }

    @Override
    public Flux<Route> getAllRoutes() {
        return routeRepository.findAll();
    }
}
