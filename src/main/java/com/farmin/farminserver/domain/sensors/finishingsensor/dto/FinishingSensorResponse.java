package com.farmin.farminserver.domain.sensors.finishingsensor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinishingSensorResponse {
    private String finishingID;
    private String sensorID;
    private String co2;
    private String nh3;
    private String pm;
    private String temper;
    private String humidity;
}
