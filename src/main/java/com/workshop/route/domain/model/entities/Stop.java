package com.workshop.route.domain.model.entities;

import com.workshop.route.domain.model.valueobjects.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stop {

    private String stopId;
    private String stopName;

    private Coordinates coordinates;

    private List<String> arrivalTimes;

}

