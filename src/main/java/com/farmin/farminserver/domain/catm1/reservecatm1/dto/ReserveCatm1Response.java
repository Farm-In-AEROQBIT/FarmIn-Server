package com.farmin.farminserver.domain.catm1.reservecatm1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReserveCatm1Response {
    private Integer sensorIdc;
    private Integer reserveId;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private LocalDateTime time;
}
