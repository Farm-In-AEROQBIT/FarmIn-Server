package com.farmin.farminserver.domain.sensors.boarssensor.service;

import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorRequest;
import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorResponse;
import com.farmin.farminserver.domain.sensors.boarssensor.mapper.BoarsSensorMapper;
import com.farmin.farminserver.entity.sensors.boarssensor.BoarsSensorEntity;
import com.farmin.farminserver.entity.sensors.boarssensor.BoarsSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoarsSensorServiceImpl implements BoarsSensorService {

    private final BoarsSensorRepository boarsSensorRepository;
    private final BoarsSensorMapper mapper;

    @Override
    public BoarsSensorResponse createBoarsSensor(BoarsSensorRequest request) {
        BoarsSensorEntity entity = mapper.toEntity(request);
        boarsSensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public BoarsSensorResponse getBoarsSensorById(Integer id) {
        BoarsSensorEntity entity = boarsSensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BoarsSensor entity not found"));
        return mapper.toResponse(entity);
    }

    @Override
    public List<BoarsSensorResponse> getAllBoarsSensors() {
        return boarsSensorRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBoarsSensor(Integer id) {
        boarsSensorRepository.deleteById(id);
    }
}
