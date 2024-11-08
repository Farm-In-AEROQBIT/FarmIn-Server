package com.farmin.farminserver.domain.barns.finishing.mapper;

import com.farmin.farminserver.domain.barns.finishing.dto.FinishingRequest;
import com.farmin.farminserver.domain.barns.finishing.dto.FinishingResponse;
import com.farmin.farminserver.entity.barns.finishing.FinishingEntity;

public class FinishingMapper {
    public static FinishingEntity toEntity(FinishingRequest dto) {
        return FinishingEntity.builder()
                .FarmID(dto.getFarmID())
                .build();
    }

    public static FinishingResponse toResponseDTO(FinishingEntity entity) {
        FinishingResponse responseDTO = new FinishingResponse();
        responseDTO.setFinishingID(entity.getId());
        responseDTO.setFarmID(entity.getFarmID());
        return responseDTO;
    }
}
