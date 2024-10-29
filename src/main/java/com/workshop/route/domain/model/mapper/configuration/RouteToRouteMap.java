package com.workshop.route.domain.model.mapper.configuration;

import com.workshop.route.domain.model.agregates.Route;
import org.modelmapper.PropertyMap;

public class RouteToRouteMap extends PropertyMap<Route, Route> {

    @Override
    protected void configure() {
        skip().setRouteId(null);
    }
}
