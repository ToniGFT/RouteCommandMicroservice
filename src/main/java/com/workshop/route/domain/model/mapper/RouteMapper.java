package com.workshop.route.domain.model.mapper;

import com.workshop.route.application.dto.RouteUpdateDTO;
import com.workshop.route.domain.events.RouteCreatedEvent;
import com.workshop.route.domain.events.RouteUpdatedEvent;
import com.workshop.route.domain.model.aggregates.Route;
import org.modelmapper.ModelMapper;

public class RouteMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    private RouteMapper() {
    }

    public static void updateRouteFromDto(RouteUpdateDTO source, Route destination) {
        modelMapper.map(source, destination);
    }

    public static RouteCreatedEvent toRouteCreatedEvent(Route route) {
        return modelMapper.map(route, RouteCreatedEvent.class);
    }

    public static RouteUpdatedEvent toRouteUpdatedEvent(Route route) {
        return modelMapper.map(route, RouteUpdatedEvent.class);
    }
}
