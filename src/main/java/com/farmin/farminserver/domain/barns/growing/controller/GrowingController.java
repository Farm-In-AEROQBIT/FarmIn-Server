package com.farmin.farminserver.domain.barns.growing.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.growing.dto.GrowingRequest;
import com.farmin.farminserver.domain.barns.growing.dto.GrowingResponse;
import com.farmin.farminserver.domain.barns.growing.service.GrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/growing")
public class GrowingController {

    private final GrowingService growingService;

    @PostMapping
    public ResponseEntity<Api<GrowingResponse>> create(@RequestBody GrowingRequest dto) {
        return ResponseEntity.ok(Api.OK(growingService.createGrowing(dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<GrowingResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(Api.OK(growingService.getGrowingById(id)));
    }

    @GetMapping
    public ResponseEntity<Api<List<GrowingResponse>>> getAll() {
        return ResponseEntity.ok(Api.OK(growingService.getAllGrowing()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Api<String>> delete(@PathVariable Integer id) {
        growingService.deleteGrowingById(id);
        return ResponseEntity.ok(Api.OK("Deleted Growing with ID: " + id));
    }
}
