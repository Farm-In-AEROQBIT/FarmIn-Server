package com.farmin.farminserver.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.user.dto.JoinRequest;
import com.farmin.farminserver.domain.user.dto.LoginRequest;
import com.farmin.farminserver.domain.user.dto.LoginResponse;
import com.farmin.farminserver.domain.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/user")
public class UserOpenApiController {
    private final UserService userService;

    @PostMapping("/join")
    public Api Join(
            @Valid
            @RequestBody JoinRequest joinRequest

    ){
        userService.join(joinRequest);
        return Api.CREATE();
    }

    @PostMapping("/login")
    public Api<LoginResponse> login(
            @Valid
            @RequestBody LoginRequest loginRequest
    ){
        LoginResponse loginResponse = userService.login(loginRequest);
        return Api.OK(loginResponse);
    }
}
