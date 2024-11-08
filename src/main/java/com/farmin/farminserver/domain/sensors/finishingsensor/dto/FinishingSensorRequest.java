package com.farmin.farminserver.domain.sensors.finishingsensor.dto;

import lombok.Data;

@Data
public class FinishingSensorRequest {
    private String finishingID;
    private String sensorID;
    private String co2;
    private String nh3;
    private String pm;
    private String temper;
    private String humidity;
}
