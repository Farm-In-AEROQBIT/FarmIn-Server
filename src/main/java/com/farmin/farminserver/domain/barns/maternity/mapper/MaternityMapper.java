package com.farmin.farminserver.domain.barns.maternity.mapper;

import com.farmin.farminserver.domain.barns.maternity.dto.MaternityRequest;
import com.farmin.farminserver.domain.barns.maternity.dto.MaternityResponse;
import com.farmin.farminserver.entity.barns.maternity.MaternityEntity;
import org.springframework.stereotype.Component;

@Component
public class MaternityMapper {

    public MaternityEntity toEntity(MaternityRequest dto) {
        return MaternityEntity.builder()
                .snFarmId(dto.getSnFarmId())
                .build();
    }

    public MaternityResponse toResponseDTO(MaternityEntity entity) {
        MaternityResponse response = new MaternityResponse();
        response.setMaternityId(entity.getMaternityId());
        response.setSnFarmId(entity.getSnFarmId());
        return response;
    }
}
