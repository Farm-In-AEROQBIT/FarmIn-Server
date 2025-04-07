package com.farmin.farminserver.domain.snfarminfo.service;

import com.farmin.farminserver.domain.snfarminfo.dto.SNFarmInfoRequest;
import com.farmin.farminserver.domain.snfarminfo.dto.SNFarmInfoResponse;

import java.util.List;

public interface SNFarmInfoService {
    SNFarmInfoResponse createSNFarmInfo(SNFarmInfoRequest request);
    SNFarmInfoResponse getSNFarmInfoByFarmId(int farmId);
    List<SNFarmInfoResponse> getAllSNFarmInfos();
    SNFarmInfoResponse updateSNFarmInfo(String snFarmId, SNFarmInfoRequest request);
    void deleteSNFarmInfo(String snFarmId);


}

