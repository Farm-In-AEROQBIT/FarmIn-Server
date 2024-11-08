package com.farmin.farminserver.domain.sensors.maternitysensor.service;

import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorRequest;
import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorResponse;
import com.farmin.farminserver.domain.sensors.maternitysensor.mapper.MaternitySensorMapper;
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
        entity.setCo2(request.getCo2());
        entity.setNh3(request.getNh3());
        entity.setPM(request.getPM());
        entity.setTemper(request.getTemper());
        entity.setHumidity(request.getHumidity());
        maternitySensorRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void deleteMaternitySensor(int id) {
        maternitySensorRepository.deleteById(id);
    }
}
