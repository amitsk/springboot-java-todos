package com.github.amitsk.sunrise.model;

import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;


@Value
@Valid
public class SunriseRequest {
    @DecimalMin("-90.00")
    String lat;
    @DecimalMin("-180.00")
    String lng;

}
