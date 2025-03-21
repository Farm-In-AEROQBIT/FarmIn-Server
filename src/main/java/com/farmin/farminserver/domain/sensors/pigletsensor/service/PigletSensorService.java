package com.farmin.farminserver.domain.sensors.pigletsensor.service;

import com.farmin.farminserver.domain.sensors.pigletsensor.dto.PigletSensorRequest;
import com.farmin.farminserver.domain.sensors.pigletsensor.dto.PigletSensorResponse;

import java.util.List;

public interface PigletSensorService {
    PigletSensorResponse createPigletSensor(PigletSensorRequest request);
    List<PigletSensorResponse> getAllPigletSensors();
    PigletSensorResponse getPigletSensorById(int id);
    PigletSensorResponse updatePigletSensor(int id, PigletSensorRequest request);
    void deletePigletSensor(int id);

    // 추가된 메서드
    List<PigletSensorResponse> getStatistics(String type, String year, String month, String weekOrDay);
}
