package com.farmin.farminserver.domain.barns.gestation.service;

import com.farmin.farminserver.domain.barns.gestation.dto.GestationRequest;
import com.farmin.farminserver.domain.barns.gestation.dto.GestationResponse;
import com.farmin.farminserver.domain.barns.gestation.mapper.GestationMapper;
import com.farmin.farminserver.entity.barns.gestation.GestationEntity;
import com.farmin.farminserver.entity.barns.gestation.GestationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GestationServiceImpl implements GestationService {
    private final GestationRepository gestationRepository;

    @Override
    public GestationResponse createGestation(GestationRequest dto) {
        GestationEntity entity = GestationMapper.toEntity(dto);
        GestationEntity savedEntity = gestationRepository.save(entity);
        return GestationMapper.toResponseDTO(savedEntity);
    }

    @Override
    public GestationResponse getGestationById(Integer id) {
        GestationEntity entity = gestationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gestation entity not found"));
        return GestationMapper.toResponseDTO(entity);
    }
}
