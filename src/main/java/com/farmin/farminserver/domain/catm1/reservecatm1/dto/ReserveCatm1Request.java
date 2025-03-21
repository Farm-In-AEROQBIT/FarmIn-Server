package com.farmin.farminserver.domain.catm1.reservecatm1.dto;

import lombok.Data;

@Data
public class ReserveCatm1Request {
    private String ReserveID; // Farms ID
    private String SenSorId3; // Sensor ID
    private String Temper; // Temper
    private String WTemper; // Water Temper
    private String Humidity; // Humidity
    private String Co2; //Co2
    private String Time;
}
