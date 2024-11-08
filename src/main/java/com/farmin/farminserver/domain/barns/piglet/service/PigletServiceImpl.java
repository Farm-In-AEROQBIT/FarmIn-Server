package com.farmin.farminserver.domain.barns.piglet.service;

import com.farmin.farminserver.domain.barns.piglet.dto.PigletRequest;
import com.farmin.farminserver.domain.barns.piglet.dto.PigletResponse;
import com.farmin.farminserver.domain.barns.piglet.mapper.PigletMapper;
import com.farmin.farminserver.entity.barns.piglet.PigletEntity;
import com.farmin.farminserver.entity.barns.piglet.PigletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PigletServiceImpl implements PigletService {
    private final PigletRepository pigletRepository;

    @Override
    public PigletResponse createPiglet(PigletRequest dto) {
        PigletEntity entity = PigletMapper.toEntity(dto);
        PigletEntity savedEntity = pigletRepository.save(entity);
        return PigletMapper.toResponseDTO(savedEntity);
    }

    @Override
    public PigletResponse getPigletById(Integer id) {
        PigletEntity entity = pigletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Piglet entity not found"));
        return PigletMapper.toResponseDTO(entity);
    }
}
