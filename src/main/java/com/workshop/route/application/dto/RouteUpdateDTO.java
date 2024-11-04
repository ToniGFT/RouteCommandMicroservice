package com.workshop.route.application.dto;

import com.workshop.route.domain.model.entities.Schedule;
import com.workshop.route.domain.model.entities.Stop;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteUpdateDTO {

    @NotEmpty(message = "The route name cannot be empty")
    private String routeName;

    @NotEmpty(message = "There must be at least one stop in the route")
    private List<@Valid Stop> stops;

    @NotNull(message = "The schedule cannot be null")
    @Valid
    private Schedule schedule;

}
