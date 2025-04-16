package com.farmin.farminserver.domain.catm1.finishingcatm1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FinishingCatm1Response {
    private Integer sensorIdc;
    private Integer finishingId;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private LocalDateTime time;
}