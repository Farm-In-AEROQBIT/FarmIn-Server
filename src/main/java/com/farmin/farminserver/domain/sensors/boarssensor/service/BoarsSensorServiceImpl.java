package com.farmin.farminserver.domain.sensors.boarssensor.service;

import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorRequest;
import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorResponse;
import com.farmin.farminserver.domain.sensors.boarssensor.mapper.BoarsSensorMapper;
import com.farmin.farminserver.entity.sensors.boarssensor.BoarsSensorEntity;
import com.farmin.farminserver.entity.sensors.boarssensor.BoarsSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoarsSensorServiceImpl implements BoarsSensorService {

    private final BoarsSensorRepository boarsSensorRepository;
    private final BoarsSensorMapper mapper;

    @Override
    public BoarsSensorResponse createBoarsSensor(BoarsSensorRequest request) {
        BoarsSensorEntity entity = mapper.toEntity(request);
        boarsSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public List<BoarsSensorResponse> getAllBoarsSensors() {
        return boarsSensorRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BoarsSensorResponse getBoarsSensorById(int id) {
        BoarsSensorEntity entity = boarsSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return mapper.toResponse(entity);
    }

    @Override
    public BoarsSensorResponse updateBoarsSensor(int id, BoarsSensorRequest request) {
        BoarsSensorEntity entity = boarsSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        // 필드 업데이트
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPm(request.getPm());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());

        boarsSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deleteBoarsSensor(int id) {
        boarsSensorRepository.deleteById(id);
    }

    @Override
    public List<BoarsSensorResponse> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<BoarsSensorEntity> sensors;

        switch (type.toLowerCase()) {
            case "yearly":
                sensors = boarsSensorRepository.findByYear(year);
                break;
            case "monthly":
                sensors = boarsSensorRepository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                sensors = boarsSensorRepository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                sensors = boarsSensorRepository.findByYearMonthAndDay(year, month, weekOrDay);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        return sensors.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 주간 통계 범위 계산
     */
    private String[] calculateDateRange(String year, String month, String week) {
        int weekNumber = Integer.parseInt(week);
        int startDay = (weekNumber - 1) * 7 + 1;
        int endDay = Math.min(startDay + 6, 31); // 최대 31일까지

        String startDate = String.format("%s-%02d-%02d", year, Integer.parseInt(month), startDay);
        String endDate = String.format("%s-%02d-%02d", year, Integer.parseInt(month), endDay);

        return new String[]{startDate, endDate};
    }
}
