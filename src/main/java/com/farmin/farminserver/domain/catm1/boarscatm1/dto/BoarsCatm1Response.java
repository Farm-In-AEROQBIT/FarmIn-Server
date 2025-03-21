package com.farmin.farminserver.domain.catm1.boarscatm1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoarsCatm1Response {
    private String boarsID;
    private String sensorid3;
    private String temper;
    private String wtemper;
    private String humidity;
    private String co2;
    private String time;
}
