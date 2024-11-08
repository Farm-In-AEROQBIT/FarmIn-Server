package com.farmin.farminserver.domain.barns.finishing.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.finishing.dto.FinishingRequest;
import com.farmin.farminserver.domain.barns.finishing.dto.FinishingResponse;
import com.farmin.farminserver.domain.barns.finishing.service.FinishingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/finishing")
public class FinishingController {
    private final FinishingService finishingService;

    @PostMapping
    public ResponseEntity<Api<FinishingResponse>> createFinishing(@RequestBody FinishingRequest dto) {
        FinishingResponse response = finishingService.createFinishing(dto);
        return ResponseEntity.ok(Api.OK(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<FinishingResponse>> getFinishingById(@PathVariable Integer id) {
        FinishingResponse response = finishingService.getFinishingById(id);
        return ResponseEntity.ok(Api.OK(response));
    }
}
