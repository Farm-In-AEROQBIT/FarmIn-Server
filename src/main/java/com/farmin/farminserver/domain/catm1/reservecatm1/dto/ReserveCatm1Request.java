package com.farmin.farminserver.domain.catm1.reservecatm1.dto;

import lombok.Data;

@Data
public class ReserveCatm1Request {
    private Integer reserveID;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private String time;
}
