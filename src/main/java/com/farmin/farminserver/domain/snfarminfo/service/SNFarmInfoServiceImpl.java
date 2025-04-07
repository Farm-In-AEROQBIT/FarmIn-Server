package com.farmin.farminserver.domain.snfarminfo.service;

import com.farmin.farminserver.domain.snfarminfo.dto.SNFarmInfoRequest;
import com.farmin.farminserver.domain.snfarminfo.dto.SNFarmInfoResponse;
import com.farmin.farminserver.domain.snfarminfo.mapper.SNFarmInfoMapper;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoEntity;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SNFarmInfoServiceImpl implements SNFarmInfoService {

    private final SNFarmInfoRepository snFarmInfoRepository;
    private final SNFarmInfoMapper snFarmInfoMapper;

    @Override
    public SNFarmInfoResponse createSNFarmInfo(SNFarmInfoRequest request) {
        SNFarmInfoEntity entity = snFarmInfoMapper.toEntity(request);
        SNFarmInfoEntity savedEntity = snFarmInfoRepository.save(entity);
        return snFarmInfoMapper.toResponse(savedEntity);
    }

    @Override
    public SNFarmInfoResponse getSNFarmInfoByFarmId(int farmId) {
        SNFarmInfoEntity entity = snFarmInfoRepository.findByFarmId(farmId)
                .orElseThrow(() -> new IllegalArgumentException("Farm ID not found: " + farmId));
        return snFarmInfoMapper.toResponse(entity);
    }

    @Override
    public List<SNFarmInfoResponse> getAllSNFarmInfos() {
        return snFarmInfoRepository.findAll().stream()
                .map(snFarmInfoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SNFarmInfoResponse updateSNFarmInfo(String snFarmId, SNFarmInfoRequest request) {
        SNFarmInfoEntity entity = snFarmInfoRepository.findById(snFarmId)
                .orElseThrow(() -> new IllegalArgumentException("SNFarmInfo not found: " + snFarmId));

        entity.setFarmId(request.getFarmId());
        SNFarmInfoEntity updatedEntity = snFarmInfoRepository.save(entity);
        return snFarmInfoMapper.toResponse(updatedEntity);
    }

    @Override
    public void deleteSNFarmInfo(String snFarmId) {
        if (!snFarmInfoRepository.existsById(snFarmId)) {
            throw new IllegalArgumentException("SNFarmInfo not found: " + snFarmId);
        }
        snFarmInfoRepository.deleteById(snFarmId);
    }
}
