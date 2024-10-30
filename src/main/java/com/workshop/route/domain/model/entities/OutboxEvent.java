package com.workshop.route.domain.model.entities;

import com.workshop.route.domain.model.valueobjects.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "outbox_event")
public class OutboxEvent {

    @Id
    private ObjectId id;
    private ObjectId aggregateId;
    private String aggregateType;
    private EventType eventType;
    private String payload;
    private LocalDateTime createdAt;
}
