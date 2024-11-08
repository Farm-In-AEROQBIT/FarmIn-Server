package com.farmin.farminserver.domain.barns.gestation.mapper;

import com.farmin.farminserver.domain.barns.gestation.dto.GestationRequest;
import com.farmin.farminserver.domain.barns.gestation.dto.GestationResponse;
import com.farmin.farminserver.entity.barns.gestation.GestationEntity;

public class GestationMapper {
    public static GestationEntity toEntity(GestationRequest dto) {
        return GestationEntity.builder()
                .FarmID(dto.getFarmID())
                .build();
    }

    public static GestationResponse toResponseDTO(GestationEntity entity) {
        GestationResponse responseDTO = new GestationResponse();
        responseDTO.setGestationID(entity.getId());
        responseDTO.setFarmID(entity.getFarmID());
        return responseDTO;
    }
}
