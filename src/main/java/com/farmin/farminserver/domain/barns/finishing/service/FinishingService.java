package com.farmin.farminserver.domain.barns.finishing.service;

import com.farmin.farminserver.domain.barns.finishing.dto.FinishingRequest;
import com.farmin.farminserver.domain.barns.finishing.dto.FinishingResponse;

import java.util.List;

public interface FinishingService {
    FinishingResponse createFinishing(FinishingRequest request);
    FinishingResponse getFinishingById(Integer id);
    List<FinishingResponse> getAllFinishing();
    void deleteFinishing(Integer id);
}
