package com.farmin.farminserver.domain.sensors.pigletsensor.service;

import com.farmin.farminserver.domain.sensors.pigletsensor.dto.PigletSensorRequest;
import com.farmin.farminserver.domain.sensors.pigletsensor.dto.PigletSensorResponse;
import com.farmin.farminserver.domain.sensors.pigletsensor.mapper.PigletSensorMapper;
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
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPM(request.getPM());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());
        pigletSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deletePigletSensor(int id) {
        pigletSensorRepository.deleteById(id);
    }
}
