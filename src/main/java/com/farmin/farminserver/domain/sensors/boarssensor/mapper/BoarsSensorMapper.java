package com.farmin.farminserver.domain.sensors.boarssensor.mapper;

import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorRequest;
import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorResponse;
import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorStatisticsResponse;
import com.farmin.farminserver.entity.sensors.boarssensor.BoarsSensorEntity;
import org.springframework.stereotype.Component;

@Component
public class BoarsSensorMapper {

    public BoarsSensorEntity toEntity(BoarsSensorRequest request) {
        return BoarsSensorEntity.builder()
                .boarsID(request.getBoarsID())
                .co2(request.getCo2())
                .nh3(request.getNh3())
                .pm(request.getPm())
                .temper(request.getTemper())
                .humidity(request.getHumidity())
                .time(request.getTime())
                .build();
    }

    public BoarsSensorResponse toResponse(BoarsSensorEntity entity) {
        return BoarsSensorResponse.builder()
                .sensorID(entity.getSensorID() != null ? entity.getSensorID().toString() : null) // Integer -> String 변환
                .boarsID(entity.getBoarsID())
                .co2(entity.getCo2())
                .nh3(entity.getNh3())
                .pm(entity.getPm())
                .temper(entity.getTemper())
                .humidity(entity.getHumidity())
                .time(entity.getTime())
                .build();
    }

    // Mapping for statistics response
    public BoarsSensorStatisticsResponse toStatisticsResponse(String label, Double avgCo2, Double avgNh3, Double avgPM, Double avgTemper, Double avgHumidity) {
        return BoarsSensorStatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageNh3(avgNh3 != null ? String.format("%.2f", avgNh3) : null)
                .averagePM(avgPM != null ? String.format("%.2f", avgPM) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
