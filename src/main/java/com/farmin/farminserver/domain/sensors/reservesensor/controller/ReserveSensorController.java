package com.farmin.farminserver.domain.sensors.reservesensor.controller;

import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorRequest;
import com.farmin.farminserver.domain.sensors.reservesensor.dto.ReserveSensorResponse;
import com.farmin.farminserver.domain.sensors.reservesensor.service.ReserveSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservesensors")
@RequiredArgsConstructor
public class ReserveSensorController {

    private final ReserveSensorService reserveSensorService;

    @PostMapping
    public ResponseEntity<ReserveSensorResponse> createReserveSensor(@RequestBody ReserveSensorRequest request) {
        ReserveSensorResponse response = reserveSensorService.createReserveSensor(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReserveSensorResponse>> getAllReserveSensors() {
        return ResponseEntity.ok(reserveSensorService.getAllReserveSensors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReserveSensorResponse> getReserveSensorById(@PathVariable int id) {
        return ResponseEntity.ok(reserveSensorService.getReserveSensorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReserveSensorResponse> updateReserveSensor(@PathVariable int id, @RequestBody ReserveSensorRequest request) {
        return ResponseEntity.ok(reserveSensorService.updateReserveSensor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserveSensor(@PathVariable int id) {
        reserveSensorService.deleteReserveSensor(id);
        return ResponseEntity.noContent().build();
    }
}
