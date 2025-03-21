package com.farmin.farminserver.domain.catm1.boarscatm1.service;

import com.farmin.farminserver.domain.catm1.boarscatm1.dto.BoarsCatm1Request;
import com.farmin.farminserver.domain.catm1.boarscatm1.dto.BoarsCatm1Response;

import java.util.List;

public interface BoarsCatm1Service {
    BoarsCatm1Response createBoarsCatm1(BoarsCatm1Request request);
    List<BoarsCatm1Response> getAllBoarsCatm1();
    BoarsCatm1Response getBoarsCatm1ById(int id);
    BoarsCatm1Response updateBoarsCatm1(int id, BoarsCatm1Request request);
    void deleteBoarsCatm1(int id);

    List<BoarsCatm1Response> getStatistics(String type, String year, String month, String weekOrDay);
}
