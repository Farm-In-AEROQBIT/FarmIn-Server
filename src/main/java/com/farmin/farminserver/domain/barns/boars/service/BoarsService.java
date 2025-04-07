package com.farmin.farminserver.domain.barns.boars.service;

import com.farmin.farminserver.domain.barns.boars.dto.BoarsRequest;
import com.farmin.farminserver.domain.barns.boars.dto.BoarsResponse;

import java.util.List;

public interface BoarsService {
    BoarsResponse createBoars(BoarsRequest dto);
    BoarsResponse getBoarsById(Integer id);
    List<BoarsResponse> getAllBoars();         // ✅ 추가
    void deleteBoarsById(Integer id);             // ✅ 추가
}
