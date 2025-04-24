package com.farmin.farminserver.domain.catm1.reservecatm1.controller;

import com.farmin.farminserver.domain.catm1.reservecatm1.dto.ReserveCatm1Request;
import com.farmin.farminserver.domain.catm1.reservecatm1.dto.ReserveCatm1Response;
import com.farmin.farminserver.domain.catm1.reservecatm1.service.ReserveCatm1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservecatm1")
public class    ReserveCatm1Controller {

    private final ReserveCatm1Service reserveCatm1Service;

    @PostMapping
    public ResponseEntity<ReserveCatm1Response> createReserveCatm1(@RequestBody ReserveCatm1Request request) {
        ReserveCatm1Response response = reserveCatm1Service.createReserveCatm1(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReserveCatm1Response>> getAllReserveCatm1() {
        return ResponseEntity.ok(reserveCatm1Service.getAllReserveCatm1());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReserveCatm1Response> getReserveCatm1ById(@PathVariable Integer id) {
        return ResponseEntity.ok(reserveCatm1Service.getReserveCatm1ById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReserveCatm1Response> updateReserveCatm1ById(@PathVariable Integer id, @RequestBody ReserveCatm1Request request) {
        return ResponseEntity.ok(reserveCatm1Service.updateReserveCatm1(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserveCatm1ById(@PathVariable Integer id) {
        reserveCatm1Service.deleteReserveCatm1(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<ReserveCatm1Response>> getStatistics(
            @RequestParam(defaultValue = "yearly") String type,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String weekOrDay
    ) {
        List<ReserveCatm1Response> responses = reserveCatm1Service.getStatistics(type, year, month, weekOrDay);
        return ResponseEntity.ok(responses);
    }
}
