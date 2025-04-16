package com.farmin.farminserver.domain.farminfo.service;

import com.farmin.farminserver.common.error.ErrorCode;
import com.farmin.farminserver.common.exception.ApiException;
import com.farmin.farminserver.domain.farminfo.dto.FarmInfoRequest;
import com.farmin.farminserver.domain.farminfo.dto.FarmInfoResponse;
import com.farmin.farminserver.domain.farminfo.mapper.FarmInfoMapper;
import com.farmin.farminserver.entity.farminfo.FarmInfoRepository;
import com.farmin.farminserver.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmInfoServiceImpl implements FarmInfoService {

    private final FarmInfoRepository farmInfoRepository;
    private final UserRepository userRepository;

    @Override
    public FarmInfoResponse createFarmInfo(FarmInfoRequest request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "존재하지 않는 유저입니다.");
        }

        var entity = FarmInfoMapper.toEntity(request);
        var saved = farmInfoRepository.save(entity);
        return FarmInfoMapper.toResponse(saved);
    }

    @Override
    public List<FarmInfoResponse> getAllFarms() {
        return farmInfoRepository.findAll().stream()
                .map(FarmInfoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FarmInfoResponse getFarmById(int id) {
        return farmInfoRepository.findById(id)
                .map(FarmInfoMapper::toResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 농장을 찾을 수 없습니다."));
    }

    @Override
    public FarmInfoResponse updateFarmInfo(int id, FarmInfoRequest request) {
        var entity = farmInfoRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 농장을 찾을 수 없습니다."));

        if (!userRepository.existsById(request.getUserId())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "존재하지 않는 유저입니다.");
        }

        entity.setFarmName(request.getFarmName());
        entity.setUserId(request.getUserId());
        return FarmInfoMapper.toResponse(farmInfoRepository.save(entity));
    }

    @Override
    public void deleteFarmInfo(int id) {
        if (!farmInfoRepository.existsById(id)) {
            throw new ApiException(ErrorCode.NOT_FOUND, "해당 농장을 찾을 수 없습니다.");
        }
        farmInfoRepository.deleteById(id);
    }
}

