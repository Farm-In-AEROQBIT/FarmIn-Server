package com.farmin.farminserver.domain.sensors.growingsensor.service;

import com.farmin.farminserver.domain.sensors.growingsensor.dto.GrowingSensorRequest;
import com.farmin.farminserver.domain.sensors.growingsensor.dto.GrowingSensorResponse;
import com.farmin.farminserver.domain.sensors.growingsensor.mapper.GrowingSensorMapper;
import com.farmin.farminserver.entity.sensors.growingsensor.GrowingSensorEntity;
import com.farmin.farminserver.entity.sensors.growingsensor.GrowingSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrowingSensorServiceImpl implements GrowingSensorService {

    private final GrowingSensorRepository growingSensorRepository;
    private final GrowingSensorMapper mapper;

    @Override
    public GrowingSensorResponse createGrowingSensor(GrowingSensorRequest request) {
        GrowingSensorEntity entity = mapper.toEntity(request);
        growingSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public List<GrowingSensorResponse> getAllGrowingSensors() {
        return growingSensorRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GrowingSensorResponse getGrowingSensorById(int id) {
        GrowingSensorEntity entity = growingSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return mapper.toResponse(entity);
    }

    @Override
    public GrowingSensorResponse updateGrowingSensor(int id, GrowingSensorRequest request) {
        GrowingSensorEntity entity = growingSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        // 필드 업데이트
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPm(request.getPm());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());

        growingSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deleteGrowingSensor(int id) {
        growingSensorRepository.deleteById(id);
    }

    @Override
    public List<GrowingSensorResponse> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<GrowingSensorEntity> sensors;

        switch (type.toLowerCase()) {
            case "yearly":
                sensors = growingSensorRepository.findByYear(year);
                break;
            case "monthly":
                sensors = growingSensorRepository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                sensors = growingSensorRepository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                sensors = growingSensorRepository.findByYearMonthAndDay(year, month, weekOrDay);
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
