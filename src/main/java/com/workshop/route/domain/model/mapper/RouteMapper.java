package com.workshop.route.domain.model.mapper;

import com.workshop.route.domain.model.agregates.Route;
import org.modelmapper.ModelMapper;

public class RouteMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        configureMappings();
    }

    private static void configureMappings() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.typeMap(Route.class, Route.class).addMappings(mapper -> {
            mapper.skip(Route::setRouteId);
        });
    }

    private RouteMapper() {
    }

    public static void mapRouteData(Route source, Route destination) {
        modelMapper.map(source, destination);
    }
}
