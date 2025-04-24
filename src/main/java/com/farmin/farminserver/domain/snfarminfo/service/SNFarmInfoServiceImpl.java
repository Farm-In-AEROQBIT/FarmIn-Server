package com.farmin.farminserver.domain.snfarminfo.service;

import com.farmin.farminserver.domain.snfarminfo.dto.SNFarmInfoRequest;
import com.farmin.farminserver.domain.snfarminfo.dto.SNFarmInfoResponse;
import com.farmin.farminserver.domain.snfarminfo.mapper.SNFarmInfoMapper;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoEntity;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SNFarmInfoServiceImpl implements SNFarmInfoService {

    private final SNFarmInfoRepository snFarmInfoRepository;
    private final SNFarmInfoMapper snFarmInfoMapper;

    @Override
    public SNFarmInfoResponse createSNFarmInfo(SNFarmInfoRequest request) {
        if (request.getSnFarmId() == null || request.getSnFarmId().isEmpty()) {
            throw new IllegalArgumentException("SNFarmID is required and cannot be empty");
        }

        // farmId 검증 추가 (필요시)
        if (request.getFarmId() <= 0) {
            throw new IllegalArgumentException("Invalid Farm ID");
        }

        SNFarmInfoEntity entity = snFarmInfoMapper.toEntity(request);
        SNFarmInfoEntity savedEntity = snFarmInfoRepository.save(entity);

        return snFarmInfoMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public SNFarmInfoResponse getSNFarmInfoByFarmId(int farmId) {
        SNFarmInfoEntity entity = snFarmInfoRepository.findByFarmId(farmId)
                .orElseThrow(() -> new RuntimeException("Farm info not found with ID: " + farmId));
        return snFarmInfoMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SNFarmInfoResponse> getAllSNFarmInfos() {
        List<SNFarmInfoEntity> entities = snFarmInfoRepository.findAll();
        return entities.stream()
                .map(snFarmInfoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SNFarmInfoResponse updateSNFarmInfo(String snFarmId, SNFarmInfoRequest request) {
        SNFarmInfoEntity entity = snFarmInfoRepository.findBySnFarmId(snFarmId)
                .orElseThrow(() -> new RuntimeException("Farm info not found with ID: " + snFarmId));

        entity.setFarmId(request.getFarmId());
        SNFarmInfoEntity updatedEntity = snFarmInfoRepository.save(entity);
        return snFarmInfoMapper.toResponse(updatedEntity);
    }

    @Override
    public void deleteSNFarmInfo(String snFarmId) {
        SNFarmInfoEntity entity = snFarmInfoRepository.findBySnFarmId(snFarmId)
                .orElseThrow(() -> new RuntimeException("Farm info not found with ID: " + snFarmId));
        snFarmInfoRepository.delete(entity);
    }
}