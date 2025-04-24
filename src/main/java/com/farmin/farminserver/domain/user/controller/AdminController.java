package com.farmin.farminserver.domain.user.controller;

import com.farmin.farminserver.domain.user.dto.UserUpdateRequest;
import com.farmin.farminserver.domain.user.service.UserService;
import com.farmin.farminserver.entity.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    // 관리자만 사용자 목록 조회 가능
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 관리자만 사용자 삭제 가능
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(
            @PathVariable Integer id,
            @RequestBody UserUpdateRequest updateRequest) {
        UserEntity updatedUser = userService.updateUser(id, updateRequest);

        // 보안을 위해 비밀번호 필드를 null로 설정하여 응답에서 제외
        updatedUser.setPassword(null);

        return ResponseEntity.ok(updatedUser);
    }
}
