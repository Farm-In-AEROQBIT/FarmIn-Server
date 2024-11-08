package com.farmin.farminserver.domain.sensors.gestationsensor.service;

import com.farmin.farminserver.domain.sensors.gestationsensor.dto.GestationSensorRequest;
import com.farmin.farminserver.domain.sensors.gestationsensor.dto.GestationSensorResponse;

import java.util.List;

public interface GestationSensorService {
    GestationSensorResponse createGestationSensor(GestationSensorRequest request);
    List<GestationSensorResponse> getAllGestationSensors();
    GestationSensorResponse getGestationSensorById(int id);
    GestationSensorResponse updateGestationSensor(int id, GestationSensorRequest request);
    void deleteGestationSensor(int id);
}
