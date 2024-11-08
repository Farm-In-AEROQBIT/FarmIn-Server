package com.farmin.farminserver.domain.sensors.reservesensor.mapper;

import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorRequest;
import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorResponse;
import com.farmin.farminserver.entity.sensors.reservesensor.ReserveSensorEntity;
import org.springframework.stereotype.Component;

@Component
public class ReserveSensorMapper {

    public ReserveSensorEntity toEntity(ReserveSensorRequest request) {
        return ReserveSensorEntity.builder()
                .ReserveID(request.getReserveID())
                .SensorID(request.getSensorID())
                .Co2(request.getCo2())
                .Nh3(request.getNh3())
                .PM(request.getPM())
                .Temper(request.getTemper())
                .Humidity(request.getHumidity())
                .build();
    }

    public ReserveSensorResponse toResponse(ReserveSensorEntity entity) {
        return ReserveSensorResponse.builder()
                .ReserveID(entity.getReserveID())
                .SensorID(entity.getSensorID())
                .Co2(entity.getCo2())
                .Nh3(entity.getNh3())
                .PM(entity.getPM())
                .Temper(entity.getTemper())
                .Humidity(entity.getHumidity())
                .build();
    }
}
