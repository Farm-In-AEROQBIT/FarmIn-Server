package com.farmin.farminserver.domain.barns.boars.mapper;

import com.farmin.farminserver.domain.barns.boars.dto.BoarsRequest;
import com.farmin.farminserver.domain.barns.boars.dto.BoarsResponse;
import com.farmin.farminserver.entity.barns.boars.BoarsEntity;
import org.springframework.stereotype.Component;

@Component
public class BoarsMapper {
    public static BoarsEntity toEntity(BoarsRequest dto) {
        return BoarsEntity.builder()
                .snFarmId(dto.getSnFarmId())
                .build();
    }

    public static BoarsResponse toResponseDTO(BoarsEntity entity) {
        BoarsResponse response = new BoarsResponse();
        response.setBoarsId(entity.getBoarsId());
        response.setSnfarmID(entity.getSnFarmId());
        return response;
    }
}
