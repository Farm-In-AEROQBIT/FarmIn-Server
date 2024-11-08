package com.farmin.farminserver.domain.barns.gestation.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.gestation.dto.GestationResponse;
import com.farmin.farminserver.domain.barns.gestation.service.GestationService;
import com.farmin.farminserver.domain.barns.gestation.dto.GestationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gestation")
public class GestationController {
    private final GestationService gestationService;

    @PostMapping
    public ResponseEntity<Api<GestationResponse>> createGestation(@RequestBody GestationRequest dto) {
        GestationResponse response = gestationService.createGestation(dto);
        return ResponseEntity.ok(Api.OK(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<GestationResponse>> getGestationById(@PathVariable Integer id) {
        GestationResponse response = gestationService.getGestationById(id);
        return ResponseEntity.ok(Api.OK(response));
    }
}
