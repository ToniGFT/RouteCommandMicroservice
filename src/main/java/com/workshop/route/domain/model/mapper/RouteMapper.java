package com.workshop.route.domain.model.mapper;

import com.workshop.route.application.dto.RouteUpdateDTO;
import com.workshop.route.domain.model.aggregates.Route;
import org.modelmapper.ModelMapper;

public class RouteMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    private RouteMapper() {
    }

    public static void mapRouteData(RouteUpdateDTO source, Route destination) {
        modelMapper.map(source, destination);
    }
}
