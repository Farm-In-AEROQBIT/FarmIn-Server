package com.farmin.farminserver.domain.catm1.growingcatm1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GrowingCatm1Request {
    private Integer growingId;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private LocalDateTime time;
}
