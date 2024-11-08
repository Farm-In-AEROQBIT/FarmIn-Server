package com.farmin.farminserver.domain.farminfo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FarmInfoRequest {
    @NotBlank
    private String farmName;
    @NotBlank
    private String userId;
}
