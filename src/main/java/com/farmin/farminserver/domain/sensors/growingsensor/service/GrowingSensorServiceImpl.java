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
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPM(request.getPM());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());
        growingSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deleteGrowingSensor(int id) {
        growingSensorRepository.deleteById(id);
    }
}
