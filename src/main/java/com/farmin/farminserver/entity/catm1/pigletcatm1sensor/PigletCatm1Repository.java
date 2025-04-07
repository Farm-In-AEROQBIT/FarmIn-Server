package com.farmin.farminserver.entity.catm1.pigletcatm1sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PigletCatm1Repository extends JpaRepository<PigletCatm1Entity, Integer> {

    @Query("SELECT p FROM PigletCatm1Entity p WHERE p.time LIKE CONCAT(:year, '%')")
    List<PigletCatm1Entity> findByYear(@Param("year") String year);

    @Query("SELECT p FROM PigletCatm1Entity p WHERE p.time LIKE CONCAT(:year, '-', :month, '%')")
    List<PigletCatm1Entity> findByYearAndMonth(@Param("year") String year, @Param("month") String month);

    @Query("SELECT p FROM PigletCatm1Entity p WHERE p.time BETWEEN :startDate AND :endDate")
    List<PigletCatm1Entity> findByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT p FROM PigletCatm1Entity p WHERE p.time LIKE CONCAT(:year, '-', :month, '-', :day, '%')")
    List<PigletCatm1Entity> findByYearMonthAndDay(@Param("year") String year, @Param("month") String month, @Param("day") String day);
}
