package com.farmin.farminserver.domain.barns.finishing.mapper;

import com.farmin.farminserver.domain.barns.finishing.dto.FinishingRequest;
import com.farmin.farminserver.domain.barns.finishing.dto.FinishingResponse;
import com.farmin.farminserver.entity.barns.finishing.FinishingEntity;

public class FinishingMapper {

    public static FinishingEntity toEntity(FinishingRequest dto) {
        return FinishingEntity.builder()
                .snFarmId(dto.getSnFarmId())
                .build();
    }

    public static FinishingResponse toResponseDTO(FinishingEntity entity) {
        FinishingResponse dto = new FinishingResponse();
        dto.setFinishingID(entity.getFinishingId());
        dto.setSnfarmID(entity.getSnFarmId());
        return dto;
    }
}
