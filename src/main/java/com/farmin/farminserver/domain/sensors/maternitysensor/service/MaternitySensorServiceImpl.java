package com.farmin.farminserver.domain.sensors.maternitysensor.service;

import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorRequest;
import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorResponse;
import com.farmin.farminserver.domain.sensors.maternitysensor.mapper.MaternitySensorMapper;
import com.farmin.farminserver.entity.sensors.growingsensor.GrowingSensorEntity;
import com.farmin.farminserver.entity.sensors.growingsensor.GrowingSensorRepository;
import com.farmin.farminserver.entity.sensors.maternitysensor.MaternitySensorEntity;
import com.farmin.farminserver.entity.sensors.maternitysensor.MaternitySensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaternitySensorServiceImpl implements MaternitySensorService {

    private final MaternitySensorRepository maternitySensorRepository;
    private final MaternitySensorMapper mapper;

    @Override
    public MaternitySensorResponse createMaternitySensor(MaternitySensorRequest request) {
        MaternitySensorEntity entity = mapper.toEntity(request);
        maternitySensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public List<MaternitySensorResponse> getAllMaternitySensors() {
        return maternitySensorRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MaternitySensorResponse getMaternitySensorById(int id) {
        MaternitySensorEntity entity = maternitySensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return mapper.toResponse(entity);
    }

    @Override
    public MaternitySensorResponse updateMaternitySensor(int id, MaternitySensorRequest request) {
        MaternitySensorEntity entity = maternitySensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        // 필드 업데이트
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPm(request.getPm());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());

        maternitySensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deleteMaternitySensor(int id) {
        maternitySensorRepository.deleteById(id);
    }

    @Override
    public List<MaternitySensorResponse> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<MaternitySensorEntity> sensors;

        switch (type.toLowerCase()) {
            case "yearly":
                sensors = maternitySensorRepository.findByYear(year);
                break;
            case "monthly":
                sensors = maternitySensorRepository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                sensors = maternitySensorRepository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                sensors = maternitySensorRepository.findByYearMonthAndDay(year, month, weekOrDay);
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
