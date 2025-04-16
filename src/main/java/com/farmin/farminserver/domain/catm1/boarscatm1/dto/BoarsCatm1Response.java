package com.farmin.farminserver.domain.catm1.boarscatm1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoarsCatm1Response {
    private Integer sensoridc;
    private Integer boarsId;
    private String temper;
    private String wtemper;
    private String humidity;
    private String co2;
    private LocalDateTime time;
}
