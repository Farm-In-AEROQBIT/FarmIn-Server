package com.farmin.farminserver.domain.user.service;

import lombok.RequiredArgsConstructor;
import com.farmin.farminserver.common.error.ErrorCode;
import com.farmin.farminserver.common.exception.ApiException;
import com.farmin.farminserver.common.utils.JwtUtils;
import com.farmin.farminserver.domain.user.dto.JoinRequest;
import com.farmin.farminserver.domain.user.dto.LoginRequest;
import com.farmin.farminserver.domain.user.dto.LoginResponse;
import com.farmin.farminserver.domain.user.mapper.UserMapper;
import com.farmin.farminserver.entity.user.UserEntity;
import com.farmin.farminserver.entity.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;
    @Override
    public void join(JoinRequest joinRequest) {
        UserEntity userEntity = userRepository.findByUsername(joinRequest.getUsername());
        if(userEntity != null){
            throw new ApiException(ErrorCode.BAD_REQUEST,"이미 있는 유저입니다.");
        }

        UserEntity newUserEntity = UserMapper.toEntity(joinRequest);
        newUserEntity.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));
        userRepository.save(newUserEntity);

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity userEntity = Optional.ofNullable(userRepository.findByUsername(loginRequest.getUsername()))
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"없는 회원입니다."));
        //바말번호 확인
        boolean passwordMatch = bCryptPasswordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword());
        if (!passwordMatch) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "비밀번호가 틀렸습니다.");
        }
        String accessToken = jwtUtils.generateAccessToken(userEntity);
        String refreshToken = jwtUtils.generateRefreshToken(userEntity);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
