package com.farmin.farminserver.domain.sensors.reservesensor.service;

import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorRequest;
import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorResponse;
import com.farmin.farminserver.domain.sensors.reservesensor.mapper.ReserveSensorMapper;
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
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPM(request.getPM());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());
        reserveSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deleteReserveSensor(int id) {
        reserveSensorRepository.deleteById(id);
    }
}
