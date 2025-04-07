package com.farmin.farminserver.domain.barns.boars.service;

import com.farmin.farminserver.domain.barns.boars.dto.BoarsRequest;
import com.farmin.farminserver.domain.barns.boars.dto.BoarsResponse;
import com.farmin.farminserver.domain.barns.boars.mapper.BoarsMapper;
import com.farmin.farminserver.entity.barns.boars.BoarsEntity;
import com.farmin.farminserver.entity.barns.boars.BoarsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoarsServiceImpl implements BoarsService {
    private final BoarsRepository boarsRepository;
    private final BoarsMapper boarsMapper;

    @Override
    public BoarsResponse createBoars(BoarsRequest dto) {
        BoarsEntity entity = boarsMapper.toEntity(dto);
        BoarsEntity savedEntity = boarsRepository.save(entity);
        return boarsMapper.toResponseDTO(savedEntity);
    }

    @Override
    public BoarsResponse getBoarsById(Integer id) {
        BoarsEntity entity = boarsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boars entity not found"));
        return boarsMapper.toResponseDTO(entity);
    }

    // 전체 조회 구현
    @Override
    public List<BoarsResponse> getAllBoars() {
        return boarsRepository.findAll()
                .stream()
                .map(BoarsMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 삭제 구현
    @Override
    public void deleteBoarsById(Integer id) {
        if (!boarsRepository.existsById(id)) {
            throw new RuntimeException("해당 ID의 Boars 데이터가 없습니다.");
        }
        boarsRepository.deleteById(id);
    }
}
