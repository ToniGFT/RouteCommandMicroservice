package com.workshop.route.application.services.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.route.domain.events.RouteCreatedEvent;
import com.workshop.route.domain.events.RouteDeletedEvent;
import com.workshop.route.domain.events.RouteUpdatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class RouteEventPublisher {

    private final KafkaSender<String, String> kafkaSender;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.route-events}")
    private String routeEventsTopic;

    public RouteEventPublisher(KafkaSender<String, String> kafkaSender, ObjectMapper objectMapper) {
        this.kafkaSender = kafkaSender;
        this.objectMapper = objectMapper;
    }

    public Mono<Void> publishRouteCreatedEvent(RouteCreatedEvent event) {
        return publishEvent("ROUTE_CREATED", event);
    }

    public Mono<Void> publishRouteUpdatedEvent(RouteUpdatedEvent event) {
        return publishEvent("ROUTE_UPDATED", event);
    }

    public Mono<Void> publishRouteDeletedEvent(RouteDeletedEvent event) {
        return publishEvent("ROUTE_DELETED", event);
    }

    private <T> Mono<Void> publishEvent(String eventType, T event) {
        try {
            Map<String, Object> messagePayload = new HashMap<>();
            messagePayload.put("type", eventType);
            messagePayload.putAll(objectMapper.convertValue(event, Map.class));

            String message = objectMapper.writeValueAsString(messagePayload);
            return kafkaSender.send(Mono.just(
                    SenderRecord.create(routeEventsTopic, null, System.currentTimeMillis(), null, message, null)
            )).then();
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to publish event: " + eventType, e));
        }
    }

}
