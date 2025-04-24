package com.farmin.farminserver.domain.user.service;

import com.farmin.farminserver.domain.user.dto.JoinRequest;
import com.farmin.farminserver.domain.user.dto.LoginRequest;
import com.farmin.farminserver.domain.user.dto.LoginResponse;
import com.farmin.farminserver.domain.user.dto.UserUpdateRequest;
import com.farmin.farminserver.entity.user.UserEntity;

import java.util.List;

public interface UserService {
    void join(JoinRequest joinRequest);
    LoginResponse login(LoginRequest loginRequest);
    List<UserEntity> getAllUsers();
    void deleteUser(Integer userId);
    UserEntity updateUser(Integer userId, UserUpdateRequest updateRequest);
    
}
