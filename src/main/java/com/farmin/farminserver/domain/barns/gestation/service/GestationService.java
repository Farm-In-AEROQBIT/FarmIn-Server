package com.farmin.farminserver.domain.barns.gestation.service;

import com.farmin.farminserver.domain.barns.gestation.dto.GestationRequest;
import com.farmin.farminserver.domain.barns.gestation.dto.GestationResponse;

public interface GestationService {
    GestationResponse createGestation(GestationRequest dto);
    GestationResponse getGestationById(Integer id);
}
