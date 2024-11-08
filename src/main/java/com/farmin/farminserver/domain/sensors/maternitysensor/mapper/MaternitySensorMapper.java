package com.farmin.farminserver.domain.sensors.maternitysensor.mapper;

import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorRequest;
import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorResponse;
import com.farmin.farminserver.entity.sensors.maternitysensor.MaternitySensorEntity;
import org.springframework.stereotype.Component;

@Component
public class MaternitySensorMapper {

    public MaternitySensorEntity toEntity(MaternitySensorRequest request) {
        return MaternitySensorEntity.builder()
                .MaternityID(request.getMaternityID())
                .SensorID(request.getSensorID())
                .Co2(request.getCo2())
                .Nh3(request.getNh3())
                .PM(request.getPM())
                .Temper(request.getTemper())
                .Humidity(request.getHumidity())
                .build();
    }

    public MaternitySensorResponse toResponse(MaternitySensorEntity entity) {
        return MaternitySensorResponse.builder()
                .MaternityID(entity.getMaternityID())
                .SensorID(entity.getSensorID())
                .Co2(entity.getCo2())
                .Nh3(entity.getNh3())
                .PM(entity.getPM())
                .Temper(entity.getTemper())
                .Humidity(entity.getHumidity())
                .build();
    }
}
