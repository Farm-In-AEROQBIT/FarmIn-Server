package com.farmin.farminserver.domain.catm1.growingcatm1.controller;

import com.farmin.farminserver.domain.catm1.growingcatm1.dto.GrowingCatm1Request;
import com.farmin.farminserver.domain.catm1.growingcatm1.dto.GrowingCatm1Response;
import com.farmin.farminserver.domain.catm1.growingcatm1.service.GrowingCatm1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/growingcatm1")
public class GrowingCatm1Controller {

    private final GrowingCatm1Service growingCatm1Service;

    @PostMapping
    public ResponseEntity<GrowingCatm1Response> createGrowingCatm1(@RequestBody GrowingCatm1Request request) {
        GrowingCatm1Response response = growingCatm1Service.createGrowingCatm1(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GrowingCatm1Response>> getAllGrowingCatm1() {
        return ResponseEntity.ok(growingCatm1Service.getAllGrowingCatm1());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrowingCatm1Response> getGrowingCatm1ById(@PathVariable Integer id) {
        return ResponseEntity.ok(growingCatm1Service.getGrowingCatm1ById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrowingCatm1Response> updateGrowingCatm1ById(@PathVariable Integer id, @RequestBody GrowingCatm1Request request) {
        return ResponseEntity.ok(growingCatm1Service.updateGrowingCatm1(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrowingCatm1ById(@PathVariable Integer id) {
        growingCatm1Service.deleteGrowingCatm1(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<GrowingCatm1Response>> getStatistics(
            @RequestParam(defaultValue = "yearly") String type,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String weekOrDay
    ) {
        List<GrowingCatm1Response> responses = growingCatm1Service.getStatistics(type, year, month, weekOrDay);
        return ResponseEntity.ok(responses);
    }
}
