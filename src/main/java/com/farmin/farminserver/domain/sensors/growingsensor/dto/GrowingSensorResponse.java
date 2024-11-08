package com.farmin.farminserver.domain.sensors.growingsensor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GrowingSensorResponse {
    private String GrowingID;
    private String SensorID;
    private String Co2;
    private String Nh3;
    private String PM;
    private String Temper;
    private String Humidity;
}