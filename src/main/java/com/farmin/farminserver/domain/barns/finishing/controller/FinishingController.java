package com.farmin.farminserver.domain.barns.finishing.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.finishing.dto.FinishingRequest;
import com.farmin.farminserver.domain.barns.finishing.dto.FinishingResponse;
import com.farmin.farminserver.domain.barns.finishing.service.FinishingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finishing")
@RequiredArgsConstructor
public class FinishingController {

    private final FinishingService finishingService;

    @PostMapping
    public ResponseEntity<Api<FinishingResponse>> create(@RequestBody FinishingRequest request) {
        return ResponseEntity.ok(Api.OK(finishingService.createFinishing(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<FinishingResponse>> get(@PathVariable Integer id) {
        return ResponseEntity.ok(Api.OK(finishingService.getFinishingById(id)));
    }

    @GetMapping
    public ResponseEntity<Api<List<FinishingResponse>>> getAll() {
        return ResponseEntity.ok(Api.OK(finishingService.getAllFinishing()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Api<String>> delete(@PathVariable Integer id) {
        finishingService.deleteFinishing(id);
        return ResponseEntity.ok(Api.OK("삭제 완료"));
    }
}
