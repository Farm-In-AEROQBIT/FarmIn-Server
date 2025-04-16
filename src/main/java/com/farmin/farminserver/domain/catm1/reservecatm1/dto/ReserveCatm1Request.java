package com.farmin.farminserver.domain.catm1.reservecatm1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReserveCatm1Request {
    private Integer reserveId;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private LocalDateTime time;
}
