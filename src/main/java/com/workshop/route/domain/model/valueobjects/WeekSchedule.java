package com.workshop.route.domain.model.valueobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekSchedule {

    private LocalTime startTime;
    private LocalTime endTime;
    private Integer frequencyMinutes;

}

