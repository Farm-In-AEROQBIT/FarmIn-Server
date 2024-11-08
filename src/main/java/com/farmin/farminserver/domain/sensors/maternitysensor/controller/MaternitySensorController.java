package com.farmin.farminserver.domain.sensors.maternitysensor.controller;

import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorRequest;
import com.farmin.farminserver.domain.sensors.maternitysensor.dto.MaternitySensorResponse;
import com.farmin.farminserver.domain.sensors.maternitysensor.service.MaternitySensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maternitysensors")
@RequiredArgsConstructor
public class MaternitySensorController {

    private final MaternitySensorService maternitySensorService;

    @PostMapping
    public ResponseEntity<MaternitySensorResponse> createMaternitySensor(@RequestBody MaternitySensorRequest request) {
        MaternitySensorResponse response = maternitySensorService.createMaternitySensor(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MaternitySensorResponse>> getAllMaternitySensors() {
        return ResponseEntity.ok(maternitySensorService.getAllMaternitySensors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaternitySensorResponse> getMaternitySensorById(@PathVariable int id) {
        return ResponseEntity.ok(maternitySensorService.getMaternitySensorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaternitySensorResponse> updateMaternitySensor(@PathVariable int id, @RequestBody MaternitySensorRequest request) {
        return ResponseEntity.ok(maternitySensorService.updateMaternitySensor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaternitySensor(@PathVariable int id) {
        maternitySensorService.deleteMaternitySensor(id);
        return ResponseEntity.noContent().build();
    }
}
