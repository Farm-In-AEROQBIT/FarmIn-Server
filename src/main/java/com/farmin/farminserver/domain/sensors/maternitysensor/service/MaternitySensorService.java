package com.farmin.farminserver.domain.sensors.maternitysensor.service;

import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorRequest;
import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorResponse;

import java.util.List;

public interface MaternitySensorService {
    MaternitySensorResponse createMaternitySensor(MaternitySensorRequest request);
    List<MaternitySensorResponse> getAllMaternitySensors();
    MaternitySensorResponse getMaternitySensorById(int id);
    MaternitySensorResponse updateMaternitySensor(int id, MaternitySensorRequest request);
    void deleteMaternitySensor(int id);
}
