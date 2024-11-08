package com.farmin.farminserver.domain.farminfo.mapper;

import com.farmin.farminserver.domain.farminfo.dto.FarmInfoRequest;
import com.farmin.farminserver.domain.farminfo.dto.FarmInfoResponse;
import com.farmin.farminserver.entity.farminfo.FarmInfoEntity;

public class FarmInfoMapper {

    public static FarmInfoEntity toEntity(FarmInfoRequest request) {
        return FarmInfoEntity.builder()
                .FarmName(request.getFarmName())
                .UserID(request.getUserId())
                .build();
    }

    public static FarmInfoResponse toResponse(FarmInfoEntity entity) {
        return FarmInfoResponse.builder()
                .farmId(String.valueOf(entity.getId()))
                .farmName(entity.getFarmName())
                .userId(entity.getUserID())
                .build();
    }
}
