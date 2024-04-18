package com.github.amitsk.sunrise.model;


public record SunsetApiResponse(
        String status,
        SunsetSunrise results) {
    public record SunsetSunrise(
            String sunrise,
            String sunset) {
    }

    public SunsetSunrise toSunsetSunrise() {
        return new SunsetSunrise(results.sunrise(), results.sunset());
    }
}
