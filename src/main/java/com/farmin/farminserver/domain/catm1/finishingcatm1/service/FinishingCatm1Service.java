package com.farmin.farminserver.domain.catm1.finishingcatm1.service;

import com.farmin.farminserver.domain.catm1.finishingcatm1.dto.FinishingCatm1Request;
import com.farmin.farminserver.domain.catm1.finishingcatm1.dto.FinishingCatm1Response;

import java.util.List;

public interface FinishingCatm1Service {
    FinishingCatm1Response createFinishingCatm1(FinishingCatm1Request request);
    List<FinishingCatm1Response> getAllFinishingCatm1();
    FinishingCatm1Response getFinishingCatm1ById(int id);
    FinishingCatm1Response updateFinishingCatm1(int id, FinishingCatm1Request request);
    void deleteFinishingCatm1(int id);

    List<FinishingCatm1Response> getStatistics(String type, String year, String month, String weekOrDay);
}
