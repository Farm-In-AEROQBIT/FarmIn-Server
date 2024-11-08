package com.farmin.farminserver.domain.barns.growing.service;

import com.farmin.farminserver.domain.barns.growing.dto.GrowingRequest;
import com.farmin.farminserver.domain.barns.growing.dto.GrowingResponse;
import com.farmin.farminserver.domain.barns.growing.mapper.GrowingMapper;
import com.farmin.farminserver.entity.barns.growing.GrowingEntity;
import com.farmin.farminserver.entity.barns.growing.GrowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrowingServiceImpl implements GrowingService {
    private final GrowingRepository growingRepository;

    @Override
    public GrowingResponse createGrowing(GrowingRequest dto) {
        GrowingEntity entity = GrowingMapper.toEntity(dto);
        GrowingEntity savedEntity = growingRepository.save(entity);
        return GrowingMapper.toResponseDTO(savedEntity);
    }

    @Override
    public GrowingResponse getGrowingById(Integer id) {
        GrowingEntity entity = growingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Growing entity not found"));
        return GrowingMapper.toResponseDTO(entity);
    }
}
