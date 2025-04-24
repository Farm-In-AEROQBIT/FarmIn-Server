package com.farmin.farminserver.domain.farmsensor.service;

import com.farmin.farminserver.domain.farmsensor.dto.FarmSectionResponse;
import com.farmin.farminserver.domain.farmsensor.dto.SectionSensorResponse;

import java.util.List;

public interface FarmSensorService {
    List<FarmSectionResponse> getFarmsByUsername(String username);
    List<FarmSectionResponse> getSectionsByFarmId(Integer farmId);
    List<SectionSensorResponse> getSensorsBySection(Integer sectionId, String sectionType, String sensorType);
}