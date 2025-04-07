package com.farmin.farminserver.domain.catm1.growingcatm1.mapper;

import com.farmin.farminserver.domain.catm1.growingcatm1.dto.GrowingCatm1Request;
import com.farmin.farminserver.domain.catm1.growingcatm1.dto.GrowingCatm1Response;
import com.farmin.farminserver.domain.catm1.growingcatm1.dto.GrowingCatm1StatisticsResponse;
import com.farmin.farminserver.entity.catm1.growingcatm1sensor.GrowingCatm1Entity;
import org.springframework.stereotype.Component;

@Component
public class GrowingCatm1Mapper {

    public GrowingCatm1Entity toEntity(GrowingCatm1Request dto) {
        return GrowingCatm1Entity.builder()
                .growingID(dto.getGrowingID())
                .temper(dto.getTemper())
                .wtemper(dto.getWTemper())
                .humidity(dto.getHumidity())
                .co2(dto.getCo2())
                .time(dto.getTime())
                .build();
    }

    public GrowingCatm1Response toResponse(GrowingCatm1Entity entity) {
        return GrowingCatm1Response.builder()
                .sensorIdc(entity.getSensorIdc())
                .growingID(entity.getGrowingID())
                .temper(entity.getTemper())
                .wTemper(entity.getWtemper())
                .humidity(entity.getHumidity())
                .co2(entity.getCo2())
                .time(entity.getTime())
                .build();
    }

    public GrowingCatm1StatisticsResponse toStatisticsResponse(String label, String avgCo2, String avgTemper, String avgWTemper, String avgHumidity){
        return GrowingCatm1StatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageWTemper(avgWTemper != null ? String.format("%.2f", avgWTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
