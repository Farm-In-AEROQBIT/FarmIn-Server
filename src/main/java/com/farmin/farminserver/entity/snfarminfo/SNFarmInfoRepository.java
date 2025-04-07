package com.farmin.farminserver.entity.snfarminfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SNFarmInfoRepository extends JpaRepository<SNFarmInfoEntity, String> {
    Optional<SNFarmInfoEntity> findByFarmId(int farmId);
    Optional<SNFarmInfoEntity> findBySnFarmId(String snFarmId);
}