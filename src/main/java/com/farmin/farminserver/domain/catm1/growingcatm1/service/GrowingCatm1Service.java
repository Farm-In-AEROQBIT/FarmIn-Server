package com.farmin.farminserver.domain.catm1.growingcatm1.service;

import com.farmin.farminserver.domain.catm1.growingcatm1.dto.GrowingCatm1Request;
import com.farmin.farminserver.domain.catm1.growingcatm1.dto.GrowingCatm1Response;

import java.util.List;

public interface GrowingCatm1Service {
    GrowingCatm1Response createGrowingCatm1(GrowingCatm1Request request);
    List<GrowingCatm1Response> getAllGrowingCatm1();
    GrowingCatm1Response getGrowingCatm1ById(int id);
    GrowingCatm1Response updateGrowingCatm1(int id, GrowingCatm1Request request);
    void deleteGrowingCatm1(int id);

    List<GrowingCatm1Response> getStatistics(String type, String year, String month, String weekOrDay);
}
