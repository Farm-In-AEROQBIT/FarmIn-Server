package com.farmin.farminserver.entity.barns.maternity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaternityRepository extends JpaRepository<MaternityEntity, Integer> {
    Optional<MaternityEntity> findById(Integer id);
    Optional<MaternityEntity> findBySnFarmId(String snFarmId);

}
