package com.farmin.farminserver.domain.barns.maternity.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.maternity.dto.MaternityRequest;
import com.farmin.farminserver.domain.barns.maternity.dto.MaternityResponse;
import com.farmin.farminserver.domain.barns.maternity.service.MaternityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/maternity")
public class MaternityController {
    private final MaternityService maternityService;

    @PostMapping
    public ResponseEntity<Api<MaternityResponse>> createMaternity(@RequestBody MaternityRequest dto) {
        MaternityResponse response = maternityService.createMaternity(dto);
        return ResponseEntity.ok(Api.OK(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<MaternityResponse>> getMaternityById(@PathVariable Integer id) {
        MaternityResponse response = maternityService.getMaternityById(id);
        return ResponseEntity.ok(Api.OK(response));
    }
}
