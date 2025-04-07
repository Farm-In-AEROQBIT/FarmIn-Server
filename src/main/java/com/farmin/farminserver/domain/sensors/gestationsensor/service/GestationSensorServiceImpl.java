package com.farmin.farminserver.domain.sensors.gestationsensor.service;

import com.farmin.farminserver.domain.sensors.gestationsensor.dto.GestationSensorRequest;
import com.farmin.farminserver.domain.sensors.gestationsensor.dto.GestationSensorResponse;
import com.farmin.farminserver.domain.sensors.gestationsensor.mapper.GestationSensorMapper;
import com.farmin.farminserver.entity.sensors.gestationsensor.GestationSensorEntity;
import com.farmin.farminserver.entity.sensors.gestationsensor.GestationSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GestationSensorServiceImpl implements GestationSensorService {

    private final GestationSensorRepository gestationSensorRepository;
    private final GestationSensorMapper mapper;

    @Override
    public GestationSensorResponse createGestationSensor(GestationSensorRequest request) {
        GestationSensorEntity entity = mapper.toEntity(request);
        gestationSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public List<GestationSensorResponse> getAllGestationSensors() {
        return gestationSensorRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GestationSensorResponse getGestationSensorById(int id) {
        GestationSensorEntity entity = gestationSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return mapper.toResponse(entity);
    }

    @Override
    public GestationSensorResponse updateGestationSensor(int id, GestationSensorRequest request) {
        GestationSensorEntity entity = gestationSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        // 필드 업데이트
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPm(request.getPm());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());

        gestationSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deleteGestationSensor(int id) {
        gestationSensorRepository.deleteById(id);
    }

    @Override
    public List<GestationSensorResponse> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<GestationSensorEntity> sensors;

        switch (type.toLowerCase()) {
            case "yearly":
                sensors = gestationSensorRepository.findByYear(year);
                break;
            case "monthly":
                sensors = gestationSensorRepository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                sensors = gestationSensorRepository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                sensors = gestationSensorRepository.findByYearMonthAndDay(year, month, weekOrDay);
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
