package com.farmin.farminserver.domain.catm1.maternitycatm1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaternityCatm1Response {
    private Integer sensorIdc;
    private Integer maternityID;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private String time;
}
