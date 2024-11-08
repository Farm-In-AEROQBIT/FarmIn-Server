package com.farmin.farminserver.domain.sensors.finishingsensor.controller;

import com.farmin.farminserver.domain.sensors.finishingsensor.dto.FinishingSensorRequest;
import com.farmin.farminserver.domain.sensors.finishingsensor.dto.FinishingSensorResponse;
import com.farmin.farminserver.domain.sensors.finishingsensor.service.FinishingSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finishingsensors")
@RequiredArgsConstructor
public class FinishingSensorController {

    private final FinishingSensorService finishingSensorService;

    @PostMapping
    public ResponseEntity<FinishingSensorResponse> createFinishingSensor(@RequestBody FinishingSensorRequest request) {
        FinishingSensorResponse response = finishingSensorService.createFinishingSensor(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FinishingSensorResponse>> getAllFinishingSensors() {
        return ResponseEntity.ok(finishingSensorService.getAllFinishingSensors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinishingSensorResponse> getFinishingSensorById(@PathVariable int id) {
        return ResponseEntity.ok(finishingSensorService.getFinishingSensorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinishingSensorResponse> updateFinishingSensor(@PathVariable int id, @RequestBody FinishingSensorRequest request) {
        return ResponseEntity.ok(finishingSensorService.updateFinishingSensor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinishingSensor(@PathVariable int id) {
        finishingSensorService.deleteFinishingSensor(id);
        return ResponseEntity.noContent().build();
    }
}
