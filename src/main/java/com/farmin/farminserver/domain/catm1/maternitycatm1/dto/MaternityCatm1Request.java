package com.farmin.farminserver.domain.catm1.maternitycatm1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaternityCatm1Request {
    private Integer maternityId;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private LocalDateTime time;
}
