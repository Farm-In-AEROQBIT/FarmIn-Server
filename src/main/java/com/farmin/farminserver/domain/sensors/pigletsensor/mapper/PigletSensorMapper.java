package com.farmin.farminserver.domain.sensors.pigletsensor.mapper;

import com.farmin.farminserver.domain.sensors.pigletsensor.dto.PigletSensorRequest;
import com.farmin.farminserver.domain.sensors.pigletsensor.dto.PigletSensorResponse;
import com.farmin.farminserver.entity.sensors.pigletsensor.PigletSensorEntity;
import org.springframework.stereotype.Component;

@Component
public class PigletSensorMapper {

    public PigletSensorEntity toEntity(PigletSensorRequest request) {
        return PigletSensorEntity.builder()
                .PigletID(request.getPigletID())
                .SensorID(request.getSensorID())
                .Co2(request.getCo2())
                .Nh3(request.getNh3())
                .PM(request.getPM())
                .Temper(request.getTemper())
                .Humidity(request.getHumidity())
                .build();
    }

    public PigletSensorResponse toResponse(PigletSensorEntity entity) {
        return PigletSensorResponse.builder()
                .PigletID(entity.getPigletID())
                .SensorID(entity.getSensorID())
                .Co2(entity.getCo2())
                .Nh3(entity.getNh3())
                .PM(entity.getPM())
                .Temper(entity.getTemper())
                .Humidity(entity.getHumidity())
                .build();
    }
}
