package com.farmin.farminserver.domain.barns.gestation.mapper;

import com.farmin.farminserver.domain.barns.gestation.dto.GestationRequest;
import com.farmin.farminserver.domain.barns.gestation.dto.GestationResponse;
import com.farmin.farminserver.entity.barns.gestation.GestationEntity;
import org.springframework.stereotype.Component;

@Component
public class GestationMapper {

    public GestationEntity toEntity(GestationRequest dto) {
        return GestationEntity.builder()
                .snFarmId(dto.getSnFarmId())
                .build();
    }

    public GestationResponse toResponseDTO(GestationEntity entity) {
        GestationResponse dto = new GestationResponse();
        dto.setGestationId(entity.getGestationId());
        dto.setSnFarmId(entity.getSnFarmId());
        return dto;
    }
}
