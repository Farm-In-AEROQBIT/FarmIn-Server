package com.farmin.farminserver.entity.barns.growing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrowingRepository extends JpaRepository<GrowingEntity, Integer> {
    Optional<GrowingEntity> findById(Integer id);
    Optional<GrowingEntity> findBySnFarmId(String snFarmId);

}
