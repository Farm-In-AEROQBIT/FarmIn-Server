package com.farmin.farminserver.domain.barns.growing.mapper;

import com.farmin.farminserver.domain.barns.growing.dto.GrowingRequest;
import com.farmin.farminserver.domain.barns.growing.dto.GrowingResponse;
import com.farmin.farminserver.entity.barns.growing.GrowingEntity;

public class GrowingMapper {
    public static GrowingEntity toEntity(GrowingRequest dto) {
        return GrowingEntity.builder()
                .FarmID(dto.getFarmID())
                .build();
    }

    public static GrowingResponse toResponseDTO(GrowingEntity entity) {
        GrowingResponse responseDTO = new GrowingResponse();
        responseDTO.setGrowingID(entity.getId());
        responseDTO.setFarmID(entity.getFarmID());
        return responseDTO;
    }
}
