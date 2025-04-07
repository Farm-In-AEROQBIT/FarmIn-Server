package com.farmin.farminserver.domain.barns.reserve.mapper;

import com.farmin.farminserver.domain.barns.reserve.dto.ReserveRequest;
import com.farmin.farminserver.domain.barns.reserve.dto.ReserveResponse;
import com.farmin.farminserver.entity.barns.reserve.ReserveEntity;
import org.springframework.stereotype.Component;

@Component
public class ReserveMapper {

    public ReserveEntity toEntity(ReserveRequest dto) {
        return ReserveEntity.builder()
                .snFarmId(dto.getSnFarmId())
                .build();
    }

    public ReserveResponse toResponseDTO(ReserveEntity entity) {
        ReserveResponse response = new ReserveResponse();
        response.setReserveId(entity.getReserveId());
        response.setSnFarmId(entity.getSnFarmId());
        return response;
    }
}
