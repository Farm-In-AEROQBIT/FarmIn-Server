package com.farmin.farminserver.domain.barns.gestation.service;

import com.farmin.farminserver.domain.barns.gestation.dto.GestationRequest;
import com.farmin.farminserver.domain.barns.gestation.dto.GestationResponse;

import java.util.List;

public interface GestationService {
    GestationResponse createGestation(GestationRequest dto);
    GestationResponse getGestationById(Integer id);
    List<GestationResponse> getAllGestation();
    void deleteGestationById(Integer id);
}
