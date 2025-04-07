package com.farmin.farminserver.domain.catm1.pigletcatm1.dto;

import lombok.Data;

@Data
public class PigletCatm1Request {
    private Integer pigletID;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private String time;
}
