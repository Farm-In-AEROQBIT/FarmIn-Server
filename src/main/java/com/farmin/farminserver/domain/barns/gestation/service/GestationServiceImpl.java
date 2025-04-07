package com.farmin.farminserver.domain.barns.gestation.service;

import com.farmin.farminserver.domain.barns.gestation.dto.GestationRequest;
import com.farmin.farminserver.domain.barns.gestation.dto.GestationResponse;
import com.farmin.farminserver.domain.barns.gestation.mapper.GestationMapper;
import com.farmin.farminserver.entity.barns.gestation.GestationEntity;
import com.farmin.farminserver.entity.barns.gestation.GestationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GestationServiceImpl implements GestationService {

    private final GestationRepository repository;
    private final GestationMapper mapper;

    @Override
    public GestationResponse createGestation(GestationRequest dto) {
        GestationEntity entity = mapper.toEntity(dto);
        GestationEntity saved = repository.save(entity);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public GestationResponse getGestationById(Integer id) {
        GestationEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gestation not found"));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public List<GestationResponse> getAllGestation() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGestationById(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("해당 ID의 Gestation 데이터가 없습니다.");
        }
        repository.deleteById(id);
    }
}
