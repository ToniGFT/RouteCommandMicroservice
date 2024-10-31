package com.workshop.route.domain.model.entities;

import com.workshop.route.domain.model.valueobjects.WeekSchedule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @NotNull(message = "Weekdays schedule cannot be null")
    @Valid
    private WeekSchedule weekdays;

    @NotNull(message = "Weekends schedule cannot be null")
    @Valid
    private WeekSchedule weekends;
}
