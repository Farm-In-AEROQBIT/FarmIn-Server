package com.farmin.farminserver.domain.catm1.maternitycatm1.dto;

import lombok.Data;

@Data
public class MaternityCatm1Request {
    private Integer maternityID;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private String time;
}
