package com.farmin.farminserver.domain.barns.maternity.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.maternity.dto.MaternityRequest;
import com.farmin.farminserver.domain.barns.maternity.dto.MaternityResponse;
import com.farmin.farminserver.domain.barns.maternity.service.MaternityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maternity")
@RequiredArgsConstructor
public class MaternityController {

    private final MaternityService maternityService;

    @PostMapping
    public ResponseEntity<Api<MaternityResponse>> create(@RequestBody MaternityRequest dto) {
        return ResponseEntity.ok(Api.OK(maternityService.createMaternity(dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<MaternityResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(Api.OK(maternityService.getMaternityById(id)));
    }

    @GetMapping
    public ResponseEntity<Api<List<MaternityResponse>>> getAll() {
        return ResponseEntity.ok(Api.OK(maternityService.getAllMaternities()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Api<String>> deleteById(@PathVariable Integer id) {
        maternityService.deleteMaternityById(id);
        return ResponseEntity.ok(Api.OK("Deleted Maternity with ID: " + id));
    }
}
