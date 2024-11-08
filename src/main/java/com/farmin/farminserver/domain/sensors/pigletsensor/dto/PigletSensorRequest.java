package com.farmin.farminserver.domain.sensors.pigletsensor.dto;

import lombok.Data;

@Data
public class PigletSensorRequest {
    private String PigletID;
    private String SensorID;
    private String Co2;
    private String Nh3;
    private String PM;
    private String Temper;
    private String Humidity;
}
