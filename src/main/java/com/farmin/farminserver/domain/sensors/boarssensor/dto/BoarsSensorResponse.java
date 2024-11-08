package com.farmin.farminserver.domain.sensors.boarssensor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoarsSensorResponse {
    private Integer sensorID;
    private String boarsID;
    private String co2;
    private String nh3;
    private String pm;
    private String temper;
    private String humidity;
}
