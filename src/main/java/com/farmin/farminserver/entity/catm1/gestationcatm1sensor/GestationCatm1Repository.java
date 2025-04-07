package com.farmin.farminserver.entity.catm1.gestationcatm1sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GestationCatm1Repository extends JpaRepository<GestationCatm1Entity, Integer> {

    @Query("SELECT g FROM GestationCatm1Entity g WHERE g.time LIKE CONCAT(:year, '%')")
    List<GestationCatm1Entity> findByYear(@Param("year") String year);

    @Query("SELECT g FROM GestationCatm1Entity g WHERE g.time LIKE CONCAT(:year, '-', :month, '%')")
    List<GestationCatm1Entity> findByYearAndMonth(@Param("year") String year, @Param("month") String month);

    @Query("SELECT g FROM GestationCatm1Entity g WHERE g.time BETWEEN :startDate AND :endDate")
    List<GestationCatm1Entity> findByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT g FROM GestationCatm1Entity g WHERE g.time LIKE CONCAT(:year, '-', :month, '-', :day, '%')")
    List<GestationCatm1Entity> findByYearMonthAndDay(@Param("year") String year, @Param("month") String month, @Param("day") String day);
}

