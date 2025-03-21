package com.farmin.farminserver.domain.sensors.reservesensor.mapper;

import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorRequest;
import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorResponse;
import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorStatisticsResponse;
import com.farmin.farminserver.entity.sensors.reservesensor.ReserveSensorEntity;
import org.springframework.stereotype.Component;

@Component
public class ReserveSensorMapper {

    public ReserveSensorEntity toEntity(ReserveSensorRequest request) {
        return ReserveSensorEntity.builder()
                .reserveID(request.getReserveID())
                .co2(request.getCo2())
                .nh3(request.getNh3())
                .pm(request.getPm())
                .temper(request.getTemper())
                .humidity(request.getHumidity())
                .time(request.getTime())
                .build();
    }

    public ReserveSensorResponse toResponse(ReserveSensorEntity entity) {
        return ReserveSensorResponse.builder()
                .sensorID(entity.getSensorID() != null ? entity.getSensorID().toString() : null) // Integer -> String 변환
                .reserveID(entity.getReserveID())
                .co2(entity.getCo2())
                .nh3(entity.getNh3())
                .pm(entity.getPm())
                .temper(entity.getTemper())
                .humidity(entity.getHumidity())
                .time(entity.getTime())
                .build();
    }

    // Mapping for statistics response
    public ReserveSensorStatisticsResponse toStatisticsResponse(String label, Double avgCo2, Double avgNh3, Double avgPM, Double avgTemper, Double avgHumidity) {
        return ReserveSensorStatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageNh3(avgNh3 != null ? String.format("%.2f", avgNh3) : null)
                .averagePM(avgPM != null ? String.format("%.2f", avgPM) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
