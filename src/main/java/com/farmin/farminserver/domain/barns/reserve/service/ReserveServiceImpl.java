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
    private final ReserveMapper reserveMapper;

    @Override
    public ReserveResponse createReserve(ReserveRequest dto) {
        ReserveEntity entity = reserveMapper.toEntity(dto);
        ReserveEntity saved = reserveRepository.save(entity);
        return reserveMapper.toResponseDTO(saved);
    }

    @Override
    public ReserveResponse getReserveById(Integer id) {
        ReserveEntity entity = reserveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserve entity not found"));
        return reserveMapper.toResponseDTO(entity);
    }
}
