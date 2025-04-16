package com.farmin.farminserver.domain.catm1.finishingcatm1.mapper;

import com.farmin.farminserver.domain.catm1.finishingcatm1.dto.FinishingCatm1Request;
import com.farmin.farminserver.domain.catm1.finishingcatm1.dto.FinishingCatm1Response;
import com.farmin.farminserver.domain.catm1.finishingcatm1.dto.FinishingCatm1StatisticsResponse;
import com.farmin.farminserver.entity.catm1.finishingcatm1sensor.FinishingCatm1Entity;
import com.farmin.farminserver.entity.catm1.growingcatm1sensor.GrowingCatm1Entity;
import org.springframework.stereotype.Component;

@Component
public class FinishingCatm1Mapper {
    public FinishingCatm1Entity toEntity(FinishingCatm1Request dto) {
        return FinishingCatm1Entity.builder()
                .finishingId(dto.getFinishingId())
                .temper(dto.getTemper())
                .wtemper(dto.getWTemper())
                .humidity(dto.getHumidity())
                .co2(dto.getCo2())
                .time(dto.getTime())
                .build();
    }

    public FinishingCatm1Response toResponse(FinishingCatm1Entity entity) {
        return FinishingCatm1Response.builder()
                .sensorIdc(entity.getSensorIdc())
                .finishingId(entity.getFinishingId())
                .temper(entity.getTemper())
                .wTemper(entity.getWtemper())
                .humidity(entity.getHumidity())
                .co2(entity.getCo2())
                .time(entity.getTime())
                .build();
    }

    public FinishingCatm1StatisticsResponse toStatisticsResponse(String label, String avgCo2, String avgTemper, String avgWTemper, String avgHumidity){
        return FinishingCatm1StatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageWTemper(avgWTemper != null ? String.format("%.2f", avgWTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
