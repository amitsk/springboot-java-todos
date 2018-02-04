package com.github.amitsk.todos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class SunsetApiResponse {
    private String status;
    private Results results;


    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class Results {
        private String sunrise;
        private String sunset;
    }

    public SunsetSunrise toSunsetSunrise() {
        return new SunsetSunrise(results.sunrise, results.sunset);
    }
}
