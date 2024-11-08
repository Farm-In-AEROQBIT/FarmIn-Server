package com.farmin.farminserver.domain.sensors.reservesensor.dto;

import lombok.Data;

@Data
public class ReserveSensorRequest {
    private String ReserveID;
    private String SensorID;
    private String Co2;
    private String Nh3;
    private String PM;
    private String Temper;
    private String Humidity;
}
