package com.farmin.farminserver.domain.sensors.boarssensor.controller;

import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorRequest;
import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorResponse;
import com.farmin.farminserver.domain.sensors.boarssensor.service.BoarsSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boarssensors")
@RequiredArgsConstructor
public class BoarsSensorController {

    private final BoarsSensorService boarsSensorService;

    @PostMapping
    public ResponseEntity<BoarsSensorResponse> createBoarsSensor(@RequestBody BoarsSensorRequest request) {
        BoarsSensorResponse response = boarsSensorService.createBoarsSensor(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BoarsSensorResponse>> getAllBoarsSensors() {
        return ResponseEntity.ok(boarsSensorService.getAllBoarsSensors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoarsSensorResponse> getBoarsSensorById(@PathVariable int id) {
        return ResponseEntity.ok(boarsSensorService.getBoarsSensorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoarsSensorResponse> updateBoarsSensor(@PathVariable int id, @RequestBody BoarsSensorRequest request) {
        return ResponseEntity.ok(boarsSensorService.updateBoarsSensor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoarsSensor(@PathVariable int id) {
        boarsSensorService.deleteBoarsSensor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<BoarsSensorResponse>> getStatistics(
            @RequestParam(defaultValue = "yearly") String type, // 기본값 설정
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String weekOrDay
    ) {
        List<BoarsSensorResponse> responses = boarsSensorService.getStatistics(type, year, month, weekOrDay);
        return ResponseEntity.ok(responses);
    }

}
