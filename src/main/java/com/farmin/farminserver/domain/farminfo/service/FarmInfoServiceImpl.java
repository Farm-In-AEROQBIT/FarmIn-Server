package com.farmin.farminserver.domain.farminfo.service;

import com.farmin.farminserver.domain.farminfo.dto.FarmInfoRequest;
import com.farmin.farminserver.domain.farminfo.dto.FarmInfoResponse;
import com.farmin.farminserver.domain.farminfo.mapper.FarmInfoMapper;
import com.farmin.farminserver.entity.farminfo.FarmInRepository;
import com.farmin.farminserver.entity.farminfo.FarmInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmInfoServiceImpl implements FarmInfoService {
    private final FarmInRepository farmInRepository;

    @Override
    public FarmInfoResponse createFarmInfo(FarmInfoRequest request) {
        FarmInfoEntity farmInfoEntity = FarmInfoMapper.toEntity(request);
        farmInRepository.save(farmInfoEntity);
        return FarmInfoMapper.toResponse(farmInfoEntity);
    }

    @Override
    public List<FarmInfoResponse> getAllFarms() {
        return farmInRepository.findAll()
                .stream()
                .map(FarmInfoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FarmInfoResponse getFarmById(int id) {
        return farmInRepository.findById(id)
                .map(FarmInfoMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Farm not found"));
    }
}
