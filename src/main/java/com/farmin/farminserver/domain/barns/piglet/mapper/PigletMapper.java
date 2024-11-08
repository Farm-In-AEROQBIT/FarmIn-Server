package com.farmin.farminserver.domain.barns.piglet.mapper;

import com.farmin.farminserver.domain.barns.piglet.dto.PigletRequest;
import com.farmin.farminserver.domain.barns.piglet.dto.PigletResponse;
import com.farmin.farminserver.entity.barns.piglet.PigletEntity;

public class PigletMapper {
    public static PigletEntity toEntity(PigletRequest dto) {
        return PigletEntity.builder()
                .FarmID(dto.getFarmID())
                .build();
    }

    public static PigletResponse toResponseDTO(PigletEntity entity) {
        PigletResponse responseDTO = new PigletResponse();
        responseDTO.setPigletID(entity.getId());
        responseDTO.setFarmID(entity.getFarmID());
        return responseDTO;
    }
}
