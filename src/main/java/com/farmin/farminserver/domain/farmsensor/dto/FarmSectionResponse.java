package com.farmin.farminserver.domain.farmsensor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FarmSectionResponse {
    private Integer id;
    private String name;
    private String type;
    private String snFarmId;
}