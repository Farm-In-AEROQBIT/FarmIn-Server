package com.farmin.farminserver.domain.farmsensor.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.farmsensor.dto.FarmSectionResponse;
import com.farmin.farminserver.domain.farmsensor.dto.SectionSensorResponse;
import com.farmin.farminserver.domain.farmsensor.service.FarmSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FarmSensorController {

    private final FarmSensorService farmSensorService;

    @GetMapping("/api/farms")
    public ResponseEntity<Api<List<FarmSectionResponse>>> getUserFarms(
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<FarmSectionResponse> farms = farmSensorService.getFarmsByUsername(username);
        return ResponseEntity.ok(Api.OK(farms));
    }

    @GetMapping("/api/farm/{farmId}/sections")
    public ResponseEntity<Api<List<FarmSectionResponse>>> getFarmSections(@PathVariable Integer farmId) {
        List<FarmSectionResponse> sections = farmSensorService.getSectionsByFarmId(farmId);
        return ResponseEntity.ok(Api.OK(sections));
    }

    @GetMapping("/api/section/{sectionId}/sensors")
    public ResponseEntity<Api<List<SectionSensorResponse>>> getSectionSensors(
            @PathVariable Integer sectionId,
            @RequestParam String sectionType,
            @RequestParam(required = false) String sensorType) {

        List<SectionSensorResponse> sensors = farmSensorService.getSensorsBySection(sectionId, sectionType, sensorType);
        return ResponseEntity.ok(Api.OK(sensors));
    }
}