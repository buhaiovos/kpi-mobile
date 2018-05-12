package com.osb.mobiledev.kpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Currency {
    Long r030;
    String txt;
    Double rate;
    String cc;
    @JsonProperty("exchangedate")
    String exchangeDate;
}
