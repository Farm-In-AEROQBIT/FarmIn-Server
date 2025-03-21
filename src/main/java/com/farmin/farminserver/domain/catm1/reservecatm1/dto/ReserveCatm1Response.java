package com.farmin.farminserver.domain.catm1.reservecatm1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReserveCatm1Response {
    private String reserveID;
    private String sensorid3;
    private String temper;
    private String wtemper;
    private String humidity;
    private String co2;
    private String time;
}
