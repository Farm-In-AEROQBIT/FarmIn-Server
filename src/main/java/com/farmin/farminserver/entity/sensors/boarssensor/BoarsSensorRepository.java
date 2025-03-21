package com.farmin.farminserver.entity.sensors.boarssensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoarsSensorRepository extends JpaRepository<BoarsSensorEntity, Integer> {

    // 연간 통계: 연도만 일치
    @Query("SELECT g FROM BoarsSensorEntity g WHERE g.time LIKE CONCAT(:year, '%')")
    List<BoarsSensorEntity> findByYear(@Param("year") String year);

    // 월간 통계: 연도와 월 일치
    @Query("SELECT g FROM BoarsSensorEntity g WHERE g.time LIKE CONCAT(:year, '-', :month, '%')")
    List<BoarsSensorEntity> findByYearAndMonth(@Param("year") String year, @Param("month") String month);

    // 주간 통계: 특정 날짜 범위 (startDate ~ endDate)
    @Query("SELECT g FROM BoarsSensorEntity g WHERE g.time BETWEEN :startDate AND :endDate")
    List<BoarsSensorEntity> findByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 일간 통계: 특정 연도-월-일 일치
    @Query("SELECT g FROM BoarsSensorEntity g WHERE g.time LIKE CONCAT(:year, '-', :month, '-', :day, '%')")
    List<BoarsSensorEntity> findByYearMonthAndDay(@Param("year") String year, @Param("month") String month, @Param("day") String day);
}
