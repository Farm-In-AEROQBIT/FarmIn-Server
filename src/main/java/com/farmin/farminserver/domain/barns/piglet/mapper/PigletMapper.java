package com.farmin.farminserver.domain.barns.piglet.mapper;

import com.farmin.farminserver.domain.barns.piglet.dto.PigletRequest;
import com.farmin.farminserver.domain.barns.piglet.dto.PigletResponse;
import com.farmin.farminserver.entity.barns.piglet.PigletEntity;
import org.springframework.stereotype.Component;

@Component
public class PigletMapper {

    public PigletEntity toEntity(PigletRequest dto) {
        return PigletEntity.builder()
                .snFarmId(dto.getSnFarmId())
                .build();
    }

    public PigletResponse toResponseDTO(PigletEntity entity) {
        PigletResponse response = new PigletResponse();
        response.setPigletId(entity.getPigletId());
        response.setSnFarmId(entity.getSnFarmId());
        return response;
    }
}
