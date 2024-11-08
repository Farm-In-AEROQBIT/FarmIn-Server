package com.farmin.farminserver.domain.sensors.finishingsensor.mapper;

import com.farmin.farminserver.domain.sensors.finishingsensor.dto.FinishingSensorRequest;
import com.farmin.farminserver.domain.sensors.finishingsensor.dto.FinishingSensorResponse;
import com.farmin.farminserver.entity.sensors.finishingsensor.FinishingSensorEntity;
import org.springframework.stereotype.Component;

@Component
public class FinishingSensorMapper {

    public FinishingSensorEntity toEntity(FinishingSensorRequest request) {
        return FinishingSensorEntity.builder()
                .FinishingID(request.getFinishingID())
                .SensorID(request.getSensorID())
                .Co2(request.getCo2())
                .Nh3(request.getNh3())
                .PM(request.getPm())
                .Temper(request.getTemper())
                .Humidity(request.getHumidity())
                .build();
    }

    public FinishingSensorResponse toResponse(FinishingSensorEntity entity) {
        return FinishingSensorResponse.builder()
                .finishingID(entity.getFinishingID())
                .sensorID(entity.getSensorID())
                .co2(entity.getCo2())
                .nh3(entity.getNh3())
                .pm(entity.getPM())
                .temper(entity.getTemper())
                .humidity(entity.getHumidity())
                .build();
    }
}
