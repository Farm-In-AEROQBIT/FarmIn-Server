package com.farmin.farminserver.domain.barns.maternity.service;

import com.farmin.farminserver.domain.barns.maternity.dto.MaternityRequest;
import com.farmin.farminserver.domain.barns.maternity.dto.MaternityResponse;
import com.farmin.farminserver.domain.barns.maternity.mapper.MaternityMapper;
import com.farmin.farminserver.entity.barns.maternity.MaternityEntity;
import com.farmin.farminserver.entity.barns.maternity.MaternityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaternityServiceImpl implements MaternityService {

    private final MaternityRepository repository;
    private final MaternityMapper mapper;

    @Override
    public MaternityResponse createMaternity(MaternityRequest dto) {
        MaternityEntity saved = repository.save(mapper.toEntity(dto));
        return mapper.toResponseDTO(saved);
    }

    @Override
    public MaternityResponse getMaternityById(Integer id) {
        MaternityEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 Maternity가 없습니다."));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public List<MaternityResponse> getAllMaternities() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMaternityById(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("해당 ID의 Maternity가 존재하지 않습니다.");
        }
        repository.deleteById(id);
    }
}
