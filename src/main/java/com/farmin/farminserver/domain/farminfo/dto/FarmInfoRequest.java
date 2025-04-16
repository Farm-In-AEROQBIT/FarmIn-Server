package com.farmin.farminserver.domain.farminfo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmInfoRequest {
    @JsonProperty("farmName")
    @NotBlank
    private String farmName;

    @JsonProperty("userId")
    @NotNull
    private Integer userId;
}