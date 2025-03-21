package com.farmin.farminserver.domain.sensors.gestationsensor.controller;

import com.farmin.farminserver.domain.sensors.gestationsensor.dto.GestationSensorRequest;
import com.farmin.farminserver.domain.sensors.gestationsensor.dto.GestationSensorResponse;
import com.farmin.farminserver.domain.sensors.gestationsensor.service.GestationSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestationsensors")
@RequiredArgsConstructor
public class GestationSensorController {

    private final GestationSensorService gestationSensorService;

    @PostMapping
    public ResponseEntity<GestationSensorResponse> createGestationSensor(@RequestBody GestationSensorRequest request) {
        GestationSensorResponse response = gestationSensorService.createGestationSensor(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GestationSensorResponse>> getAllGestationSensors() {
        return ResponseEntity.ok(gestationSensorService.getAllGestationSensors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GestationSensorResponse> getGestationSensorById(@PathVariable int id) {
        return ResponseEntity.ok(gestationSensorService.getGestationSensorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GestationSensorResponse> updateGestationSensor(@PathVariable int id, @RequestBody GestationSensorRequest request) {
        return ResponseEntity.ok(gestationSensorService.updateGestationSensor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGestationSensor(@PathVariable int id) {
        gestationSensorService.deleteGestationSensor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<GestationSensorResponse>> getStatistics(
            @RequestParam(defaultValue = "yearly") String type, // 기본값 설정
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String weekOrDay
    ) {
        List<GestationSensorResponse> responses = gestationSensorService.getStatistics(type, year, month, weekOrDay);
        return ResponseEntity.ok(responses);
    }

}
