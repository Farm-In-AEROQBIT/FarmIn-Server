package com.farmin.farminserver.domain.catm1.finishingcatm1.controller;

import com.farmin.farminserver.domain.catm1.finishingcatm1.dto.FinishingCatm1Request;
import com.farmin.farminserver.domain.catm1.finishingcatm1.dto.FinishingCatm1Response;
import com.farmin.farminserver.domain.catm1.finishingcatm1.service.FinishingCatm1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/finishingcatm1")
public class FinishingCatm1Controller {

    private final FinishingCatm1Service finishingCatm1Service;

//    @PostMapping
//    public ResponseEntity<FinishingCatm1Response> createFinishingCatm1(@RequestBody FinishingCatm1Request request) {
//        FinishingCatm1Response response = finishingCatm1Service.createFinishingCatm1(request);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping
    public ResponseEntity<FinishingCatm1Response> create(@RequestBody FinishingCatm1Request request) {
        return ResponseEntity.ok(finishingCatm1Service.createFinishingCatm1(request));
    }

    @GetMapping
    public ResponseEntity<List<FinishingCatm1Response>> getAllFinishingCatm1() {
        return ResponseEntity.ok(finishingCatm1Service.getAllFinishingCatm1());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinishingCatm1Response> getFinishingCatm1ById(@PathVariable Integer id) {
        return ResponseEntity.ok(finishingCatm1Service.getFinishingCatm1ById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinishingCatm1Response> update(@PathVariable Integer id, @RequestBody FinishingCatm1Request request) {
        return ResponseEntity.ok(finishingCatm1Service.updateFinishingCatm1(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        finishingCatm1Service.deleteFinishingCatm1(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<FinishingCatm1Response>> getStatistics(
            @RequestParam(defaultValue = "yearly") String type,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String weekOrDay
    ) {
        List<FinishingCatm1Response> responses = finishingCatm1Service.getStatistics(type, year, month, weekOrDay);
        return ResponseEntity.ok(responses);
    }
}
