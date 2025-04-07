package com.farmin.farminserver.domain.catm1.growingcatm1.dto;

import lombok.Data;

@Data
public class GrowingCatm1Request {
    private Integer growingID;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private String time;
}
