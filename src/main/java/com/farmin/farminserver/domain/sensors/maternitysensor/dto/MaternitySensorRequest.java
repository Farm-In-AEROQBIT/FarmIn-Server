package com.farmin.farminserver.domain.sensors.maternitysensor.dto;

import lombok.Data;

@Data
public class MaternitySensorRequest {
    private String MaternityID;
    private String SensorID;
    private String Co2;
    private String Nh3;
    private String PM;
    private String Temper;
    private String Humidity;
}
