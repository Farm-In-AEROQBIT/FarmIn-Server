package com.farmin.farminserver.domain.catm1.maternitycatm1.service;

import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1Request;
import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1Response;

import java.util.List;

public interface MaternityCatm1Service {
    MaternityCatm1Response createMaternityCatm1(MaternityCatm1Request request);
    List<MaternityCatm1Response> getAllMaternityCatm1();
    MaternityCatm1Response getMaternityCatm1ById(int id);
    MaternityCatm1Response updateMaternityCatm1(int id, MaternityCatm1Request request);
    void deleteMaternityCatm1(int id);

    List<MaternityCatm1Response> getStatistics(String type, String year, String month, String weekOrDay);
}
