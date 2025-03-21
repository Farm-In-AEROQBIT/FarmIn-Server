package com.farmin.farminserver.domain.catm1.maternitycatm1.mapper;

import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1Request;
import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1Response;
import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1StatisticsResponse;
import com.farmin.farminserver.entity.catm1.maternitycatm1sensor.MaternityCatm1Entity;
import org.springframework.stereotype.Component;

@Component
public class MaternityCatm1Mapper {
    public static MaternityCatm1Entity toEntity(MaternityCatm1Request dto) {
        return MaternityCatm1Entity.builder()
                .maternityID(dto.getMaternityID())
                .co2(dto.getCo2())
                .time(dto.getTime())
                .humidity(dto.getHumidity())
                .temper(dto.getTemper())
                .wtemper(dto.getWTemper())
                .build();
    }

    public MaternityCatm1Response toResponse(MaternityCatm1Entity entity){
        return MaternityCatm1Response.builder()
                .sensorid3(entity.getSensorID() != null ? entity.getSensorID().toString() : null) //integer -> String
                .maternityID(entity.getMaternityID())
                .co2(entity.getCo2())
                .temper(entity.getTemper())
                .wtemper(entity.getWtemper())
                .humidity(entity.getHumidity())
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
