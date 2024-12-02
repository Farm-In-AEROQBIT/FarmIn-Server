package com.farmin.farminserver.domain.sensors.growingsensor.mapper;

import com.farmin.farminserver.domain.sensors.growingsensor.dto.GrowingSensorRequest;
import com.farmin.farminserver.domain.sensors.growingsensor.dto.GrowingSensorResponse;
import com.farmin.farminserver.domain.sensors.growingsensor.dto.GrowingSensorStatisticsResponse;
import com.farmin.farminserver.entity.sensors.growingsensor.GrowingSensorEntity;
import org.springframework.stereotype.Component;

@Component
public class GrowingSensorMapper {

    public GrowingSensorEntity toEntity(GrowingSensorRequest request) {
        return GrowingSensorEntity.builder()
                .growingID(request.getGrowingID())
                .co2(request.getCo2())
                .nh3(request.getNh3())
                .pm(request.getPm())
                .temper(request.getTemper())
                .humidity(request.getHumidity())
                .time(request.getTime())
                .build();
    }

    public GrowingSensorResponse toResponse(GrowingSensorEntity entity) {
        return GrowingSensorResponse.builder()
                .sensorID(entity.getSensorID() != null ? entity.getSensorID().toString() : null) // Integer -> String 변환
                .growingID(entity.getGrowingID())
                .co2(entity.getCo2())
                .nh3(entity.getNh3())
                .pm(entity.getPm())
                .temper(entity.getTemper())
                .humidity(entity.getHumidity())
                .time(entity.getTime())
                .build();
    }

    // Mapping for statistics response
    public GrowingSensorStatisticsResponse toStatisticsResponse(String label, Double avgCo2, Double avgNh3, Double avgPM, Double avgTemper, Double avgHumidity) {
        return GrowingSensorStatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageNh3(avgNh3 != null ? String.format("%.2f", avgNh3) : null)
                .averagePM(avgPM != null ? String.format("%.2f", avgPM) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
