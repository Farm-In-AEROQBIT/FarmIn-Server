package com.farmin.farminserver.domain.user.service;

import com.farmin.farminserver.domain.user.dto.JoinRequest;
import com.farmin.farminserver.domain.user.dto.LoginRequest;
import com.farmin.farminserver.domain.user.dto.LoginResponse;
import com.farmin.farminserver.entity.user.UserEntity;

import java.util.List;

public interface UserService {
    void join(JoinRequest joinRequest);
    LoginResponse login(LoginRequest loginRequest);
    //관리자 기능 추가
    List<UserEntity> getAllUsers(); // 사용자 목록 조회
    void deleteUser(Integer userId); // 사용자 삭제
}
