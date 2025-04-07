package com.farmin.farminserver.domain.barns.growing.service;

import com.farmin.farminserver.domain.barns.growing.dto.GrowingRequest;
import com.farmin.farminserver.domain.barns.growing.dto.GrowingResponse;
import com.farmin.farminserver.domain.barns.growing.mapper.GrowingMapper;
import com.farmin.farminserver.entity.barns.growing.GrowingEntity;
import com.farmin.farminserver.entity.barns.growing.GrowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrowingServiceImpl implements GrowingService {

    private final GrowingRepository growingRepository;
    private final GrowingMapper growingMapper;

    @Override
    public GrowingResponse createGrowing(GrowingRequest dto) {
        GrowingEntity entity = growingMapper.toEntity(dto);
        GrowingEntity savedEntity = growingRepository.save(entity);
        return growingMapper.toResponseDTO(savedEntity);
    }

    @Override
    public GrowingResponse getGrowingById(Integer id) {
        GrowingEntity entity = growingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Growing entity not found"));
        return growingMapper.toResponseDTO(entity);
    }

    @Override
    public List<GrowingResponse> getAllGrowing() {
        return growingRepository.findAll().stream()
                .map(growingMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGrowingById(Integer id) {
        if (!growingRepository.existsById(id)) {
            throw new RuntimeException("해당 ID의 Growing 데이터가 없습니다.");
        }
        growingRepository.deleteById(id);
    }
}
