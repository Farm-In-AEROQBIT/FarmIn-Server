package com.farmin.farminserver.domain.catm1.finishingcatm1.service;

import com.farmin.farminserver.domain.catm1.finishingcatm1.dto.FinishingCatm1Request;
import com.farmin.farminserver.domain.catm1.finishingcatm1.dto.FinishingCatm1Response;
import com.farmin.farminserver.domain.catm1.finishingcatm1.mapper.FinishingCatm1Mapper;
import com.farmin.farminserver.entity.catm1.finishingcatm1sensor.FinishingCatm1Entity;
import com.farmin.farminserver.entity.catm1.finishingcatm1sensor.FinishingCatm1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinishingCatm1ServiceImpl implements FinishingCatm1Service {

    private final FinishingCatm1Repository finishingcatm1Repository;
    private final FinishingCatm1Mapper finishingcatm1mapper;

    @Override
    public FinishingCatm1Response createFinishingCatm1(FinishingCatm1Request request) {
        FinishingCatm1Entity entity = finishingcatm1mapper.toEntity(request);
        finishingcatm1Repository.save(entity);
        return finishingcatm1mapper.toResponse(entity);
    }

    @Override
    public List<FinishingCatm1Response> getAllFinishingCatm1() {
        return finishingcatm1Repository.findAll().stream()
                .map(finishingcatm1mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FinishingCatm1Response getFinishingCatm1ById(int id) {
        FinishingCatm1Entity entity = finishingcatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return finishingcatm1mapper.toResponse(entity);
    }

    @Override
    public FinishingCatm1Response updateFinishingCatm1(int id, FinishingCatm1Request request) {
        FinishingCatm1Entity entity = finishingcatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());
        entity.setWtemper(request.getWTemper());
        entity.setCo2(request.getCo2());

        return finishingcatm1mapper.toResponse(finishingcatm1Repository.save(entity));
    }
    @Override
    public void deleteFinishingCatm1(int id) {
        finishingcatm1Repository.deleteById(id);
    }

    @Override
    public List<FinishingCatm1Response> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<FinishingCatm1Entity> catm1;

        switch (type.toLowerCase()) {
            case "yearly":
                catm1 = finishingcatm1Repository.findByYear(year);
                break;
            case "monthly":
                catm1 = finishingcatm1Repository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                catm1 = finishingcatm1Repository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                catm1 = finishingcatm1Repository.findByYearMonthAndDay(year, month, weekOrDay);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        return catm1.stream()
                .map(finishingcatm1mapper::toResponse)
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
