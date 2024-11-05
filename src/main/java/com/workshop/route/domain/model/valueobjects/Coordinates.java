package com.workshop.route.domain.model.valueobjects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coordinates {

    @NotNull(message = "Latitude cannot be null")
    @Min(value = -90, message = "Minimum latitude is -90")
    @Max(value = 90, message = "Maximum latitude is 90")
    private Double latitude;

    @NotNull(message = "Longitude cannot be null")
    @Min(value = -180, message = "Minimum longitude is -180")
    @Max(value = 180, message = "Maximum longitude is 180")
    private Double longitude;
}
