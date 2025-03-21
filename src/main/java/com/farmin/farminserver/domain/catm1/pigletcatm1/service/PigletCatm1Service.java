package com.farmin.farminserver.domain.catm1.pigletcatm1.service;

import com.farmin.farminserver.domain.catm1.pigletcatm1.dto.PigletCatm1Request;
import com.farmin.farminserver.domain.catm1.pigletcatm1.dto.PigletCatm1Response;

import java.util.List;

public interface PigletCatm1Service {
    PigletCatm1Response createPigletCatm1(PigletCatm1Request request);
    List<PigletCatm1Response> getAllPigletCatm1();
    PigletCatm1Response getPigletCatm1ById(int id);
    PigletCatm1Response updatePigletCatm1(int id, PigletCatm1Request request);
    void deletePigletCatm1(int id);

    List<PigletCatm1Response> getStatistics(String type, String year, String month, String weekOrDay);
}
