package com.workshop.route.domain.model.aggregates;

import com.workshop.route.domain.model.entities.Schedule;
import com.workshop.route.domain.model.entities.Stop;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Route")
public class Route {

    @Id
    private ObjectId routeId;

    @NotEmpty(message = "The route name cannot be empty")
    private String routeName;

    @NotEmpty(message = "There must be at least one stop in the route")
    private List<@Valid Stop> stops;

    @NotNull(message = "The schedule cannot be null")
    @Valid
    private Schedule schedule;
}
