package com.farmin.farminserver.domain.barns.reserve.controller;

import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.domain.barns.reserve.dto.ReserveRequest;
import com.farmin.farminserver.domain.barns.reserve.dto.ReserveResponse;
import com.farmin.farminserver.domain.barns.reserve.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reserve")
public class ReserveController {
    private final ReserveService reserveService;

    @PostMapping
    public ResponseEntity<Api<ReserveResponse>> createReserve(@RequestBody ReserveRequest dto) {
        ReserveResponse response = reserveService.createReserve(dto);
        return ResponseEntity.ok(Api.OK(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Api<ReserveResponse>> getReserveById(@PathVariable Integer id) {
        ReserveResponse response = reserveService.getReserveById(id);
        return ResponseEntity.ok(Api.OK(response));
    }
}
