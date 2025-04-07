package com.farmin.farminserver.entity.barns.boars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoarsRepository extends JpaRepository<BoarsEntity, Integer> {
    Optional<BoarsEntity> findById(Integer id);
    Optional<BoarsEntity> findBySnFarmId(String snFarmId);

}

