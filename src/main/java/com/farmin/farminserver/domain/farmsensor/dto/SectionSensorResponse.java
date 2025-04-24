package com.farmin.farminserver.domain.farmsensor.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SectionSensorResponse {
    private Integer sensorId;
    private Integer sectionId;
    private String sectionType;
    private String sensorType; // "regular" or "catm1"

    // Common sensor fields
    private String co2;
    private String temperature;
    private String humidity;

    // Fields for specific sensor types
    private String nh3;
    private String pm;
    private String waterTemperature;

    private LocalDateTime timestamp;
}
