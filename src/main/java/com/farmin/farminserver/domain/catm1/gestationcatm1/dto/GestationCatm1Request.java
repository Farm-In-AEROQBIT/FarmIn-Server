package com.farmin.farminserver.domain.catm1.gestationcatm1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GestationCatm1Request {
    private Integer gestationId;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private LocalDateTime time;
}

