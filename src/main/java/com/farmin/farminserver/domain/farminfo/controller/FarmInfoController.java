package com.farmin.farminserver.domain.farminfo.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.common.exception.ApiException;
import com.farmin.farminserver.domain.farminfo.dto.FarmInfoRequest;
import com.farmin.farminserver.domain.farminfo.dto.FarmInfoResponse;
import com.farmin.farminserver.domain.farminfo.service.FarmInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farm-info")
@RequiredArgsConstructor
public class FarmInfoController {

    private final FarmInfoService farmInfoService;

    @PostMapping("/joinfarm")
    public Api<FarmInfoResponse> createFarm(/*@Valid*/ @RequestBody FarmInfoRequest request) {
        System.out.println("Received request data: " + request);
        return Api.OK(farmInfoService.createFarmInfo(request));
    }


    @GetMapping
    public Api<List<FarmInfoResponse>> getAllFarms() {
        return Api.OK(farmInfoService.getAllFarms());
    }

    @GetMapping("/{id}")
    public Api<FarmInfoResponse> getFarmById(@PathVariable int id) {
        return Api.OK(farmInfoService.getFarmById(id));
    }

    @PutMapping("/{id}")
    public Api<FarmInfoResponse> updateFarm(@PathVariable int id, @Valid @RequestBody FarmInfoRequest request) {
        return Api.OK(farmInfoService.updateFarmInfo(id, request));
    }

    @DeleteMapping("/{id}")
    public Api<Void> deleteFarm(@PathVariable int id) {
        farmInfoService.deleteFarmInfo(id);
        return Api.OK(null);
    }


}
