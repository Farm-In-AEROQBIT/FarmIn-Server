package com.farmin.farminserver.domain.catm1.boarscatm1.dto;

import lombok.Data;

@Data
public class BoarsCatm1Request {
    private String boarsID;
    private String temper;
    private String wtemper;
    private String humidity;
    private String co2;
    private String time;
}
