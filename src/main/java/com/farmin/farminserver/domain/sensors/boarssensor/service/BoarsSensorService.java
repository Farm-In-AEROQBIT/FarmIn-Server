package com.farmin.farminserver.domain.sensors.boarssensor.service;

import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorRequest;
import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorResponse;

import java.util.List;

public interface BoarsSensorService {
    BoarsSensorResponse createBoarsSensor(BoarsSensorRequest request);
    BoarsSensorResponse getBoarsSensorById(Integer id);
    List<BoarsSensorResponse> getAllBoarsSensors();
    void deleteBoarsSensor(Integer id);
}
