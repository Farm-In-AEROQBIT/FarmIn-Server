package com.farmin.farminserver.domain.barns.reserve.service;

import com.farmin.farminserver.domain.barns.reserve.dto.ReserveRequest;
import com.farmin.farminserver.domain.barns.reserve.dto.ReserveResponse;

public interface ReserveService {
    ReserveResponse createReserve(ReserveRequest dto);
    ReserveResponse getReserveById(Integer id);
}
