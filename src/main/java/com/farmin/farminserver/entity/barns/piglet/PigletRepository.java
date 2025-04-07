package com.farmin.farminserver.entity.barns.piglet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PigletRepository extends JpaRepository<PigletEntity, Integer> {
    Optional<PigletEntity> findById(Integer id);
    Optional<PigletEntity> findBySnFarmId(String snFarmId);

}
