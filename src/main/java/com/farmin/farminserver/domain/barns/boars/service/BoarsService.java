package com.farmin.farminserver.domain.barns.boars.service;

import com.farmin.farminserver.domain.barns.boars.dto.BoarsRequest;
import com.farmin.farminserver.domain.barns.boars.dto.BoarsResponse;

public interface BoarsService {
    BoarsResponse createBoars(BoarsRequest dto);
    BoarsResponse getBoarsById(Integer id);
}
