package com.farmin.farminserver.domain.sensors.growingsensor.service;

import com.farmin.farminserver.domain.sensors.growingsensor.dto.GrowingSensorRequest;
import com.farmin.farminserver.domain.sensors.growingsensor.dto.GrowingSensorResponse;

import java.util.List;

public interface GrowingSensorService {
    GrowingSensorResponse createGrowingSensor(GrowingSensorRequest request);
    List<GrowingSensorResponse> getAllGrowingSensors();
    GrowingSensorResponse getGrowingSensorById(int id);
    GrowingSensorResponse updateGrowingSensor(int id, GrowingSensorRequest request);
    void deleteGrowingSensor(int id);
}
