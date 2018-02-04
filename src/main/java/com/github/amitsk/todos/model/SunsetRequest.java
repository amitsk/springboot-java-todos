package com.github.amitsk.todos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;


@Value
public class SunsetRequest {
    Double lat;
    Double lng;

}
