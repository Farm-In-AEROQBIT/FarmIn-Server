package com.farmin.farminserver.domain.barns.finishing.service;

import com.farmin.farminserver.domain.barns.finishing.dto.FinishingRequest;
import com.farmin.farminserver.domain.barns.finishing.dto.FinishingResponse;

public interface FinishingService {
    FinishingResponse createFinishing(FinishingRequest dto);
    FinishingResponse getFinishingById(Integer id);
}
