package com.farmin.farminserver.domain.catm1.pigletcatm1.controller;

import com.farmin.farminserver.domain.catm1.pigletcatm1.dto.PigletCatm1Request;
import com.farmin.farminserver.domain.catm1.pigletcatm1.dto.PigletCatm1Response;
import com.farmin.farminserver.domain.catm1.pigletcatm1.service.PigletCatm1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pigletcatm1")
public class PigletCatm1Controller {

    private final PigletCatm1Service pigletCatm1Service;

    @PostMapping
    public ResponseEntity<PigletCatm1Response> createPigletCatm1(@RequestBody PigletCatm1Request request) {
        PigletCatm1Response response = pigletCatm1Service.createPigletCatm1(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PigletCatm1Response>> getAllPigletCatm1() {
        return ResponseEntity.ok(pigletCatm1Service.getAllPigletCatm1());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PigletCatm1Response> getPigletCatm1ById(@PathVariable Integer id) {
        return ResponseEntity.ok(pigletCatm1Service.getPigletCatm1ById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PigletCatm1Response> updatePigletCatm1ById(@PathVariable Integer id, @RequestBody PigletCatm1Request request) {
        return ResponseEntity.ok(pigletCatm1Service.updatePigletCatm1(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePigletCatm1ById(@PathVariable Integer id) {
        pigletCatm1Service.deletePigletCatm1(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<PigletCatm1Response>> getStatistics(
            @RequestParam(defaultValue = "yearly") String type,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String weekOrDay
    ) {
        List<PigletCatm1Response> responses = pigletCatm1Service.getStatistics(type, year, month, weekOrDay);
        return ResponseEntity.ok(responses);
    }
}
