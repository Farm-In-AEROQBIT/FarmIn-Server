package com.farmin.farminserver.domain.barns.finishing.service;

import com.farmin.farminserver.domain.barns.finishing.dto.FinishingRequest;
import com.farmin.farminserver.domain.barns.finishing.dto.FinishingResponse;
import com.farmin.farminserver.domain.barns.finishing.mapper.FinishingMapper;
import com.farmin.farminserver.entity.barns.finishing.FinishingEntity;
import com.farmin.farminserver.entity.barns.finishing.FinishingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinishingServiceImpl implements FinishingService {

    private final FinishingRepository finishingRepository;

    @Override
    public FinishingResponse createFinishing(FinishingRequest request) {
        FinishingEntity entity = FinishingMapper.toEntity(request);
        FinishingEntity saved = finishingRepository.save(entity);
        return FinishingMapper.toResponseDTO(saved);
    }

    @Override
    public FinishingResponse getFinishingById(Integer id) {
        FinishingEntity entity = finishingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Finishing not found"));
        return FinishingMapper.toResponseDTO(entity);
    }

    @Override
    public List<FinishingResponse> getAllFinishing() {
        return finishingRepository.findAll().stream()
                .map(FinishingMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFinishing(Integer id) {
        finishingRepository.deleteById(id);
    }
}
