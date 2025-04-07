package com.farmin.farminserver.domain.catm1.gestationcatm1.dto;

import lombok.Data;

@Data
public class GestationCatm1Request {
    private Integer gestationID;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private String time;
}

