package com.farmin.farminserver.domain.snfarminfo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SNFarmInfoRequest {
    private int farmId;
}
