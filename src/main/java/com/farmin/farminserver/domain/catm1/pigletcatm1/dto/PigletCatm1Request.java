package com.farmin.farminserver.domain.catm1.pigletcatm1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PigletCatm1Request {
    private Integer pigletId;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private LocalDateTime time;
}
