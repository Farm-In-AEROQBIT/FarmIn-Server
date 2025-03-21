package com.farmin.farminserver.domain.catm1.gestationcatm1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GestationCatm1Response {
    private String gestationID;
    private String sensorid3;
    private String temper;
    private String wtemper;
    private String humidity;
    private String co2;
    private String time;
}
