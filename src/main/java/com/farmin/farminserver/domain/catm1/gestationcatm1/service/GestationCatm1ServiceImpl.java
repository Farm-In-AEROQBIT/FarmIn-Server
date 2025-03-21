package com.farmin.farminserver.domain.catm1.gestationcatm1.service;

import com.farmin.farminserver.domain.catm1.gestationcatm1.dto.GestationCatm1Request;
import com.farmin.farminserver.domain.catm1.gestationcatm1.dto.GestationCatm1Response;
import com.farmin.farminserver.domain.catm1.gestationcatm1.mapper.GestationCatm1Mapper;
import com.farmin.farminserver.entity.catm1.gestationcatm1sensor.GestationCatm1Entity;
import com.farmin.farminserver.entity.catm1.gestationcatm1sensor.GestationCatm1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GestationCatm1ServiceImpl implements GestationCatm1Service {

    private final GestationCatm1Repository gestationcatm1Repository;
    private final GestationCatm1Mapper gestationcatm1mapper;

    @Override
    public GestationCatm1Response createGestationCatm1(GestationCatm1Request request) {
        GestationCatm1Entity entity = gestationcatm1mapper.toEntity(request);
        gestationcatm1Repository.save(entity);
        return gestationcatm1mapper.toResponse(entity);
    }

    @Override
    public List<GestationCatm1Response> getAllGestationCatm1() {
        return gestationcatm1Repository.findAll().stream()
                .map(gestationcatm1mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GestationCatm1Response getGestationCatm1ById(int id) {
        GestationCatm1Entity entity = gestationcatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return gestationcatm1mapper.toResponse(entity);
    }

    @Override
    public GestationCatm1Response updateGestationCatm1(int id, GestationCatm1Request request) {
        GestationCatm1Entity entity = gestationcatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        // 필드 업데이트
        entity.setCo2(request.getCo2());
        entity.setWtemper(request.getWTemper());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());

        gestationcatm1Repository.save(entity);
        return gestationcatm1mapper.toResponse(entity);
    }

    @Override
    public void deleteGestationCatm1(int id) {
        gestationcatm1Repository.deleteById(id);
    }

    @Override
    public List<GestationCatm1Response> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<GestationCatm1Entity> catm1;

        switch (type.toLowerCase()) {
            case "yearly":
                catm1 = gestationcatm1Repository.findByYear(year);
                break;
            case "monthly":
                catm1 = gestationcatm1Repository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                catm1 = gestationcatm1Repository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                catm1 = gestationcatm1Repository.findByYearMonthAndDay(year, month, weekOrDay);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        return catm1.stream()
                .map(gestationcatm1mapper::toResponse)
                .collect(Collectors.toList());
    }

    private String[] calculateDateRange(String year, String month, String week) {
        int weekNumber = Integer.parseInt(week);
        int startDay = (weekNumber - 1) * 7 + 1;
        int endDay = Math.min(startDay + 6, 31); // 최대 31일까지

        String startDate = String.format("%s-%02d-%02d", year, Integer.parseInt(month), startDay);
        String endDate = String.format("%s-%02d-%02d", year, Integer.parseInt(month), endDay);

        return new String[]{startDate, endDate};
    }
}
