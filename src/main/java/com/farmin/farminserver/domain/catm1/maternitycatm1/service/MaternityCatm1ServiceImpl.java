package com.farmin.farminserver.domain.catm1.maternitycatm1.service;

import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1Request;
import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1Response;
import com.farmin.farminserver.domain.catm1.maternitycatm1.mapper.MaternityCatm1Mapper;
import com.farmin.farminserver.entity.catm1.maternitycatm1sensor.MaternityCatm1Entity;
import com.farmin.farminserver.entity.catm1.maternitycatm1sensor.MaternityCatm1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaternityCatm1ServiceImpl implements MaternityCatm1Service {

    private final MaternityCatm1Repository maternitycatm1Repository;
    private final MaternityCatm1Mapper maternitycatm1mapper;

    @Override
    public MaternityCatm1Response createMaternityCatm1(MaternityCatm1Request request) {
        MaternityCatm1Entity entity = maternitycatm1mapper.toEntity(request);
        maternitycatm1Repository.save(entity);
        return maternitycatm1mapper.toResponse(entity);
    }

    @Override
    public List<MaternityCatm1Response> getAllMaternityCatm1() {
        return maternitycatm1Repository.findAll().stream()
                .map(maternitycatm1mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MaternityCatm1Response getMaternityCatm1ById(int id) {
        MaternityCatm1Entity entity = maternitycatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return maternitycatm1mapper.toResponse(entity);
    }

    @Override
    public MaternityCatm1Response updateMaternityCatm1(int id, MaternityCatm1Request request) {
        var entity = maternitycatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        entity.setTemper(request.getTemper());
        entity.setWtemper(request.getWTemper());
        entity.setHumidity(request.getHumidity());
        entity.setCo2(request.getCo2());

        return maternitycatm1mapper.toResponse(maternitycatm1Repository.save(entity));
    }

    @Override
    public void deleteMaternityCatm1(int id) {
        maternitycatm1Repository.deleteById(id);
    }

    @Override
    public List<MaternityCatm1Response> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<MaternityCatm1Entity> catm1;

        switch (type.toLowerCase()) {
            case "yearly" -> {
                int parsedYear = Integer.parseInt(year);
                catm1 = maternitycatm1Repository.findByYear(parsedYear);
            }
            case "monthly" -> {
                int parsedYear = Integer.parseInt(year);
                int parsedMonth = Integer.parseInt(month);
                catm1 = maternitycatm1Repository.findByYearAndMonth(parsedYear, parsedMonth);
            }
            case "weekly" -> {
                String[] range = calculateDateRange(year, month, weekOrDay);
                LocalDateTime start = LocalDateTime.parse(range[0] + "T00:00:00");
                LocalDateTime end = LocalDateTime.parse(range[1] + "T23:59:59");
                catm1 = maternitycatm1Repository.findByDateRange(start, end);
            }
            case "daily" -> {
                int parsedYear = Integer.parseInt(year);
                int parsedMonth = Integer.parseInt(month);
                int parsedDay = Integer.parseInt(weekOrDay);
                catm1 = maternitycatm1Repository.findByYearMonthAndDay(parsedYear, parsedMonth, parsedDay);
            }
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        }

        return catm1.stream()
                .map(maternitycatm1mapper::toResponse)
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
