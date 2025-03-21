package com.farmin.farminserver.domain.catm1.maternitycatm1.dto;

import lombok.Data;

@Data
public class MaternityCatm1Request {
    private String MaternityID; // Farms ID
    private String SenSorId3; // Sensor ID
    private String Temper; // Temper
    private String WTemper; // Water Temper
    private String Humidity; // Humidity
    private String Co2; //Co2
    private String Time;
}
