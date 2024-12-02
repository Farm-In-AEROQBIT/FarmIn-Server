package com.farmin.farminserver.domain.sensors.growingsensor.controller;

import com.farmin.farminserver.domain.sensors.growingsensor.dto.GrowingSensorRequest;
import com.farmin.farminserver.domain.sensors.growingsensor.dto.GrowingSensorResponse;
import com.farmin.farminserver.domain.sensors.growingsensor.service.GrowingSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/growingsensors")
@RequiredArgsConstructor
public class GrowingSensorController {

    private final GrowingSensorService growingSensorService;

    @PostMapping
    public ResponseEntity<GrowingSensorResponse> createGrowingSensor(@RequestBody GrowingSensorRequest request) {
        GrowingSensorResponse response = growingSensorService.createGrowingSensor(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GrowingSensorResponse>> getAllGrowingSensors() {
        return ResponseEntity.ok(growingSensorService.getAllGrowingSensors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrowingSensorResponse> getGrowingSensorById(@PathVariable int id) {
        return ResponseEntity.ok(growingSensorService.getGrowingSensorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrowingSensorResponse> updateGrowingSensor(@PathVariable int id, @RequestBody GrowingSensorRequest request) {
        return ResponseEntity.ok(growingSensorService.updateGrowingSensor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrowingSensor(@PathVariable int id) {
        growingSensorService.deleteGrowingSensor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<GrowingSensorResponse>> getStatistics(
            @RequestParam(defaultValue = "yearly") String type, // 기본값 설정
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String weekOrDay
    ) {
        List<GrowingSensorResponse> responses = growingSensorService.getStatistics(type, year, month, weekOrDay);
        return ResponseEntity.ok(responses);
    }

}
