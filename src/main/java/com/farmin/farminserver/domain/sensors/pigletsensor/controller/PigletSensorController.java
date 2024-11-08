package com.farmin.farminserver.domain.sensors.pigletsensor.controller;

import com.farmin.farminserver.domain.sensors.pigletsensor.dto.PigletSensorRequest;
import com.farmin.farminserver.domain.sensors.pigletsensor.dto.PigletSensorResponse;
import com.farmin.farminserver.domain.sensors.pigletsensor.service.PigletSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pigletsensors")
@RequiredArgsConstructor
public class PigletSensorController {

    private final PigletSensorService pigletSensorService;

    @PostMapping
    public ResponseEntity<PigletSensorResponse> createPigletSensor(@RequestBody PigletSensorRequest request) {
        PigletSensorResponse response = pigletSensorService.createPigletSensor(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PigletSensorResponse>> getAllPigletSensors() {
        return ResponseEntity.ok(pigletSensorService.getAllPigletSensors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PigletSensorResponse> getPigletSensorById(@PathVariable int id) {
        return ResponseEntity.ok(pigletSensorService.getPigletSensorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PigletSensorResponse> updatePigletSensor(@PathVariable int id, @RequestBody PigletSensorRequest request) {
        return ResponseEntity.ok(pigletSensorService.updatePigletSensor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePigletSensor(@PathVariable int id) {
        pigletSensorService.deletePigletSensor(id);
        return ResponseEntity.noContent().build();
    }
}
