package com.farmin.farminserver.domain.catm1.boarscatm1.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BoarsCatm1StatisticsResponse {
    private String label;
    private String averageCo2;
    private String averageTemper;
    private String averageHumidity;
    private String averageWTemper;
}
