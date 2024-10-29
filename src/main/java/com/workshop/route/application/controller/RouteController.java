package com.workshop.route.application.controller;

import com.workshop.route.application.response.service.RouteResponseService;
import com.workshop.route.application.services.RouteService;
import com.workshop.route.domain.model.agregates.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private static final Logger logger = LoggerFactory.getLogger(RouteController.class);
    private final RouteService routeService;
    private final RouteResponseService routeResponseService;

    public RouteController(RouteService routeService, RouteResponseService routeResponseService) {
        this.routeService = routeService;
        this.routeResponseService = routeResponseService;
    }

    @PostMapping
    public Mono<ResponseEntity<Route>> createRoute(@RequestBody Route route) {
        logger.info("Attempting to create a new route with name: {}", route.getRouteName());
        return routeService.createRoute(route)
                .flatMap(routeResponseService::buildCreatedResponse)
                .doOnSuccess(response -> logger.info("Successfully created route with ID: {}", route.getRouteId()));
    }

    @GetMapping("/{_id}")
    public Mono<ResponseEntity<Route>> getRouteById(@PathVariable String id) {
        logger.info("Fetching route with ID: {}", id);
        return routeService.getRouteById(id)
                .flatMap(routeResponseService::buildOkResponse)
                .doOnSuccess(response -> logger.info("Successfully fetched route with ID: {}", id))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{_id}")
    public Mono<ResponseEntity<Route>> updateRoute(@PathVariable String id, @RequestBody Route route) {
        logger.info("Attempting to update route with ID: {}", id);
        return routeService.updateRoute(id, route)
                .flatMap(routeResponseService::buildOkResponse)
                .doOnSuccess(response -> logger.info("Successfully updated route with ID: {}", id))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{_id}")
    public Mono<ResponseEntity<Void>> deleteRoute(@PathVariable String id) {
        logger.info("Attempting to delete route with ID: {}", id);
        return routeService.deleteRoute(id)
                .then(routeResponseService.buildNoContentResponse())
                .doOnSuccess(response -> logger.info("Successfully deleted route with ID: {}", id));
    }

    @GetMapping
    public Flux<ResponseEntity<Route>> getAllRoutes() {
        logger.info("Fetching all routes");
        return routeResponseService.buildRoutesResponse(routeService.getAllRoutes())
                .doOnComplete(() -> logger.info("Successfully fetched all routes"));
    }
}
