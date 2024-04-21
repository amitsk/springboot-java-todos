package com.github.amitsk.sunrise.model;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record SunriseRequest(
        @DecimalMin("-90.00")
        @DecimalMax("90.00")
        String lat,
        @DecimalMin("-180.00")
        @DecimalMax("180.00")
        String lng) {
}

