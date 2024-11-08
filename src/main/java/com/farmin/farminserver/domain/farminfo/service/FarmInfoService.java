package com.farmin.farminserver.domain.farminfo.service;

import com.farmin.farminserver.domain.farminfo.dto.FarmInfoRequest;
import com.farmin.farminserver.domain.farminfo.dto.FarmInfoResponse;

import java.util.List;

public interface FarmInfoService {
    FarmInfoResponse createFarmInfo(FarmInfoRequest request);
    List<FarmInfoResponse> getAllFarms();
    FarmInfoResponse getFarmById(int id);
}
