package com.farmin.farminserver.domain.catm1.growingcatm1.dto;

import lombok.Data;
import lombok.Builder;

@Builder
@Data
public class GrowingCatm1StatisticsResponse {
    private String label;
    private String averageCo2;
    private String averageTemper;
    private String averageHumidity;
    private String averageWTemper;
}
