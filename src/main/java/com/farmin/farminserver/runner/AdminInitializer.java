package com.farmin.farminserver.runner;

import lombok.extern.slf4j.Slf4j;
import com.farmin.farminserver.entity.user.UserEntity;
import com.farmin.farminserver.entity.user.UserRepository;
import com.farmin.farminserver.entity.user.enums.Role;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Order(1)  // 가장 우선 순위로 실행
@Slf4j
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminInitializer(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        try {
            // 기존 관리자 계정이 있는지 확인
            Optional<UserEntity> existingAdmin = userRepository.findByRole(Role.ROLE_ADMIN);

            if (existingAdmin.isPresent()) {
                log.info("✅ 관리자 계정이 이미 존재합니다: {}", existingAdmin.get().getUsername());
                return;
            }

            // 환경 변수에서 관리자 계정 정보 가져오기
            String adminUsername = System.getenv("ADMIN_USERNAME");
            String adminEmail = System.getenv("ADMIN_EMAIL");
            String adminPassword = System.getenv("ADMIN_PASSWORD");
            String adminName = System.getenv("ADMIN_NAME");

            // 필수 환경 변수 검증
            if (adminUsername == null || adminUsername.trim().isEmpty()) {
                log.error("❌ 환경 변수 `ADMIN_USERNAME`이 설정되지 않았습니다.");
                return;
            }
            if (adminEmail == null || adminEmail.trim().isEmpty()) {
                log.error("❌ 환경 변수 `ADMIN_EMAIL`이 설정되지 않았습니다.");
                return;
            }
            if (adminPassword == null || adminPassword.trim().isEmpty()) {
                log.error("❌ 환경 변수 `ADMIN_PASSWORD`가 설정되지 않았습니다.");
                return;
            }
            if (adminName == null || adminName.trim().isEmpty()) {
                log.error("❌ 환경 변수 `ADMIN_NAME`이 설정되지 않았습니다.");
                return;
            }

            // 관리자 계정 생성
            UserEntity admin = UserEntity.builder()
                    .username(adminUsername)
                    .password(bCryptPasswordEncoder.encode(adminPassword))
                    .name(adminName)
                    .email(adminEmail)
                    .role(Role.ROLE_ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("✅ 관리자 계정이 성공적으로 생성되었습니다. (username: {}, email: {})", adminUsername, adminEmail);
        } catch (Exception e) {
            log.error("❌ 관리자 계정 생성 중 오류 발생", e);
        }
    }
}
