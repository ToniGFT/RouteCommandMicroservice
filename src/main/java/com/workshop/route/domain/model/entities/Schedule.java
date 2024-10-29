package com.workshop.route.domain.model.entities;

import com.workshop.route.domain.model.valueobjects.WeekSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    private WeekSchedule weekdays;
    private WeekSchedule weekends;

}
