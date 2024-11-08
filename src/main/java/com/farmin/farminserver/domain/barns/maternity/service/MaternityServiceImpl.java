package com.farmin.farminserver.domain.barns.maternity.service;

import com.farmin.farminserver.domain.barns.maternity.dto.MaternityRequest;
import com.farmin.farminserver.domain.barns.maternity.dto.MaternityResponse;
import com.farmin.farminserver.domain.barns.maternity.mapper.MaternityMapper;
import com.farmin.farminserver.entity.barns.maternity.MaternityEntity;
import com.farmin.farminserver.entity.barns.maternity.MaternityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaternityServiceImpl implements MaternityService {
    private final MaternityRepository maternityRepository;

    @Override
    public MaternityResponse createMaternity(MaternityRequest dto) {
        MaternityEntity entity = MaternityMapper.toEntity(dto);
        MaternityEntity savedEntity = maternityRepository.save(entity);
        return MaternityMapper.toResponseDTO(savedEntity);
    }

    @Override
    public MaternityResponse getMaternityById(Integer id) {
        MaternityEntity entity = maternityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maternity entity not found"));
        return MaternityMapper.toResponseDTO(entity);
    }
}
