package com.farmin.farminserver.domain.sensors.growingsensor.mapper;

import com.farmin.farminserver.domain.sensors.growingsensor.dto.GrowingSensorRequest;
import com.farmin.farminserver.domain.sensors.growingsensor.dto.GrowingSensorResponse;
import com.farmin.farminserver.entity.sensors.growingsensor.GrowingSensorEntity;
import org.springframework.stereotype.Component;

@Component
public class GrowingSensorMapper {

    public GrowingSensorEntity toEntity(GrowingSensorRequest request) {
        return GrowingSensorEntity.builder()
                .GrowingID(request.getGrowingID())
                .SensorID(request.getSensorID())
                .Co2(request.getCo2())
                .Nh3(request.getNh3())
                .PM(request.getPM())
                .Temper(request.getTemper())
                .Humidity(request.getHumidity())
                .build();
    }

    public GrowingSensorResponse toResponse(GrowingSensorEntity entity) {
        return GrowingSensorResponse.builder()
                .GrowingID(entity.getGrowingID())
                .SensorID(entity.getSensorID())
                .Co2(entity.getCo2())
                .Nh3(entity.getNh3())
                .PM(entity.getPM())
                .Temper(entity.getTemper())
                .Humidity(entity.getHumidity())
                .build();
    }
}
