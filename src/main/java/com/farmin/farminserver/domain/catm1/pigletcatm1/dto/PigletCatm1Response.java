package com.farmin.farminserver.domain.catm1.pigletcatm1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PigletCatm1Response {
    private Integer sensorIdc;
    private Integer pigletID;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private String time;
}
