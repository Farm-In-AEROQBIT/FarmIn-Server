package com.farmin.farminserver.entity.catm1.growingcatm1sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GrowingCatm1Repository extends JpaRepository<GrowingCatm1Entity, Integer> {

    @Query("SELECT g FROM GrowingCatm1Entity g WHERE g.time LIKE CONCAT(:year, '%')")
    List<GrowingCatm1Entity> findByYear(@Param("year") String year);

    @Query("SELECT g FROM GrowingCatm1Entity g WHERE g.time LIKE CONCAT(:year, '-', :month, '%')")
    List<GrowingCatm1Entity> findByYearAndMonth(@Param("year") String year, @Param("month") String month);

    @Query("SELECT g FROM GrowingCatm1Entity g WHERE g.time BETWEEN :startDate AND :endDate")
    List<GrowingCatm1Entity> findByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT g FROM GrowingCatm1Entity g WHERE g.time LIKE CONCAT(:year, '-', :month, '-', :day, '%')")
    List<GrowingCatm1Entity> findByYearMonthAndDay(@Param("year") String year, @Param("month") String month, @Param("day") String day);
}
