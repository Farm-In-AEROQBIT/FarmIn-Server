package com.farmin.farminserver.domain.catm1.maternitycatm1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MaternityCatm1Response {
    private Integer sensorIdc;
    private Integer maternityId;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private LocalDateTime time;
}
