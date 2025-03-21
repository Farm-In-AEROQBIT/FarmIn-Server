package com.farmin.farminserver.domain.sensors.finishingsensor.service;

import com.farmin.farminserver.domain.sensors.finishingsensor.dto.FinishingSensorRequest;
import com.farmin.farminserver.domain.sensors.finishingsensor.dto.FinishingSensorResponse;
import com.farmin.farminserver.domain.sensors.finishingsensor.mapper.FinishingSensorMapper;
import com.farmin.farminserver.entity.sensors.finishingsensor.FinishingSensorEntity;
import com.farmin.farminserver.entity.sensors.finishingsensor.FinishingSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinishingSensorServiceImpl implements FinishingSensorService {

    private final FinishingSensorRepository finishingSensorRepository;
    private final FinishingSensorMapper mapper;

    @Override
    public FinishingSensorResponse createFinishingSensor(FinishingSensorRequest request) {
        FinishingSensorEntity entity = mapper.toEntity(request);
        finishingSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public List<FinishingSensorResponse> getAllFinishingSensors() {
        return finishingSensorRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FinishingSensorResponse getFinishingSensorById(int id) {
        FinishingSensorEntity entity = finishingSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return mapper.toResponse(entity);
    }

    @Override
    public FinishingSensorResponse updateFinishingSensor(int id, FinishingSensorRequest request) {
        FinishingSensorEntity entity = finishingSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        // 필드 업데이트
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPm(request.getPm());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());

        finishingSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deleteFinishingSensor(int id) {
        finishingSensorRepository.deleteById(id);
    }

    @Override
    public List<FinishingSensorResponse> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<FinishingSensorEntity> sensors;

        switch (type.toLowerCase()) {
            case "yearly":
                sensors = finishingSensorRepository.findByYear(year);
                break;
            case "monthly":
                sensors = finishingSensorRepository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                sensors = finishingSensorRepository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                sensors = finishingSensorRepository.findByYearMonthAndDay(year, month, weekOrDay);
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
