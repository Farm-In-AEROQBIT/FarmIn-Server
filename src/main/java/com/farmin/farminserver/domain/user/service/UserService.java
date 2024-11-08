package com.farmin.farminserver.domain.user.service;

import com.farmin.farminserver.domain.user.dto.JoinRequest;
import com.farmin.farminserver.domain.user.dto.LoginRequest;
import com.farmin.farminserver.domain.user.dto.LoginResponse;

public interface UserService {
    void join(JoinRequest joinRequest);
    LoginResponse login(LoginRequest loginRequest);
}
