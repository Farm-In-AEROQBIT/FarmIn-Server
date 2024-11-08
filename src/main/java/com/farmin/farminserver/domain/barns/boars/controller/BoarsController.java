package com.farmin.farminserver.domain.barns.boars.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.boars.dto.BoarsRequest;
import com.farmin.farminserver.domain.barns.boars.dto.BoarsResponse;
import com.farmin.farminserver.domain.barns.boars.service.BoarsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boars")
public class BoarsController {
    private final BoarsService boarsService;

    @PostMapping
    public ResponseEntity<Api<BoarsResponse>> createBoars(@RequestBody BoarsRequest dto) {
        BoarsResponse response = boarsService.createBoars(dto);
        return ResponseEntity.ok(Api.OK(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<BoarsResponse>> getBoarsById(@PathVariable Integer id) {
        BoarsResponse response = boarsService.getBoarsById(id);
        return ResponseEntity.ok(Api.OK(response));
    }
}
