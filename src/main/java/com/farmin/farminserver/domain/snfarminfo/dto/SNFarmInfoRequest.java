package com.farmin.farminserver.domain.snfarminfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SNFarmInfoRequest {
    private int farmId;
    private String snFarmId;
    private String description;
    private List<String> barnTypes;
}