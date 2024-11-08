package com.farmin.farminserver.domain.barns.growing.service;

import com.farmin.farminserver.domain.barns.growing.dto.GrowingRequest;
import com.farmin.farminserver.domain.barns.growing.dto.GrowingResponse;

public interface GrowingService {
    GrowingResponse createGrowing(GrowingRequest dto);
    GrowingResponse getGrowingById(Integer id);
}
