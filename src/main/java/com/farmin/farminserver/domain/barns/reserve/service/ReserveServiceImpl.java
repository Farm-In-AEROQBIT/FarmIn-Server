package com.farmin.farminserver.domain.barns.reserve.service;

import com.farmin.farminserver.domain.barns.reserve.dto.ReserveRequest;
import com.farmin.farminserver.domain.barns.reserve.dto.ReserveResponse;
import com.farmin.farminserver.domain.barns.reserve.mapper.ReserveMapper;
import com.farmin.farminserver.entity.barns.reserve.ReserveEntity;
import com.farmin.farminserver.entity.barns.reserve.ReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService {
    private final ReserveRepository reserveRepository;

    @Override
    public ReserveResponse createReserve(ReserveRequest dto) {
        ReserveEntity entity = ReserveMapper.toEntity(dto);
        ReserveEntity savedEntity = reserveRepository.save(entity);
        return ReserveMapper.toResponseDTO(savedEntity);
    }

    @Override
    public ReserveResponse getReserveById(Integer id) {
        ReserveEntity entity = reserveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserve entity not found"));
        return ReserveMapper.toResponseDTO(entity);
    }
}
