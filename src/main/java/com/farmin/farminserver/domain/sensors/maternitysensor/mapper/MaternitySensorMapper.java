package com.farmin.farminserver.domain.sensors.maternitysensor.mapper;

import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorRequest;
import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorResponse;
import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorStatisticsResponse;
import com.farmin.farminserver.entity.sensors.maternitysensor.MaternitySensorEntity;
import org.springframework.stereotype.Component;

@Component
public class MaternitySensorMapper {

    public MaternitySensorEntity toEntity(MaternitySensorRequest request) {
        return MaternitySensorEntity.builder()
                .maternityID(request.getMaternityID())
                .co2(request.getCo2())
                .nh3(request.getNh3())
                .pm(request.getPm())
                .temper(request.getTemper())
                .humidity(request.getHumidity())
                .time(request.getTime())
                .build();
    }

    public MaternitySensorResponse toResponse(MaternitySensorEntity entity) {
        return MaternitySensorResponse.builder()
                .sensorID(entity.getSensorID() != null ? entity.getSensorID().toString() : null) // Integer -> String 변환
                .maternityID(entity.getMaternityID())
                .co2(entity.getCo2())
                .nh3(entity.getNh3())
                .pm(entity.getPm())
                .temper(entity.getTemper())
                .humidity(entity.getHumidity())
                .time(entity.getTime())
                .build();
    }

    // Mapping for statistics response
    public MaternitySensorStatisticsResponse toStatisticsResponse(String label, Double avgCo2, Double avgNh3, Double avgPM, Double avgTemper, Double avgHumidity) {
        return MaternitySensorStatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageNh3(avgNh3 != null ? String.format("%.2f", avgNh3) : null)
                .averagePM(avgPM != null ? String.format("%.2f", avgPM) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
