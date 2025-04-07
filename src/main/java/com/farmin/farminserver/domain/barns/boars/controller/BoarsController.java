package com.farmin.farminserver.domain.barns.boars.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.boars.dto.BoarsRequest;
import com.farmin.farminserver.domain.barns.boars.dto.BoarsResponse;
import com.farmin.farminserver.domain.barns.boars.service.BoarsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // ✅ 전체 목록 조회
    @GetMapping
    public ResponseEntity<Api<List<BoarsResponse>>> getAllBoars() {
        List<BoarsResponse> responseList = boarsService.getAllBoars();
        return ResponseEntity.ok(Api.OK(responseList));
    }

    // ✅ 삭제 기능
    @DeleteMapping("/{id}")
    public ResponseEntity<Api<String>> deleteBoars(@PathVariable Integer id) {
        boarsService.deleteBoarsById(id);
        return ResponseEntity.ok(Api.OK("Deleted Boars with ID: " + id));
    }
}
