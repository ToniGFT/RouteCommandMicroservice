package com.workshop.route.domain.model.mapper;

import com.workshop.route.domain.model.agregates.Route;
import com.workshop.route.domain.model.mapper.configuration.RouteToRouteMap;
import org.modelmapper.ModelMapper;

public class RouteMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        configureMappings();
    }

    private static void configureMappings() {
        modelMapper.addMappings(new RouteToRouteMap());
    }

    private RouteMapper() {
    }

    public static void mapRouteData(Route source, Route destination) {
        modelMapper.map(source, destination);
    }
}
