package com.workshop.route.application.services;

import com.workshop.route.domain.model.agregates.Route;
import com.workshop.route.domain.model.mapper.RouteMapper;
import com.workshop.route.domain.repository.RouteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public Mono<Route> createRoute(Route route) {
        return routeRepository.save(route);
    }

    @Override
    public Mono<Route> getRouteById(String id) {
        return routeRepository.findById(id);
    }

    @Override
    public Mono<Route> updateRoute(String id, Route route) {
        return routeRepository.findById(id)
                .doOnNext(existingRoute -> RouteMapper.mapRouteData(route, existingRoute))
                .flatMap(routeRepository::save);
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
