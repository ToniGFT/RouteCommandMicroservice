package com.workshop.route.domain.model.mapper.configuration;

import com.workshop.route.domain.model.aggregates.Route;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

public class ModelMapperConfig {

    public static ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE);

        configureRouteMapping(modelMapper);

        return modelMapper;
    }

    private static void configureRouteMapping(ModelMapper modelMapper) {
        modelMapper.typeMap(Route.class, Route.class).addMappings(mapper -> {
            mapper.skip(Route::setRouteId);
        });
    }
}
