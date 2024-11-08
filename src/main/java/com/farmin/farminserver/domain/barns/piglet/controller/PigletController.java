package com.farmin.farminserver.domain.barns.piglet.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.piglet.dto.PigletRequest;
import com.farmin.farminserver.domain.barns.piglet.dto.PigletResponse;
import com.farmin.farminserver.domain.barns.piglet.service.PigletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/piglet")
public class PigletController {
    private final PigletService pigletService;

    @PostMapping
    public ResponseEntity<Api<PigletResponse>> createPiglet(@RequestBody PigletRequest dto) {
        PigletResponse response = pigletService.createPiglet(dto);
        return ResponseEntity.ok(Api.OK(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<PigletResponse>> getPigletById(@PathVariable Integer id) {
        PigletResponse response = pigletService.getPigletById(id);
        return ResponseEntity.ok(Api.OK(response));
    }
}
