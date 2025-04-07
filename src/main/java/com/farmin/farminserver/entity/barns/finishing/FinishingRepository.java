package com.farmin.farminserver.entity.barns.finishing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinishingRepository extends JpaRepository<FinishingEntity, Integer> {
    Optional<FinishingEntity> findById(Integer id);
    Optional<FinishingEntity> findBySnFarmId(String snFarmId);

}
