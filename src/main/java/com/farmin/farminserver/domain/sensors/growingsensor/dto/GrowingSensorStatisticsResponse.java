package com.farmin.farminserver.domain.sensors.growingsensor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GrowingSensorStatisticsResponse {
    private String label; // X축에 표시될 값 (연도, 월, 주, 일 등)
    private String averageCo2; // 평균 이산화탄소 값
    private String averageNh3; // 평균 암모니아 값
    private String averagePM;  // 평균 미세먼지 값
    private String averageTemper; // 평균 온도
    private String averageHumidity; // 평균 습도
}
