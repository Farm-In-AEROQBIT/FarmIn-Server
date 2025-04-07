package com.farmin.farminserver.domain.catm1.growingcatm1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GrowingCatm1Response {
    private Integer sensorIdc;
    private Integer growingID;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private String time;
}
