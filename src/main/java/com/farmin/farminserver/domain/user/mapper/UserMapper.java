package com.farmin.farminserver.domain.user.mapper;

import com.farmin.farminserver.common.error.ErrorCode;
import com.farmin.farminserver.common.exception.ApiException;
import com.farmin.farminserver.domain.user.dto.JoinRequest;
import com.farmin.farminserver.entity.user.UserEntity;
import com.farmin.farminserver.entity.user.enums.Role;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserMapper {
    public static UserEntity toEntity(JoinRequest joinRequest){
        return Optional.ofNullable(joinRequest).map(
                it->{
                    return UserEntity.builder()
                            .username(joinRequest.getUsername())
                            .name(joinRequest.getName())
                            .password(joinRequest.getPassword())
                            .email(joinRequest.getEmail())
                            .role(Role.ROLE_USER)
                            .createdAt(LocalDateTime.now())
                            .build();
                }
        ).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }
}
