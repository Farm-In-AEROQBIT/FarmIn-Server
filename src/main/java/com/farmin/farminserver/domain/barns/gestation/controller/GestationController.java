package com.farmin.farminserver.domain.barns.gestation.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.gestation.dto.GestationRequest;
import com.farmin.farminserver.domain.barns.gestation.dto.GestationResponse;
import com.farmin.farminserver.domain.barns.gestation.service.GestationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gestation")
public class GestationController {

    private final GestationService gestationService;

    @PostMapping
    public ResponseEntity<Api<GestationResponse>> createGestation(@RequestBody GestationRequest dto) {
        return ResponseEntity.ok(Api.OK(gestationService.createGestation(dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<GestationResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(Api.OK(gestationService.getGestationById(id)));
    }

    @GetMapping
    public ResponseEntity<Api<List<GestationResponse>>> getAll() {
        return ResponseEntity.ok(Api.OK(gestationService.getAllGestation()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Api<String>> deleteById(@PathVariable Integer id) {
        gestationService.deleteGestationById(id);
        return ResponseEntity.ok(Api.OK("Deleted Gestation with ID: " + id));
    }
}
