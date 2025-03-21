package com.farmin.farminserver.domain.sensors.gestationsensor.dto;

import lombok.Data;

@Data
public class GestationSensorRequest {
    private String gestationID; // 농장 ID
    private String co2;       // 이산화탄소
    private String nh3;       // 암모니아
    private String pm;        // 미세먼지
    private String temper;    // 온도
    private String humidity;  // 습도
    private String time;      // 측정 시간
}

