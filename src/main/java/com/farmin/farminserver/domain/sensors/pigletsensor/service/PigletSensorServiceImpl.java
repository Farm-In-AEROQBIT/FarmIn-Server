package com.farmin.farminserver.domain.sensors.pigletsensor.service;

import com.farmin.farminserver.domain.sensors.pigletsensor.dto.PigletSensorRequest;
import com.farmin.farminserver.domain.sensors.pigletsensor.dto.PigletSensorResponse;
import com.farmin.farminserver.domain.sensors.pigletsensor.mapper.PigletSensorMapper;
import com.farmin.farminserver.entity.sensors.growingsensor.GrowingSensorEntity;
import com.farmin.farminserver.entity.sensors.growingsensor.GrowingSensorRepository;
import com.farmin.farminserver.entity.sensors.pigletsensor.PigletSensorEntity;
import com.farmin.farminserver.entity.sensors.pigletsensor.PigletSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PigletSensorServiceImpl implements PigletSensorService {

    private final PigletSensorRepository pigletSensorRepository;
    private final PigletSensorMapper mapper;

    @Override
    public PigletSensorResponse createPigletSensor(PigletSensorRequest request) {
        PigletSensorEntity entity = mapper.toEntity(request);
        pigletSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public List<PigletSensorResponse> getAllPigletSensors() {
        return pigletSensorRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PigletSensorResponse getPigletSensorById(int id) {
        PigletSensorEntity entity = pigletSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return mapper.toResponse(entity);
    }

    @Override
    public PigletSensorResponse updatePigletSensor(int id, PigletSensorRequest request) {
        PigletSensorEntity entity = pigletSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        // 필드 업데이트
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPm(request.getPm());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());

        pigletSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deletePigletSensor(int id) {
        pigletSensorRepository.deleteById(id);
    }

    @Override
    public List<PigletSensorResponse> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<PigletSensorEntity> sensors;

        switch (type.toLowerCase()) {
            case "yearly":
                sensors = pigletSensorRepository.findByYear(year);
                break;
            case "monthly":
                sensors = pigletSensorRepository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                sensors = pigletSensorRepository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                sensors = pigletSensorRepository.findByYearMonthAndDay(year, month, weekOrDay);
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
