package com.farmin.farminserver.domain.barns.boars.service;

import com.farmin.farminserver.domain.barns.boars.dto.BoarsRequest;
import com.farmin.farminserver.domain.barns.boars.dto.BoarsResponse;
import com.farmin.farminserver.domain.barns.boars.mapper.BoarsMapper;
import com.farmin.farminserver.entity.barns.boars.BoarsEntity;
import com.farmin.farminserver.entity.barns.boars.BoarsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoarsServiceImpl implements BoarsService {
    private final BoarsRepository boarsRepository;

    @Override
    public BoarsResponse createBoars(BoarsRequest dto) {
        BoarsEntity entity = BoarsMapper.toEntity(dto);
        BoarsEntity savedEntity = boarsRepository.save(entity);
        return BoarsMapper.toResponseDTO(savedEntity);
    }

    @Override
    public BoarsResponse getBoarsById(Integer id) {
        BoarsEntity entity = boarsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boars entity not found"));
        return BoarsMapper.toResponseDTO(entity);
    }
}
