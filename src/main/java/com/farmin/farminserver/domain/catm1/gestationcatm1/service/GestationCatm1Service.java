package com.farmin.farminserver.domain.catm1.gestationcatm1.service;

import com.farmin.farminserver.domain.catm1.gestationcatm1.dto.GestationCatm1Request;
import com.farmin.farminserver.domain.catm1.gestationcatm1.dto.GestationCatm1Response;

import java.util.List;

public interface GestationCatm1Service {
    GestationCatm1Response createGestationCatm1(GestationCatm1Request request);
    List<GestationCatm1Response> getAllGestationCatm1();
    GestationCatm1Response getGestationCatm1ById(int id);
    GestationCatm1Response updateGestationCatm1(int id, GestationCatm1Request request);
    void deleteGestationCatm1(int id);

    List<GestationCatm1Response> getStatistics(String type, String year, String month, String weekOrDay);
}
