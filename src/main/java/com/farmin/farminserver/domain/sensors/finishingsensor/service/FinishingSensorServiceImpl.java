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
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPM(request.getPm());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());
        finishingSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deleteFinishingSensor(int id) {
        finishingSensorRepository.deleteById(id);
    }
}
