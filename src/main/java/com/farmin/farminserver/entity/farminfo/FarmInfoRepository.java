package com.farmin.farminserver.entity.farminfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmInfoRepository extends JpaRepository<FarmInfoEntity,Integer>{
    List<FarmInfoEntity> findByUserId(Integer userId);
}
