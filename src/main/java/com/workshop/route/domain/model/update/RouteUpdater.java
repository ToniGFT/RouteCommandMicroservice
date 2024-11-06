package com.workshop.route.domain.model.update;

import com.workshop.route.application.dto.RouteUpdateDTO;
import com.workshop.route.domain.model.aggregates.Route;
import com.workshop.route.domain.model.mapper.RouteMapper;
import com.workshop.route.domain.model.validation.RouteValidator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RouteUpdater {

    private final RouteValidator routeValidator;

    public RouteUpdater(RouteValidator routeValidator) {
        this.routeValidator = routeValidator;
    }

    public Mono<Route> mapAndValidate(RouteUpdateDTO source, Route target) {
        RouteMapper.mapRouteData(source, target);
        return Mono.fromRunnable(() -> routeValidator.validate(target))
                .thenReturn(target);
    }
}
