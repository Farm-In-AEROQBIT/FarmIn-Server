package com.farmin.farminserver.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JoinRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotBlank
    @Email(message = "이메일을 양식을 지켜주세요.")
    private String email;
}
