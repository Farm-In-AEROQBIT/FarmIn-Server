package com.farmin.farminserver.domain.farminfo.mapper;

import com.farmin.farminserver.domain.farminfo.dto.FarmInfoRequest;
import com.farmin.farminserver.domain.farminfo.dto.FarmInfoResponse;
import com.farmin.farminserver.entity.farminfo.FarmInfoEntity;

public class FarmInfoMapper {

    public static FarmInfoEntity toEntity(FarmInfoRequest request) {
        return FarmInfoEntity.builder()
                .farmName(request.getFarmName())
                .userId(request.getUserId())
                .build();
    }

    public static FarmInfoResponse toResponse(FarmInfoEntity entity) {
        return FarmInfoResponse.builder()
                .farmId(entity.getFarmId())
                .farmName(entity.getFarmName())
                .userId(entity.getUserId())
                .build();
    }
}
