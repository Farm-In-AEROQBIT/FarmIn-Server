package com.farmin.farminserver.domain.barns.boars.mapper;

import com.farmin.farminserver.domain.barns.boars.dto.BoarsRequest;
import com.farmin.farminserver.domain.barns.boars.dto.BoarsResponse;
import com.farmin.farminserver.entity.barns.boars.BoarsEntity;

public class BoarsMapper {
    public static BoarsEntity toEntity(BoarsRequest dto) {
        return BoarsEntity.builder()
                .FarmID(dto.getFarmID())
                .build();
    }

    public static BoarsResponse toResponseDTO(BoarsEntity entity) {
        BoarsResponse responseDTO = new BoarsResponse();
        responseDTO.setBoarsID(entity.getId());
        responseDTO.setFarmID(entity.getFarmID());
        return responseDTO;
    }
}
