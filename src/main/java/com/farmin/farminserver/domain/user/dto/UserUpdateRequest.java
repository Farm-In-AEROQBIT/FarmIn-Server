package com.farmin.farminserver.domain.user.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String email;
    private String phonenum;
    private String password;
}