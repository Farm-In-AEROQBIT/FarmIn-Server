package com.farmin.farminserver.domain.catm1.gestationcatm1.controller;

import com.farmin.farminserver.domain.catm1.gestationcatm1.dto.GestationCatm1Request;
import com.farmin.farminserver.domain.catm1.gestationcatm1.dto.GestationCatm1Response;
import com.farmin.farminserver.domain.catm1.gestationcatm1.service.GestationCatm1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gestationcatm1")
public class GestationCatm1Controller {

    private final GestationCatm1Service gestationCatm1Service;

    @PostMapping
    public ResponseEntity<GestationCatm1Response> createGestationCatm1(@RequestBody GestationCatm1Request request) {
        GestationCatm1Response response = gestationCatm1Service.createGestationCatm1(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GestationCatm1Response>> getAllGestationCatm1() {
        return ResponseEntity.ok(gestationCatm1Service.getAllGestationCatm1());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GestationCatm1Response> getGestationCatm1ById(@PathVariable Integer id) {
        return ResponseEntity.ok(gestationCatm1Service.getGestationCatm1ById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GestationCatm1Response> updateGestationCatm1ById(@PathVariable Integer id, @RequestBody GestationCatm1Request request) {
        return ResponseEntity.ok(gestationCatm1Service.updateGestationCatm1(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGestationCatm1ById(@PathVariable Integer id) {
        gestationCatm1Service.deleteGestationCatm1(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<GestationCatm1Response>> getStatistics(
            @RequestParam(defaultValue = "yearly") String type,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String weekOrDay
    ) {
        List<GestationCatm1Response> responses = gestationCatm1Service.getStatistics(type, year, month, weekOrDay);
        return ResponseEntity.ok(responses);
    }
}
