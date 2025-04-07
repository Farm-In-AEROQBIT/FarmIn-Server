package com.farmin.farminserver.domain.catm1.reservecatm1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReserveCatm1Response {
    private Integer sensorIdc;
    private Integer reserveID;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private String time;
}
