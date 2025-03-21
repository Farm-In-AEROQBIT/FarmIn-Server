package com.farmin.farminserver.domain.catm1.boarscatm1.service;

import com.farmin.farminserver.domain.catm1.boarscatm1.dto.BoarsCatm1Request;
import com.farmin.farminserver.domain.catm1.boarscatm1.dto.BoarsCatm1Response;
import com.farmin.farminserver.domain.catm1.boarscatm1.mapper.BoarsCatm1Mapper;
import com.farmin.farminserver.entity.catm1.boarscatm1sensor.BoarsCatm1Entity;
import com.farmin.farminserver.entity.catm1.boarscatm1sensor.BoarsCatm1Repository;
import com.farmin.farminserver.entity.catm1.growingcatm1sensor.GrowingCatm1Entity;
import com.farmin.farminserver.entity.catm1.growingcatm1sensor.GrowingCatm1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoarsCatm1ServiceImpl implements BoarsCatm1Service {

    private final BoarsCatm1Repository boarscatm1Repository;
    private final BoarsCatm1Mapper boarscatm1mapper;

    @Override
    public BoarsCatm1Response createBoarsCatm1(BoarsCatm1Request request) {
        BoarsCatm1Entity entity = boarscatm1mapper.toEntity(request);
        boarscatm1Repository.save(entity);
        return boarscatm1mapper.toResponse(entity);
    }

    @Override
    public List<BoarsCatm1Response> getAllBoarsCatm1() {
        return boarscatm1Repository.findAll().stream()
                .map(boarscatm1mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BoarsCatm1Response getBoarsCatm1ById(int id) {
        BoarsCatm1Entity entity = boarscatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return boarscatm1mapper.toResponse(entity);
    }

    @Override
    public BoarsCatm1Response updateBoarsCatm1(int id, BoarsCatm1Request request) {
        BoarsCatm1Entity entity = boarscatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        // 필드 업데이트
        entity.setCo2(request.getCo2());
        entity.setWtemper(request.getWTemper());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());

        boarscatm1Repository.save(entity);
        return boarscatm1mapper.toResponse(entity);
    }

    @Override
    public void deleteBoarsCatm1(int id) {
        boarscatm1Repository.deleteById(id);
    }

    @Override
    public List<BoarsCatm1Response> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<BoarsCatm1Entity> catm1;

        switch (type.toLowerCase()) {
            case "yearly":
                catm1 = boarscatm1Repository.findByYear(year);
                break;
            case "monthly":
                catm1 = boarscatm1Repository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                catm1 = boarscatm1Repository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                catm1 = boarscatm1Repository.findByYearMonthAndDay(year, month, weekOrDay);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        return catm1.stream()
                .map(boarscatm1mapper::toResponse)
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
