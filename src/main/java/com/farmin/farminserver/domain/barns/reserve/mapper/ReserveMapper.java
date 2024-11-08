package com.farmin.farminserver.domain.barns.reserve.mapper;

import com.farmin.farminserver.domain.barns.reserve.dto.ReserveRequest;
import com.farmin.farminserver.domain.barns.reserve.dto.ReserveResponse;
import com.farmin.farminserver.entity.barns.reserve.ReserveEntity;

public class ReserveMapper {
    public static ReserveEntity toEntity(ReserveRequest dto) {
        return ReserveEntity.builder()
                .FarmID(dto.getFarmID())
                .build();
    }

    public static ReserveResponse toResponseDTO(ReserveEntity entity) {
        ReserveResponse responseDTO = new ReserveResponse();
        responseDTO.setReserveID(entity.getId());
        responseDTO.setFarmID(entity.getFarmID());
        return responseDTO;
    }
}
