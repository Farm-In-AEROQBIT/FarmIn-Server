package com.farmin.farminserver.domain.catm1.growingcatm1.service;

import com.farmin.farminserver.domain.catm1.growingcatm1.dto.GrowingCatm1Request;
import com.farmin.farminserver.domain.catm1.growingcatm1.dto.GrowingCatm1Response;
import com.farmin.farminserver.domain.catm1.growingcatm1.mapper.GrowingCatm1Mapper;
import com.farmin.farminserver.entity.catm1.growingcatm1sensor.GrowingCatm1Entity;
import com.farmin.farminserver.entity.catm1.growingcatm1sensor.GrowingCatm1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrowingCatm1ServiceImpl implements GrowingCatm1Service {

    private final GrowingCatm1Repository growingcatm1Repository;
    private final GrowingCatm1Mapper growingcatm1mapper;

    @Override
    public GrowingCatm1Response createGrowingCatm1(GrowingCatm1Request request) {
        GrowingCatm1Entity entity = growingcatm1mapper.toEntity(request);
        growingcatm1Repository.save(entity);
        return growingcatm1mapper.toResponse(entity);
    }

    @Override
    public List<GrowingCatm1Response> getAllGrowingCatm1() {
        return growingcatm1Repository.findAll().stream()
                .map(growingcatm1mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GrowingCatm1Response getGrowingCatm1ById(int id) {
        GrowingCatm1Entity entity = growingcatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return growingcatm1mapper.toResponse(entity);
    }

    @Override
    public GrowingCatm1Response updateGrowingCatm1(int id, GrowingCatm1Request request) {
        var entity = growingcatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        entity.setTemper(request.getTemper());
        entity.setWtemper(request.getWTemper());
        entity.setHumidity(request.getHumidity());
        entity.setCo2(request.getCo2());

        return growingcatm1mapper.toResponse(growingcatm1Repository.save(entity));
    }

    @Override
    public void deleteGrowingCatm1(int id) {
        growingcatm1Repository.deleteById(id);
    }

    @Override
    public List<GrowingCatm1Response> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<GrowingCatm1Entity> catm1;

        switch (type.toLowerCase()) {
            case "yearly":
                catm1 = growingcatm1Repository.findByYear(year);
                break;
            case "monthly":
                catm1 = growingcatm1Repository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                catm1 = growingcatm1Repository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                catm1 = growingcatm1Repository.findByYearMonthAndDay(year, month, weekOrDay);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        return catm1.stream()
                .map(growingcatm1mapper::toResponse)
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
