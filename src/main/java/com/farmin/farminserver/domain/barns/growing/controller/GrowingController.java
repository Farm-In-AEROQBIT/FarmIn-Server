package com.farmin.farminserver.domain.barns.growing.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.growing.dto.GrowingRequest;
import com.farmin.farminserver.domain.barns.growing.dto.GrowingResponse;
import com.farmin.farminserver.domain.barns.growing.service.GrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/growing")
public class GrowingController {
    private final GrowingService growingService;

    @PostMapping
    public ResponseEntity<Api<GrowingResponse>> createGrowing(@RequestBody GrowingRequest dto) {
        GrowingResponse response = growingService.createGrowing(dto);
        return ResponseEntity.ok(Api.OK(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<GrowingResponse>> getGrowingById(@PathVariable Integer id) {
        GrowingResponse response = growingService.getGrowingById(id);
        return ResponseEntity.ok(Api.OK(response));
    }
}
