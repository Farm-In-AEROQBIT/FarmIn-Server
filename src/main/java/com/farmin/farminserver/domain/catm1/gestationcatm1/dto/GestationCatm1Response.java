package com.farmin.farminserver.domain.catm1.gestationcatm1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GestationCatm1Response {
    private Integer sensorIdc;
    private Integer gestationID;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private String time;
}
