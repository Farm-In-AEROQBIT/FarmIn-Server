package com.farmin.farminserver.domain.user.service;

import com.farmin.farminserver.domain.user.dto.UserUpdateRequest;
import com.farmin.farminserver.entity.barns.boars.BoarsRepository;
import com.farmin.farminserver.entity.barns.finishing.FinishingRepository;
import com.farmin.farminserver.entity.barns.gestation.GestationRepository;
import com.farmin.farminserver.entity.barns.growing.GrowingRepository;
import com.farmin.farminserver.entity.barns.maternity.MaternityRepository;
import com.farmin.farminserver.entity.barns.piglet.PigletRepository;
import com.farmin.farminserver.entity.barns.reserve.ReserveRepository;
import com.farmin.farminserver.entity.user.enums.Role;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;

    private final FinishingRepository finishingRepository;
    private final GrowingRepository growingRepository;
    private final MaternityRepository maternityRepository;
    private final GestationRepository gestationRepository;
    private final PigletRepository pigletRepository;
    private final BoarsRepository boarsRepository;
    private final ReserveRepository reserveRepository;

    @Override
    public void join(JoinRequest joinRequest) {
        UserEntity userEntity = userRepository.findByUsername(joinRequest.getUsername());
        if (userEntity != null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 있는 유저입니다.");
        }

        UserEntity newUserEntity = UserMapper.toEntity(joinRequest);
        newUserEntity.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));
        userRepository.save(newUserEntity);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity userEntity = Optional.ofNullable(userRepository.findByUsername(loginRequest.getUsername()))
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "없는 회원입니다."));

        boolean passwordMatch = bCryptPasswordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword());
        if (!passwordMatch) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "비밀번호가 틀렸습니다.");
        }

        String accessToken = jwtUtils.generateAccessToken(userEntity);
        String refreshToken = jwtUtils.generateRefreshToken(userEntity);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isAdmin(userEntity.getRole() == Role.ROLE_ADMIN)
                .build();
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity updateUser(Integer userId, UserUpdateRequest updateRequest) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        // 요청에 포함된 필드만 업데이트
        if (updateRequest.getName() != null && !updateRequest.getName().isEmpty()) {
            userEntity.setName(updateRequest.getName());
        }

        if (updateRequest.getEmail() != null && !updateRequest.getEmail().isEmpty()) {
            userEntity.setEmail(updateRequest.getEmail());
        }

        if (updateRequest.getPhonenum() != null && !updateRequest.getPhonenum().isEmpty()) {
            userEntity.setPhonenum(updateRequest.getPhonenum());
        }

        // 비밀번호가 제공된 경우에만 업데이트
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            // 비밀번호 암호화 처리
            String encodedPassword = bCryptPasswordEncoder.encode(updateRequest.getPassword());
            userEntity.setPassword(encodedPassword);
        }

        // 업데이트 시간 자동 갱신 (UpdateTimestamp 어노테이션이 있으므로 자동 처리)
        return userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ApiException(ErrorCode.NOT_FOUND, "해당 유저를 찾을 수 없습니다.");
        }
        userRepository.deleteById(userId);
    }
}
