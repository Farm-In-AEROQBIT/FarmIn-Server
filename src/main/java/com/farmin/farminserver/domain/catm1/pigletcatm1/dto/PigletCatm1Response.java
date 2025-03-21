package com.farmin.farminserver.domain.catm1.pigletcatm1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PigletCatm1Response {
    private String pigletID;
    private String sensorid3;
    private String temper;
    private String wtemper;
    private String humidity;
    private String co2;
    private String time;
}
