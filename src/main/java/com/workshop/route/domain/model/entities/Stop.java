package com.workshop.route.domain.model.entities;

import com.workshop.route.domain.model.valueobjects.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stop {

    private String stopId;
    private String stopName;

    private Coordinates coordinates;

    private List<String> arrivalTimes;

}

