package com.farmin.farminserver.entity.snfarminfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SNFarmInfoRepository extends JpaRepository<SNFarmInfoEntity, String> {
    Optional<SNFarmInfoEntity> findByFarmId(int farmId);
    Optional<SNFarmInfoEntity> findBySnFarmId(String snFarmId);
    List<SNFarmInfoEntity> findByFarmId(Integer farmId);
}
