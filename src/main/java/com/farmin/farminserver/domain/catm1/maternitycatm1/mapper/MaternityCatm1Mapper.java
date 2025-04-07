package com.farmin.farminserver.domain.catm1.maternitycatm1.mapper;

import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1Request;
import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1Response;
import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1StatisticsResponse;
import com.farmin.farminserver.entity.catm1.maternitycatm1sensor.MaternityCatm1Entity;
import org.springframework.stereotype.Component;

@Component
public class MaternityCatm1Mapper {

    public MaternityCatm1Entity toEntity(MaternityCatm1Request dto) {
        return MaternityCatm1Entity.builder()
                .maternityID(dto.getMaternityID())
                .temper(dto.getTemper())
                .wtemper(dto.getWTemper())
                .humidity(dto.getHumidity())
                .co2(dto.getCo2())
                .time(dto.getTime())
                .build();
    }

    public MaternityCatm1Response toResponse(MaternityCatm1Entity entity) {
        return MaternityCatm1Response.builder()
                .sensorIdc(entity.getSensorIdc())
                .maternityID(entity.getMaternityID())
                .temper(entity.getTemper())
                .wTemper(entity.getWtemper())
                .humidity(entity.getHumidity())
                .co2(entity.getCo2())
                .time(entity.getTime())
                .build();
    }

    public MaternityCatm1StatisticsResponse toStatisticsResponse(String label, String avgCo2, String avgTemper, String avgWTemper, String avgHumidity){
        return MaternityCatm1StatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageWTemper(avgWTemper != null ? String.format("%.2f", avgWTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
