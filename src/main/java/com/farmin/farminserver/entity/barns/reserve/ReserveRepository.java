package com.farmin.farminserver.entity.barns.reserve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReserveRepository extends JpaRepository<ReserveEntity, Integer> {
    Optional<ReserveEntity> findById(Integer id);
    Optional<ReserveEntity> findBySnFarmId(String snFarmId);

}
