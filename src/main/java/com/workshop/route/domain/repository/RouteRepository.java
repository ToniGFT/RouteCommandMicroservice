package com.workshop.route.domain.repository;

import com.workshop.route.domain.model.agregates.Route;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends ReactiveMongoRepository<Route, ObjectId> {
}
