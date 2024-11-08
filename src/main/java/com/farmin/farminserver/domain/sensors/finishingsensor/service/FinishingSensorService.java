package com.farmin.farminserver.domain.sensors.finishingsensor.service;

import com.farmin.farminserver.domain.sensors.finishingsensor.dto.FinishingSensorRequest;
import com.farmin.farminserver.domain.sensors.finishingsensor.dto.FinishingSensorResponse;

import java.util.List;

public interface FinishingSensorService {
    FinishingSensorResponse createFinishingSensor(FinishingSensorRequest request);
    List<FinishingSensorResponse> getAllFinishingSensors();
    FinishingSensorResponse getFinishingSensorById(int id);
    FinishingSensorResponse updateFinishingSensor(int id, FinishingSensorRequest request);
    void deleteFinishingSensor(int id);
}
