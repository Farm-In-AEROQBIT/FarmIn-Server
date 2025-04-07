package com.farmin.farminserver.entity.barns.gestation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GestationRepository extends JpaRepository<GestationEntity, Integer> {
    Optional<GestationEntity> findById(Integer id);
    Optional<GestationEntity> findBySnFarmId(String snFarmId);

}
