package com.farmin.farminserver.domain.sensors.gestationsensor.mapper;

import com.farmin.farminserver.domain.sensors.gestationsensor.dto.GestationSensorRequest;
import com.farmin.farminserver.domain.sensors.gestationsensor.dto.GestationSensorResponse;
import com.farmin.farminserver.entity.sensors.gestationsensor.GestationSensorEntity;
import org.springframework.stereotype.Component;

@Component
public class GestationSensorMapper {

    public GestationSensorEntity toEntity(GestationSensorRequest request) {
        return GestationSensorEntity.builder()
                .GestationID(request.getGestationID())
                .SensorID(request.getSensorID())
                .Co2(request.getCo2())
                .Nh3(request.getNh3())
                .PM(request.getPM())
                .Temper(request.getTemper())
                .Humidity(request.getHumidity())
                .build();
    }

    public GestationSensorResponse toResponse(GestationSensorEntity entity) {
        return GestationSensorResponse.builder()
                .GestationID(entity.getGestationID())
                .SensorID(entity.getSensorID())
                .Co2(entity.getCo2())
                .Nh3(entity.getNh3())
                .PM(entity.getPM())
                .Temper(entity.getTemper())
                .Humidity(entity.getHumidity())
                .build();
    }
}
