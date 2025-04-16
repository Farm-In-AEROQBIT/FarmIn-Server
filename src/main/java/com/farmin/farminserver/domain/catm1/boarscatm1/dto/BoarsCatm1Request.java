package com.farmin.farminserver.domain.catm1.boarscatm1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoarsCatm1Request {
    private String boarsId;
    private String temper;
    private String wtemper;
    private String humidity;
    private String co2;
    private LocalDateTime time;
}
