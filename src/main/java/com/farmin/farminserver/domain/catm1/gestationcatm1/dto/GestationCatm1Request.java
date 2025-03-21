package com.farmin.farminserver.domain.catm1.gestationcatm1.dto;

import lombok.Data;

@Data
public class GestationCatm1Request {
    private String GestationID; // Farms ID
    private String SenSorId3; // Sensor ID
    private String Temper; // Temper
    private String WTemper; // Water Temper
    private String Humidity; // Humidity
    private String Co2; //Co2
    private String Time;
}
