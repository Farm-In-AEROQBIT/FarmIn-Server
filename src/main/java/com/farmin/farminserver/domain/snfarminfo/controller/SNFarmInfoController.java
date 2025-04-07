package com.farmin.farminserver.domain.snfarminfo.controller;

import com.farmin.farminserver.domain.snfarminfo.dto.SNFarmInfoRequest;
import com.farmin.farminserver.domain.snfarminfo.dto.SNFarmInfoResponse;
import com.farmin.farminserver.domain.snfarminfo.service.SNFarmInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/snfarminfo")
public class SNFarmInfoController {

    private final SNFarmInfoService snFarmInfoService;

    @PostMapping
    public ResponseEntity<SNFarmInfoResponse> createSNFarmInfo(@RequestBody SNFarmInfoRequest request) {
        SNFarmInfoResponse response = snFarmInfoService.createSNFarmInfo(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{farmId}")
    public ResponseEntity<SNFarmInfoResponse> getSNFarmInfoByFarmId(@PathVariable int farmId) {
        SNFarmInfoResponse response = snFarmInfoService.getSNFarmInfoByFarmId(farmId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SNFarmInfoResponse>> getAllSNFarmInfos() {
        List<SNFarmInfoResponse> responses = snFarmInfoService.getAllSNFarmInfos();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{snFarmId}")
    public ResponseEntity<SNFarmInfoResponse> updateSNFarmInfo(@PathVariable String snFarmId, @RequestBody SNFarmInfoRequest request) {
        SNFarmInfoResponse response = snFarmInfoService.updateSNFarmInfo(snFarmId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{snFarmId}")
    public ResponseEntity<Void> deleteSNFarmInfo(@PathVariable String snFarmId) {
        snFarmInfoService.deleteSNFarmInfo(snFarmId);
        return ResponseEntity.noContent().build();
    }
}
