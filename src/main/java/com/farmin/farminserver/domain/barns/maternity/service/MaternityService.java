package com.farmin.farminserver.domain.barns.maternity.service;

import com.farmin.farminserver.domain.barns.maternity.dto.MaternityRequest;
import com.farmin.farminserver.domain.barns.maternity.dto.MaternityResponse;

import java.util.List;

public interface MaternityService {
    MaternityResponse createMaternity(MaternityRequest dto);
    MaternityResponse getMaternityById(Integer id);
    List<MaternityResponse> getAllMaternities();
    void deleteMaternityById(Integer id);
}
