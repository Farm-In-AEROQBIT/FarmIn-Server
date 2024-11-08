package com.farmin.farminserver.domain.sensors.boarssensor.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorRequest;
import com.farmin.farminserver.domain.sensors.boarssensor.dto.BoarsSensorResponse;
import com.farmin.farminserver.domain.sensors.boarssensor.service.BoarsSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boars-sensor")
public class BoarsSensorController {
    private final BoarsSensorService boarsSensorService;

    @PostMapping
    public ResponseEntity<Api<BoarsSensorResponse>> createBoarsSensor(@RequestBody BoarsSensorRequest request) {
        BoarsSensorResponse response = boarsSensorService.createBoarsSensor(request);
        return ResponseEntity.ok(Api.OK(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<BoarsSensorResponse>> getBoarsSensorById(@PathVariable Integer id) {
        BoarsSensorResponse response = boarsSensorService.getBoarsSensorById(id);
        return ResponseEntity.ok(Api.OK(response));
    }

    @GetMapping
    public ResponseEntity<Api<List<BoarsSensorResponse>>> getAllBoarsSensors() {
        List<BoarsSensorResponse> responses = boarsSensorService.getAllBoarsSensors();
        return ResponseEntity.ok(Api.OK(responses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Api<Void>> deleteBoarsSensor(@PathVariable Integer id) {
        boarsSensorService.deleteBoarsSensor(id);
        return ResponseEntity.ok(Api.OK(null));
    }
}
