package com.farmin.farminserver.domain.sensors.gestationsensor.mapper;

import com.farmin.farminserver.domain.sensors.gestationsensor.dto.GestationSensorRequest;
import com.farmin.farminserver.domain.sensors.gestationsensor.dto.GestationSensorResponse;
import com.farmin.farminserver.domain.sensors.gestationsensor.dto.GestationSensorStatisticsResponse;
import com.farmin.farminserver.entity.sensors.gestationsensor.GestationSensorEntity;
import org.springframework.stereotype.Component;

@Component
public class GestationSensorMapper {

    public GestationSensorEntity toEntity(GestationSensorRequest request) {
        return GestationSensorEntity.builder()
                .gestationID(request.getGestationID())
                .co2(request.getCo2())
                .nh3(request.getNh3())
                .pm(request.getPm())
                .temper(request.getTemper())
                .humidity(request.getHumidity())
                .time(request.getTime())
                .build();
    }

    public GestationSensorResponse toResponse(GestationSensorEntity entity) {
        return GestationSensorResponse.builder()
                .sensorID(entity.getSensorID() != null ? entity.getSensorID().toString() : null) // Integer -> String 변환
                .gestationID(entity.getGestationID())
                .co2(entity.getCo2())
                .nh3(entity.getNh3())
                .pm(entity.getPm())
                .temper(entity.getTemper())
                .humidity(entity.getHumidity())
                .time(entity.getTime())
                .build();
    }

    // Mapping for statistics response
    public GestationSensorStatisticsResponse toStatisticsResponse(String label, Double avgCo2, Double avgNh3, Double avgPM, Double avgTemper, Double avgHumidity) {
        return GestationSensorStatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageNh3(avgNh3 != null ? String.format("%.2f", avgNh3) : null)
                .averagePM(avgPM != null ? String.format("%.2f", avgPM) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
