package com.farmin.farminserver.domain.barns.finishing.service;

import com.farmin.farminserver.domain.barns.finishing.dto.FinishingRequest;
import com.farmin.farminserver.domain.barns.finishing.dto.FinishingResponse;
import com.farmin.farminserver.domain.barns.finishing.mapper.FinishingMapper;
import com.farmin.farminserver.entity.barns.finishing.FinishingEntity;
import com.farmin.farminserver.entity.barns.finishing.FinishingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinishingServiceImpl implements FinishingService {
    private final FinishingRepository finishingRepository;

    @Override
    public FinishingResponse createFinishing(FinishingRequest dto) {
        FinishingEntity entity = FinishingMapper.toEntity(dto);
        FinishingEntity savedEntity = finishingRepository.save(entity);
        return FinishingMapper.toResponseDTO(savedEntity);
    }

    @Override
    public FinishingResponse getFinishingById(Integer id) {
        FinishingEntity entity = finishingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Finishing entity not found"));
        return FinishingMapper.toResponseDTO(entity);
    }
}
