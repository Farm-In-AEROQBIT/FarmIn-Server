package com.farmin.farminserver.domain.catm1.reservecatm1.service;

import com.farmin.farminserver.domain.catm1.boarscatm1.dto.BoarsCatm1Response;
import com.farmin.farminserver.domain.catm1.reservecatm1.dto.ReserveCatm1Request;
import com.farmin.farminserver.domain.catm1.reservecatm1.dto.ReserveCatm1Response;
import com.farmin.farminserver.domain.catm1.reservecatm1.mapper.ReserveCatm1Mapper;
import com.farmin.farminserver.entity.catm1.boarscatm1sensor.BoarsCatm1Entity;
import com.farmin.farminserver.entity.catm1.growingcatm1sensor.GrowingCatm1Entity;
import com.farmin.farminserver.entity.catm1.growingcatm1sensor.GrowingCatm1Repository;
import com.farmin.farminserver.entity.catm1.reservecatm1sensor.ReserveCatm1Entity;
import com.farmin.farminserver.entity.catm1.reservecatm1sensor.ReserveCatm1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReserveCatm1ServiceImpl implements ReserveCatm1Service {

    private final ReserveCatm1Repository reservecatm1Repository;
    private final ReserveCatm1Mapper reservecatm1mapper;

    @Override
    public ReserveCatm1Response createReserveCatm1(ReserveCatm1Request request) {
        ReserveCatm1Entity entity = reservecatm1mapper.toEntity(request);
        reservecatm1Repository.save(entity);
        return reservecatm1mapper.toResponse(entity);
    }

    @Override
    public List<ReserveCatm1Response> getAllReserveCatm1() {
        return reservecatm1Repository.findAll().stream()
                .map(reservecatm1mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReserveCatm1Response getReserveCatm1ById(int id) {
        ReserveCatm1Entity entity = reservecatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return reservecatm1mapper.toResponse(entity);
    }

    @Override
    public ReserveCatm1Response updateReserveCatm1(int id, ReserveCatm1Request request) {
        var entity = reservecatm1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        entity.setTemper(request.getTemper());
        entity.setWtemper(request.getWTemper());
        entity.setHumidity(request.getHumidity());
        entity.setCo2(request.getCo2());

        return reservecatm1mapper.toResponse(reservecatm1Repository.save(entity));
    }

    @Override
    public void deleteReserveCatm1(int id) {
        reservecatm1Repository.deleteById(id);
    }

    @Override
    public List<ReserveCatm1Response> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<ReserveCatm1Entity> catm1;

        switch (type.toLowerCase()) {
            case "yearly" -> {
                int parsedYear = Integer.parseInt(year);
                catm1 = reservecatm1Repository.findByYear(parsedYear);
            }
            case "monthly" -> {
                int parsedYear = Integer.parseInt(year);
                int parsedMonth = Integer.parseInt(month);
                catm1 = reservecatm1Repository.findByYearAndMonth(parsedYear, parsedMonth);
            }
            case "weekly" -> {
                String[] range = calculateDateRange(year, month, weekOrDay);
                LocalDateTime start = LocalDateTime.parse(range[0] + "T00:00:00");
                LocalDateTime end = LocalDateTime.parse(range[1] + "T23:59:59");
                catm1 = reservecatm1Repository.findByDateRange(start, end);
            }
            case "daily" -> {
                int parsedYear = Integer.parseInt(year);
                int parsedMonth = Integer.parseInt(month);
                int parsedDay = Integer.parseInt(weekOrDay);
                catm1 = reservecatm1Repository.findByYearMonthAndDay(parsedYear, parsedMonth, parsedDay);
            }
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        }

        return catm1.stream()
                .map(reservecatm1mapper::toResponse)
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
