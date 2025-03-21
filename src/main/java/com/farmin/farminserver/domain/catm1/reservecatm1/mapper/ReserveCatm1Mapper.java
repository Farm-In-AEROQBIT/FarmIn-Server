package com.farmin.farminserver.domain.catm1.reservecatm1.mapper;

import com.farmin.farminserver.domain.catm1.reservecatm1.dto.ReserveCatm1Request;
import com.farmin.farminserver.domain.catm1.reservecatm1.dto.ReserveCatm1Response;
import com.farmin.farminserver.domain.catm1.reservecatm1.dto.ReserveCatm1StatisticsResponse;
import com.farmin.farminserver.entity.catm1.growingcatm1sensor.GrowingCatm1Entity;
import com.farmin.farminserver.entity.catm1.reservecatm1sensor.ReserveCatm1Entity;
import org.springframework.stereotype.Component;

@Component
public class ReserveCatm1Mapper {
    public static ReserveCatm1Entity toEntity(ReserveCatm1Request dto) {
        return ReserveCatm1Entity.builder()
                .reserveID(dto.getReserveID())
                .co2(dto.getCo2())
                .time(dto.getTime())
                .humidity(dto.getHumidity())
                .temper(dto.getTemper())
                .wtemper(dto.getWTemper())
                .build();
    }

    public ReserveCatm1Response toResponse(ReserveCatm1Entity entity){
        return ReserveCatm1Response.builder()
                .sensorid3(entity.getSensorID() != null ? entity.getSensorID().toString() : null) //integer -> String
                .reserveID(entity.getReserveID())
                .co2(entity.getCo2())
                .temper(entity.getTemper())
                .wtemper(entity.getWtemper())
                .humidity(entity.getHumidity())
                .time(entity.getTime())
                .build();
    }

    public ReserveCatm1StatisticsResponse toStatisticsResponse(String label, String avgCo2, String avgTemper, String avgWTemper, String avgHumidity){
        return ReserveCatm1StatisticsResponse.builder()
                .label(label)
                .averageCo2(avgCo2 != null ? String.format("%.2f", avgCo2) : null)
                .averageTemper(avgTemper != null ? String.format("%.2f", avgTemper) : null)
                .averageWTemper(avgWTemper != null ? String.format("%.2f", avgWTemper) : null)
                .averageHumidity(avgHumidity != null ? String.format("%.2f", avgHumidity) : null)
                .build();
    }
}
