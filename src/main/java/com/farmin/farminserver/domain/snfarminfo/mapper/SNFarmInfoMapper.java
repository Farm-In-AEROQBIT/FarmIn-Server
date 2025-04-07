package com.farmin.farminserver.domain.snfarminfo.mapper;

import com.farmin.farminserver.domain.snfarminfo.dto.SNFarmInfoRequest;
import com.farmin.farminserver.domain.snfarminfo.dto.SNFarmInfoResponse;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoEntity;
import org.springframework.stereotype.Component;

@Component
public class SNFarmInfoMapper {

    public SNFarmInfoEntity toEntity(SNFarmInfoRequest request) {
        return SNFarmInfoEntity.builder()
                .farmId(request.getFarmId())
                .build();
    }

    public SNFarmInfoResponse toResponse(SNFarmInfoEntity entity) {
        return SNFarmInfoResponse.builder()
                .snFarmId(entity.getSnFarmId())
                .farmId(entity.getFarmId())
                .build();
    }
}
