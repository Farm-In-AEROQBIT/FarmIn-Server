package com.farmin.farminserver.domain.catm1.finishingcatm1.dto;

import lombok.Data;

@Data
public class FinishingCatm1Request {
    private Integer finishingID;
    private String temper;
    private String wTemper;
    private String humidity;
    private String co2;
    private String time;
}

