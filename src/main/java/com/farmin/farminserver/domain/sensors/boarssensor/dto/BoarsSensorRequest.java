package com.farmin.farminserver.domain.sensors.boarssensor.dto;

import lombok.Data;

@Data
public class BoarsSensorRequest {
    private String boarsID;
    private String co2;
    private String nh3;
    private String pm;
    private String temper;
    private String humidity;
}
