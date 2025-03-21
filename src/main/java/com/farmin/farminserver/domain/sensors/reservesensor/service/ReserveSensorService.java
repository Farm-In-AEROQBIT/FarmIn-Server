package com.farmin.farminserver.domain.sensors.reservesensor.service;

import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorRequest;
import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorResponse;

import java.util.List;

public interface ReserveSensorService {
    ReserveSensorResponse createReserveSensor(ReserveSensorRequest request);
    List<ReserveSensorResponse> getAllReserveSensors();
    ReserveSensorResponse getReserveSensorById(int id);
    ReserveSensorResponse updateReserveSensor(int id, ReserveSensorRequest request);
    void deleteReserveSensor(int id);

    // 추가된 메서드
    List<ReserveSensorResponse> getStatistics(String type, String year, String month, String weekOrDay);
}
