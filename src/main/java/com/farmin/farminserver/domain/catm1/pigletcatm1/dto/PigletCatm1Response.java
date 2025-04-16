package com.farmin.farminserver.domain.catm1.pigletcatm1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PigletCatm1Response {
    private Integer sensorIdc;
    private Integer pigletId;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private LocalDateTime time;
}
