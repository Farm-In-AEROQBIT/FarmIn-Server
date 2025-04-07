package com.farmin.farminserver.entity.catm1.finishingcatm1sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FinishingCatm1Repository extends JpaRepository<FinishingCatm1Entity, Integer> {

    @Query("SELECT f FROM FinishingCatm1Entity f WHERE f.time LIKE CONCAT(:year, '%')")
    List<FinishingCatm1Entity> findByYear(@Param("year") String year);

    @Query("SELECT f FROM FinishingCatm1Entity f WHERE f.time LIKE CONCAT(:year, '-', :month, '%')")
    List<FinishingCatm1Entity> findByYearAndMonth(@Param("year") String year, @Param("month") String month);

    @Query("SELECT f FROM FinishingCatm1Entity f WHERE f.time BETWEEN :startDate AND :endDate")
    List<FinishingCatm1Entity> findByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT f FROM FinishingCatm1Entity f WHERE f.time LIKE CONCAT(:year, '-', :month, '-', :day, '%')")
    List<FinishingCatm1Entity> findByYearMonthAndDay(@Param("year") String year, @Param("month") String month, @Param("day") String day);
}
