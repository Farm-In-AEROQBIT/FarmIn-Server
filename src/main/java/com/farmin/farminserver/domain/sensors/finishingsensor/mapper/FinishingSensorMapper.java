package com.farmin.farminserver.domain.sensors.finishingsensor.mapper;

import com.farmin.farminserver.domain.sensors.finishingsensor.dto.FinishingSensorRequest;
import com.farmin.farminserver.domain.sensors.finishingsensor.dto.FinishingSensorResponse;
import com.farmin.farminserver.domain.sensors.finishingsensor.dto.FinishingSensorStatisticsResponse;
import com.farmin.farminserver.entity.sensors.finishingsensor.FinishingSensorEntity;
import org.springframework.stereotype.Component;

@Component
public class FinishingSensorMapper {

    public FinishingSensorEntity toEntity(FinishingSensorRequest request) {
        return FinishingSensorEntity.builder()
                .finishingID(request.getFinishingID())
                .co2(request.getCo2())
                .nh3(request.getNh3())
                .pm(request.getPm())
                .temper(request.getTemper())
                .humidity(request.getHumidity())
                .time(request.getTime())
                .build();
    }

    public FinishingSensorResponse toResponse(FinishingSensorEntity entity) {
        return FinishingSensorResponse.builder()
                .sensorID(entity.getSensorID() != null ? entity.getSensorID().toString() : null) // Integer -> String 변환
                .finishingID(entity.getFinishingID())
                .co2(entity.getCo2())
                .nh3(entity.getNh3())
                .pm(entity.getPm())
                .temper(entity.getTemper())
                .humidity(entity.getHumidity())
                .time(entity.getTime())
                .build();
    }

    // Mapping for statistics response
    public FinishingSensorStatisticsResponse toStatisticsResponse(String label, Double avgCo2, Double avgNh3, Double avgPM, Double avgTemper, Double avgHumidity) {
        return FinishingSensorStatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageNh3(avgNh3 != null ? String.format("%.2f", avgNh3) : null)
                .averagePM(avgPM != null ? String.format("%.2f", avgPM) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
