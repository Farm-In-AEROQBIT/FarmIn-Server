package com.farmin.farminserver.domain.catm1.pigletcatm1.service;

import com.farmin.farminserver.domain.catm1.pigletcatm1.dto.PigletCatm1Request;
import com.farmin.farminserver.domain.catm1.pigletcatm1.dto.PigletCatm1Response;
import com.farmin.farminserver.domain.catm1.pigletcatm1.mapper.PigletCatm1Mapper;
import com.farmin.farminserver.entity.catm1.pigletcatm1sensor.PigletCatm1Entity;
import com.farmin.farminserver.entity.catm1.pigletcatm1sensor.PigletCatm1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PigletCatm1ServiceImpl implements PigletCatm1Service {

    private final PigletCatm1Repository pigletcatm1Repository;
    private final PigletCatm1Mapper pigletcatm1mapper;

    @Override
    public PigletCatm1Response createPigletCatm1(PigletCatm1Request request) {
        PigletCatm1Entity entity = pigletcatm1mapper.toEntity(request);
        pigletcatm1Repository.save(entity);
        return pigletcatm1mapper.toResponse(entity);
    }

    @Override
    public List<PigletCatm1Response> getAllPigletCatm1() {
        return pigletcatm1Repository.findAll().stream()
                .map(pigletcatm1mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PigletCatm1Response getPigletCatm1ById(int id) {
        PigletCatm1Entity entity = pigletcatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return pigletcatm1mapper.toResponse(entity);
    }

    @Override
    public PigletCatm1Response updatePigletCatm1(int id, PigletCatm1Request request) {
        PigletCatm1Entity entity = pigletcatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        // 필드 업데이트
        entity.setCo2(request.getCo2());
        entity.setWtemper(request.getWTemper());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());

        pigletcatm1Repository.save(entity);
        return pigletcatm1mapper.toResponse(entity);
    }

    @Override
    public void deletePigletCatm1(int id) {
        pigletcatm1Repository.deleteById(id);
    }

    @Override
    public List<PigletCatm1Response> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<PigletCatm1Entity> catm1;

        switch (type.toLowerCase()) {
            case "yearly":
                catm1 = pigletcatm1Repository.findByYear(year);
                break;
            case "monthly":
                catm1 = pigletcatm1Repository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                catm1 = pigletcatm1Repository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                catm1 = pigletcatm1Repository.findByYearMonthAndDay(year, month, weekOrDay);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        return catm1.stream()
                .map(pigletcatm1mapper::toResponse)
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
