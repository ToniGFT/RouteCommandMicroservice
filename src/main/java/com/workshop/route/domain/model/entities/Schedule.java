package com.workshop.route.domain.model.entities;

import com.workshop.route.domain.model.valueobjects.WeekSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    private WeekSchedule weekdays;
    private WeekSchedule weekends;

}
