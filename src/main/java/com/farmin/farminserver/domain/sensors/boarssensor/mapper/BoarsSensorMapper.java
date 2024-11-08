package com.farmin.farminserver.domain.sensors.boarssensor.mapper;

import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorRequest;
import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorResponse;
import com.farmin.farminserver.entity.sensors.boarssensor.BoarsSensorEntity;
import org.springframework.stereotype.Component;

@Component
public class BoarsSensorMapper {

    public BoarsSensorEntity toEntity(BoarsSensorRequest request) {
        return BoarsSensorEntity.builder()
                .BoarsID(request.getBoarsID())
                .Co2(request.getCo2())
                .Nh3(request.getNh3())
                .PM(request.getPm())
                .Temper(request.getTemper())
                .Humidity(request.getHumidity())
                .build();
    }

    public BoarsSensorResponse toResponse(BoarsSensorEntity entity) {
        return BoarsSensorResponse.builder()
                .sensorID(entity.getId())
                .boarsID(entity.getBoarsID())
                .co2(entity.getCo2())
                .nh3(entity.getNh3())
                .pm(entity.getPM())
                .temper(entity.getTemper())
                .humidity(entity.getHumidity())
                .build();
    }
}
