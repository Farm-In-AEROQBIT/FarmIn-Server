package com.farmin.farminserver.domain.catm1.pigletcatm1.mapper;

import com.farmin.farminserver.domain.catm1.pigletcatm1.dto.PigletCatm1Request;
import com.farmin.farminserver.domain.catm1.pigletcatm1.dto.PigletCatm1Response;
import com.farmin.farminserver.domain.catm1.pigletcatm1.dto.PigletCatm1StatisticsResponse;
import com.farmin.farminserver.entity.catm1.maternitycatm1sensor.MaternityCatm1Entity;
import com.farmin.farminserver.entity.catm1.pigletcatm1sensor.PigletCatm1Entity;
import org.springframework.stereotype.Component;

@Component
public class PigletCatm1Mapper {

    public PigletCatm1Entity toEntity(PigletCatm1Request dto) {
        return PigletCatm1Entity.builder()
                .pigletID(dto.getPigletID())
                .temper(dto.getTemper())
                .wtemper(dto.getWTemper())
                .humidity(dto.getHumidity())
                .co2(dto.getCo2())
                .time(dto.getTime())
                .build();
    }

    public PigletCatm1Response toResponse(PigletCatm1Entity entity) {
        return PigletCatm1Response.builder()
                .sensorIdc(entity.getSensorIdc())
                .pigletID(entity.getPigletID())
                .temper(entity.getTemper())
                .wTemper(entity.getWtemper())
                .humidity(entity.getHumidity())
                .co2(entity.getCo2())
                .time(entity.getTime())
                .build();
    }

    public PigletCatm1StatisticsResponse toStatisticsResponse(String label, String avgCo2, String avgTemper, String avgWTemper, String avgHumidity){
        return PigletCatm1StatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageWTemper(avgWTemper != null ? String.format("%.2f", avgWTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
