package com.farmin.farminserver.entity.catm1.pigletcatm1sensor;

import com.farmin.farminserver.entity.barns.piglet.PigletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PigletCatm1Repository extends JpaRepository<PigletCatm1Entity, Integer> {

    boolean existsByPigletIdAndTime(Integer pigletId, LocalDateTime time);
    //Optional<PigletEntity> findBySnFarmId(String snFarmId);

    List<PigletCatm1Entity> findByPigletId(Integer pigletId);

    // 연간 통계: 연도만 일치
    @Query("SELECT g FROM PigletCatm1Entity g WHERE FUNCTION('YEAR', g.time) = :year")
    List<PigletCatm1Entity> findByYear(@Param("year") int year);

    // 월간 통계: 연도와 월 일치
    @Query("SELECT g FROM PigletCatm1Entity g WHERE FUNCTION('YEAR', g.time) = :year AND FUNCTION('MONTH', g.time) = :month")
    List<PigletCatm1Entity> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    // 주간 통계: 특정 날짜 범위 (startDate ~ endDate)
    @Query("SELECT g FROM PigletCatm1Entity g WHERE g.time BETWEEN :startDate AND :endDate")
    List<PigletCatm1Entity> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 일간 통계: 특정 연도-월-일 일치
    @Query("SELECT g FROM PigletCatm1Entity g WHERE FUNCTION('YEAR', g.time) = " +
            ":year AND FUNCTION('MONTH', g.time) = :month AND FUNCTION('DAY', g.time) = :day")
    List<PigletCatm1Entity> findByYearMonthAndDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);
}
