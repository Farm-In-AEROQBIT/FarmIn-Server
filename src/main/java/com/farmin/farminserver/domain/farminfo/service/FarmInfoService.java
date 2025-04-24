package com.farmin.farminserver.domain.farminfo.service;

import com.farmin.farminserver.domain.farminfo.dto.FarmInfoRequest;
import com.farmin.farminserver.domain.farminfo.dto.FarmInfoResponse;

import java.util.List;

public interface FarmInfoService {
    FarmInfoResponse createFarmInfo(FarmInfoRequest request);
    List<FarmInfoResponse> getAllFarms();
    FarmInfoResponse getFarmById(int id);
    List<FarmInfoResponse> getFarmsByUserId(Integer userId);
    List<FarmInfoResponse> getFarmsByUsername(String username);
    FarmInfoResponse updateFarmInfo(int id, FarmInfoRequest request);
    void deleteFarmInfo(int id);
}