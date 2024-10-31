package com.workshop.route.domain.model.entities;

import com.workshop.route.domain.model.valueobjects.EventType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Aggregate ID cannot be null")
    private ObjectId aggregateId;

    @NotEmpty(message = "Aggregate type cannot be empty")
    private String aggregateType;

    @NotNull(message = "Event type cannot be null")
    private EventType eventType;

    @NotEmpty(message = "Payload cannot be empty")
    private String payload;

    @NotNull(message = "CreatedAt cannot be null")
    private LocalDateTime createdAt;
}
