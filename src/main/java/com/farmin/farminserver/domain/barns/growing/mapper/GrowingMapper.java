package com.farmin.farminserver.domain.barns.growing.mapper;

import com.farmin.farminserver.domain.barns.growing.dto.GrowingRequest;
import com.farmin.farminserver.domain.barns.growing.dto.GrowingResponse;
import com.farmin.farminserver.entity.barns.growing.GrowingEntity;
import org.springframework.stereotype.Component;

@Component
public class GrowingMapper {

    public GrowingEntity toEntity(GrowingRequest dto) {
        return GrowingEntity.builder()
                .snFarmId(dto.getSnFarmId())
                .build();
    }

    public GrowingResponse toResponseDTO(GrowingEntity entity) {
        GrowingResponse dto = new GrowingResponse();
        dto.setGrowingId(entity.getGrowingId());
        dto.setSnFarmId(entity.getSnFarmId());
        return dto;
    }
}
