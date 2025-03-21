package com.farmin.farminserver.domain.catm1.boarscatm1.mapper;

import com.farmin.farminserver.domain.catm1.boarscatm1.dto.BoarsCatm1Request;
import com.farmin.farminserver.domain.catm1.boarscatm1.dto.BoarsCatm1Response;
import com.farmin.farminserver.domain.catm1.boarscatm1.dto.BoarsCatm1StatisticsResponse;
import com.farmin.farminserver.entity.catm1.boarscatm1sensor.BoarsCatm1Entity;
import org.springframework.stereotype.Component;

@Component
public class BoarsCatm1Mapper {
    public static BoarsCatm1Entity toEntity(BoarsCatm1Request dto) {
        return BoarsCatm1Entity.builder()
                .boarsID(dto.getBoarsID())
                .co2(dto.getCo2())
                .time(dto.getTime())
                .humidity(dto.getHumidity())
                .temper(dto.getTemper())
                .wtemper(dto.getWTemper())
                .build();
    }

    public BoarsCatm1Response toResponse(BoarsCatm1Entity entity){
        return BoarsCatm1Response.builder()
                .sensorid3(entity.getSensorID() != null ? entity.getSensorID().toString() : null) //integer -> String
                .boarsID(entity.getBoarsID())
                .co2(entity.getCo2())
                .temper(entity.getTemper())
                .wtemper(entity.getWtemper())
                .humidity(entity.getHumidity())
                .time(entity.getTime())
                .build();
    }

    public BoarsCatm1StatisticsResponse toStatisticsResponse(String label, String avgCo2, String avgTemper, String avgWTemper, String avgHumidity){
        return BoarsCatm1StatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageWTemper(avgWTemper != null ? String.format("%.2f", avgWTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
