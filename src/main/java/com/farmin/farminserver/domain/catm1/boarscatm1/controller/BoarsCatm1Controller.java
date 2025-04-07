package com.farmin.farminserver.domain.catm1.boarscatm1.controller;

import com.farmin.farminserver.domain.catm1.boarscatm1.dto.BoarsCatm1Request;
import com.farmin.farminserver.domain.catm1.boarscatm1.dto.BoarsCatm1Response;
import com.farmin.farminserver.domain.catm1.boarscatm1.service.BoarsCatm1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boarscatm1")
public class BoarsCatm1Controller {

    private final BoarsCatm1Service boarsCatm1Service;

    @PostMapping
    public ResponseEntity<BoarsCatm1Response> create(@RequestBody BoarsCatm1Request request) {
        return ResponseEntity.ok(boarsCatm1Service.createBoarsCatm1(request));
    }

    @GetMapping
    public ResponseEntity<List<BoarsCatm1Response>> getAllBoarsCatm1() {
        return ResponseEntity.ok(boarsCatm1Service.getAllBoarsCatm1());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoarsCatm1Response> getBoarsCatm1ById(@PathVariable Integer id) {
        return ResponseEntity.ok(boarsCatm1Service.getBoarsCatm1ById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoarsCatm1Response> updateBoarsCatm1ById(@PathVariable Integer id, @RequestBody BoarsCatm1Request request) {
        return ResponseEntity.ok(boarsCatm1Service.updateBoarsCatm1(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boarsCatm1Service.deleteBoarsCatm1(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<BoarsCatm1Response>> getStatistics(
            @RequestParam(defaultValue = "yearly") String type,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String weekOrDay
    ) {
        List<BoarsCatm1Response> responses = boarsCatm1Service.getStatistics(type, year, month, weekOrDay);
        return ResponseEntity.ok(responses);
    }
}
