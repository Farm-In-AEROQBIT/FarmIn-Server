package com.farmin.farminserver.entity.catm1.maternitycatm1sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaternityCatm1Repository extends JpaRepository<MaternityCatm1Entity, Integer> {

    @Query("SELECT m FROM MaternityCatm1Entity m WHERE m.time LIKE CONCAT(:year, '%')")
    List<MaternityCatm1Entity> findByYear(@Param("year") String year);

    @Query("SELECT m FROM MaternityCatm1Entity m WHERE m.time LIKE CONCAT(:year, '-', :month, '%')")
    List<MaternityCatm1Entity> findByYearAndMonth(@Param("year") String year, @Param("month") String month);

    @Query("SELECT m FROM MaternityCatm1Entity m WHERE m.time BETWEEN :startDate AND :endDate")
    List<MaternityCatm1Entity> findByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT m FROM MaternityCatm1Entity m WHERE m.time LIKE CONCAT(:year, '-', :month, '-', :day, '%')")
    List<MaternityCatm1Entity> findByYearMonthAndDay(@Param("year") String year, @Param("month") String month, @Param("day") String day);
}
