package com.farmin.farminserver.entity.user;

import com.farmin.farminserver.entity.user.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    UserEntity findByUsername(String username);

    Optional<UserEntity> findByRole(Role role);
}
