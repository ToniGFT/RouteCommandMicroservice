package com.workshop.route.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String errorCode;
    private HttpStatus status;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @Builder.Default
    private ObjectId errorId = new ObjectId();
}
