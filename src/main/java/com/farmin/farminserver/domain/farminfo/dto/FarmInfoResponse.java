package com.farmin.farminserver.domain.farminfo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FarmInfoResponse {
    private Integer farmId;
    private String farmName;
    private Integer userId;
}
