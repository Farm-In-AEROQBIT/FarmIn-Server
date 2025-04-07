package com.farmin.farminserver.domain.catm1.gestationcatm1.mapper;

import com.farmin.farminserver.domain.catm1.gestationcatm1.dto.GestationCatm1Request;
import com.farmin.farminserver.domain.catm1.gestationcatm1.dto.GestationCatm1Response;
import com.farmin.farminserver.domain.catm1.gestationcatm1.dto.GestationCatm1StatisticsResponse;
import com.farmin.farminserver.entity.catm1.gestationcatm1sensor.GestationCatm1Entity;
import org.springframework.stereotype.Component;

@Component
public class GestationCatm1Mapper {
    public GestationCatm1Entity toEntity(GestationCatm1Request dto) {
        return GestationCatm1Entity.builder()
                .gestationID(dto.getGestationID())
                .temper(dto.getTemper())
                .wtemper(dto.getWTemper())
                .humidity(dto.getHumidity())
                .co2(dto.getCo2())
                .time(dto.getTime())
                .build();
    }

    public GestationCatm1Response toResponse(GestationCatm1Entity entity) {
        return GestationCatm1Response.builder()
                .sensorIdc(entity.getSensorIdc())
                .gestationID(entity.getGestationID())
                .temper(entity.getTemper())
                .wTemper(entity.getWtemper())
                .humidity(entity.getHumidity())
                .co2(entity.getCo2())
                .time(entity.getTime())
                .build();
    }

    public GestationCatm1StatisticsResponse toStatisticsResponse(String label, String avgCo2, String avgTemper, String avgWTemper, String avgHumidity){
        return GestationCatm1StatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageWTemper(avgWTemper != null ? String.format("%.2f", avgWTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
