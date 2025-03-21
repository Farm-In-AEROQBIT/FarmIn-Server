package com.farmin.farminserver.domain.sensors.boarssensor.service;

import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorRequest;
import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorResponse;

import java.util.List;

public interface BoarsSensorService {
    BoarsSensorResponse createBoarsSensor(BoarsSensorRequest request);
    List<BoarsSensorResponse> getAllBoarsSensors();
    BoarsSensorResponse getBoarsSensorById(int id);
    BoarsSensorResponse updateBoarsSensor(int id, BoarsSensorRequest request);
    void deleteBoarsSensor(int id);

    // 추가된 메서드
    List<BoarsSensorResponse> getStatistics(String type, String year, String month, String weekOrDay);
}
