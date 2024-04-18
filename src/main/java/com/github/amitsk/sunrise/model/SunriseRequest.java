package com.github.amitsk.sunrise.model;


import javax.validation.constraints.DecimalMin;


public record SunriseRequest(
        @DecimalMin("-90.00") String lat,
        @DecimalMin("-180.00") String lng) {
}

