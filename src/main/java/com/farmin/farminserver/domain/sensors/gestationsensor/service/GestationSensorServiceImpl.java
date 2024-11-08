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
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPM(request.getPM());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());
        gestationSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deleteGestationSensor(int id) {
        gestationSensorRepository.deleteById(id);
    }
}
