package com.github.amitsk.todos.model;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SunsetSunrise {
    private String sunrise;
    private String sunset;
}
