package com.farmin.farminserver.domain.catm1.pigletcatm1.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PigletCatm1StatisticsResponse {
    private String label;
    private String averageCo2;
    private String averageTemper;
    private String averageHumidity;
    private String averageWTemper;
}
