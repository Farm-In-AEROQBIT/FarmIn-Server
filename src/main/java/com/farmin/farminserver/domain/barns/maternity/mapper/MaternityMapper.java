package com.farmin.farminserver.domain.barns.maternity.mapper;

import com.farmin.farminserver.domain.barns.maternity.dto.MaternityRequest;
import com.farmin.farminserver.domain.barns.maternity.dto.MaternityResponse;
import com.farmin.farminserver.entity.barns.maternity.MaternityEntity;

public class MaternityMapper {
    public static MaternityEntity toEntity(MaternityRequest dto) {
        return MaternityEntity.builder()
                .FarmID(dto.getFarmID())
                .build();
    }

    public static MaternityResponse toResponseDTO(MaternityEntity entity) {
        MaternityResponse responseDTO = new MaternityResponse();
        responseDTO.setMaternityID(entity.getId());
        responseDTO.setFarmID(entity.getFarmID());
        return responseDTO;
    }
}
