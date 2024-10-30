package com.workshop.route.domain.model.mapper;

import com.workshop.route.domain.model.aggregates.Route;
import com.workshop.route.domain.model.mapper.configuration.ModelMapperConfig;
import org.modelmapper.ModelMapper;

public class RouteMapper {

    private static final ModelMapper modelMapper = ModelMapperConfig.getModelMapper();

    private RouteMapper() {
    }

    public static void mapRouteData(Route source, Route destination) {
        modelMapper.map(source, destination);
    }
}
