package com.farmin.farminserver.entity.sensors.finishingsensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FinishingSensorRepository extends JpaRepository<FinishingSensorEntity, Integer> {

    // 연간 통계: 연도만 일치
    @Query("SELECT g FROM FinishingSensorEntity g WHERE g.time LIKE CONCAT(:year, '%')")
    List<FinishingSensorEntity> findByYear(@Param("year") String year);

    // 월간 통계: 연도와 월 일치
    @Query("SELECT g FROM FinishingSensorEntity g WHERE g.time LIKE CONCAT(:year, '-', :month, '%')")
    List<FinishingSensorEntity> findByYearAndMonth(@Param("year") String year, @Param("month") String month);

    // 주간 통계: 특정 날짜 범위 (startDate ~ endDate)
    @Query("SELECT g FROM FinishingSensorEntity g WHERE g.time BETWEEN :startDate AND :endDate")
    List<FinishingSensorEntity> findByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 일간 통계: 특정 연도-월-일 일치
    @Query("SELECT g FROM FinishingSensorEntity g WHERE g.time LIKE CONCAT(:year, '-', :month, '-', :day, '%')")
    List<FinishingSensorEntity> findByYearMonthAndDay(@Param("year") String year, @Param("month") String month, @Param("day") String day);
}
