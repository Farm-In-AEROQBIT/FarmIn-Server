package com.farmin.farminserver.domain.sensors.reservesensor.service;

import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorRequest;
import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorResponse;
import com.farmin.farminserver.domain.sensors.reservesensor.mapper.ReserveSensorMapper;
import com.farmin.farminserver.entity.sensors.growingsensor.GrowingSensorEntity;
import com.farmin.farminserver.entity.sensors.growingsensor.GrowingSensorRepository;
import com.farmin.farminserver.entity.sensors.reservesensor.ReserveSensorEntity;
import com.farmin.farminserver.entity.sensors.reservesensor.ReserveSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReserveSensorServiceImpl implements ReserveSensorService {

    private final ReserveSensorRepository reserveSensorRepository;
    private final ReserveSensorMapper mapper;

    @Override
    public ReserveSensorResponse createReserveSensor(ReserveSensorRequest request) {
        ReserveSensorEntity entity = mapper.toEntity(request);
        reserveSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public List<ReserveSensorResponse> getAllReserveSensors() {
        return reserveSensorRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReserveSensorResponse getReserveSensorById(int id) {
        ReserveSensorEntity entity = reserveSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        return mapper.toResponse(entity);
    }

    @Override
    public ReserveSensorResponse updateReserveSensor(int id, ReserveSensorRequest request) {
        ReserveSensorEntity entity = reserveSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        // 필드 업데이트
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPm(request.getPm());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());

        reserveSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deleteReserveSensor(int id) {
        reserveSensorRepository.deleteById(id);
    }

    @Override
    public List<ReserveSensorResponse> getStatistics(String type, String year, String month, String weekOrDay) {
        if (type == null || type.trim().isEmpty()) {
            type = "yearly";
        }

        List<ReserveSensorEntity> sensors;

        switch (type.toLowerCase()) {
            case "yearly":
                sensors = reserveSensorRepository.findByYear(year);
                break;
            case "monthly":
                sensors = reserveSensorRepository.findByYearAndMonth(year, month);
                break;
            case "weekly":
                String[] dateRange = calculateDateRange(year, month, weekOrDay);
                sensors = reserveSensorRepository.findByDateRange(dateRange[0], dateRange[1]);
                break;
            case "daily":
                sensors = reserveSensorRepository.findByYearMonthAndDay(year, month, weekOrDay);
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
