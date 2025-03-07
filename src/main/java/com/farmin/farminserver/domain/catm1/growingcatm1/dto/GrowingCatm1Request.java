package com.farmin.farminserver.domain.catm1.growingcatm1.dto;

import lombok.Data;

@Data
public class GrowingCatm1Request {
    private String GrowingID; // Farms ID
    private String SenSorId3; // Sensor ID
    private String Temper; // Temper
    private String WTemper; // Water Temper
    private String Humidity; // Humidity
    private String Co2; //Co2
    private String Time;
}
