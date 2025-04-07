package com.farmin.farminserver.entity.catm1.reservecatm1sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReserveCatm1Repository extends JpaRepository<ReserveCatm1Entity, Integer> {

    @Query("SELECT r FROM ReserveCatm1Entity r WHERE r.time LIKE CONCAT(:year, '%')")
    List<ReserveCatm1Entity> findByYear(@Param("year") String year);

    @Query("SELECT r FROM ReserveCatm1Entity r WHERE r.time LIKE CONCAT(:year, '-', :month, '%')")
    List<ReserveCatm1Entity> findByYearAndMonth(@Param("year") String year, @Param("month") String month);

    @Query("SELECT r FROM ReserveCatm1Entity r WHERE r.time BETWEEN :startDate AND :endDate")
    List<ReserveCatm1Entity> findByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT r FROM ReserveCatm1Entity r WHERE r.time LIKE CONCAT(:year, '-', :month, '-', :day, '%')")
    List<ReserveCatm1Entity> findByYearMonthAndDay(@Param("year") String year, @Param("month") String month, @Param("day") String day);
}
