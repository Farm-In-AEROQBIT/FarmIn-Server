package com.farmin.farminserver.domain.sensors.growingsensor.dto;

import lombok.Data;

@Data
public class GrowingSensorRequest {
    private String GrowingID;
    private String SensorID;
    private String Co2;
    private String Nh3;
    private String PM;
    private String Temper;
    private String Humidity;
}
