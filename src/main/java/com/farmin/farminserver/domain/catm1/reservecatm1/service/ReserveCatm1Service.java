package com.farmin.farminserver.domain.catm1.reservecatm1.service;

import com.farmin.farminserver.domain.catm1.reservecatm1.dto.ReserveCatm1Request;
import com.farmin.farminserver.domain.catm1.reservecatm1.dto.ReserveCatm1Response;

import java.util.List;

public interface ReserveCatm1Service {
    ReserveCatm1Response createReserveCatm1(ReserveCatm1Request request);
    List<ReserveCatm1Response> getAllReserveCatm1();
    ReserveCatm1Response getReserveCatm1ById(int id);
    ReserveCatm1Response updateReserveCatm1(int id, ReserveCatm1Request request);
    void deleteReserveCatm1(int id);

    List<ReserveCatm1Response> getStatistics(String type, String year, String month, String weekOrDay);
}
