package com.farmin.farminserver.domain.catm1.maternitycatm1.controller;

import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1Request;
import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1Response;
import com.farmin.farminserver.domain.catm1.maternitycatm1.service.MaternityCatm1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/maternitycatm1")
public class MaternityCatm1Controller {

    private final MaternityCatm1Service maternityCatm1Service;

    @PostMapping
    public ResponseEntity<MaternityCatm1Response> createMaternityCatm1(@RequestBody MaternityCatm1Request request) {
        MaternityCatm1Response response = maternityCatm1Service.createMaternityCatm1(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MaternityCatm1Response>> getAllMaternityCatm1() {
        return ResponseEntity.ok(maternityCatm1Service.getAllMaternityCatm1());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaternityCatm1Response> getMaternityCatm1ById(@PathVariable Integer id) {
        return ResponseEntity.ok(maternityCatm1Service.getMaternityCatm1ById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaternityCatm1Response> updateMaternityCatm1ById(@PathVariable Integer id, @RequestBody MaternityCatm1Request request) {
        return ResponseEntity.ok(maternityCatm1Service.updateMaternityCatm1(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaternityCatm1ById(@PathVariable Integer id) {
        maternityCatm1Service.deleteMaternityCatm1(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<MaternityCatm1Response>> getStatistics(
            @RequestParam(defaultValue = "yearly") String type,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String weekOrDay
    ) {
        List<MaternityCatm1Response> responses = maternityCatm1Service.getStatistics(type, year, month, weekOrDay);
        return ResponseEntity.ok(responses);
    }
}
