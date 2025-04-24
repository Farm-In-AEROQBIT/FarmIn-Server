package com.farmin.farminserver.domain.farmsensor.service;

import com.farmin.farminserver.common.error.ErrorCode;
import com.farmin.farminserver.common.exception.ApiException;
import com.farmin.farminserver.domain.farmsensor.dto.FarmSectionResponse;
import com.farmin.farminserver.domain.farmsensor.dto.SectionSensorResponse;
import com.farmin.farminserver.entity.barns.boars.BoarsRepository;
import com.farmin.farminserver.entity.barns.finishing.FinishingRepository;
import com.farmin.farminserver.entity.barns.gestation.GestationRepository;
import com.farmin.farminserver.entity.barns.growing.GrowingRepository;
import com.farmin.farminserver.entity.barns.maternity.MaternityRepository;
import com.farmin.farminserver.entity.barns.piglet.PigletRepository;
import com.farmin.farminserver.entity.barns.reserve.ReserveRepository;
import com.farmin.farminserver.entity.catm1.boarscatm1sensor.BoarsCatm1Repository;
import com.farmin.farminserver.entity.catm1.finishingcatm1sensor.FinishingCatm1Repository;
import com.farmin.farminserver.entity.catm1.gestationcatm1sensor.GestationCatm1Repository;
import com.farmin.farminserver.entity.catm1.growingcatm1sensor.GrowingCatm1Repository;
import com.farmin.farminserver.entity.catm1.maternitycatm1sensor.MaternityCatm1Repository;
import com.farmin.farminserver.entity.catm1.pigletcatm1sensor.PigletCatm1Repository;
import com.farmin.farminserver.entity.catm1.reservecatm1sensor.ReserveCatm1Repository;
import com.farmin.farminserver.entity.farminfo.FarmInfoEntity;
import com.farmin.farminserver.entity.farminfo.FarmInfoRepository;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoEntity;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoRepository;
import com.farmin.farminserver.entity.user.UserEntity;
import com.farmin.farminserver.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmSensorServiceImpl implements FarmSensorService {

    private final UserRepository userRepository;
    private final FarmInfoRepository farmInfoRepository;
    private final SNFarmInfoRepository snFarmInfoRepository;

    // Section repositories
    private final BoarsRepository boarsRepository;
    private final FinishingRepository finishingRepository;
    private final GestationRepository gestationRepository;
    private final GrowingRepository growingRepository;
    private final MaternityRepository maternityRepository;
    private final PigletRepository pigletRepository;
    private final ReserveRepository reserveRepository;

    // CATM1 sensor repositories only
    private final BoarsCatm1Repository boarsCatm1Repository;
    private final FinishingCatm1Repository finishingCatm1Repository;
    private final GestationCatm1Repository gestationCatm1Repository;
    private final GrowingCatm1Repository growingCatm1Repository;
    private final MaternityCatm1Repository maternityCatm1Repository;
    private final PigletCatm1Repository pigletCatm1Repository;
    private final ReserveCatm1Repository reserveCatm1Repository;

    @Override
    public List<FarmSectionResponse> getFarmsByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "해당 사용자를 찾을 수 없습니다.");
        }

        Integer userId = user.getId().intValue();
        List<FarmInfoEntity> farms = farmInfoRepository.findByUserId(userId);

        List<FarmSectionResponse> result = new ArrayList<>();

        for (FarmInfoEntity farm : farms) {
            // 각 농장에 대한 모든 SNFarmInfo 조회
            List<SNFarmInfoEntity> snFarms = snFarmInfoRepository.findByFarmId(farm.getFarmId());

            if (snFarms.isEmpty()) {
                // SNFarmInfo가 없는 경우, 기본 Farm 정보만 포함
                result.add(FarmSectionResponse.builder()
                        .id(farm.getFarmId())
                        .name(farm.getFarmName())
                        .type("farm")
                        .snFarmId(null)
                        .build());
            } else {
                // 각 SNFarmInfo에 대해 응답 생성
                for (SNFarmInfoEntity snFarm : snFarms) {
                    result.add(FarmSectionResponse.builder()
                            .id(farm.getFarmId())
                            .name(farm.getFarmName())
                            .type("farm")
                            .snFarmId(snFarm.getSnFarmId())
                            .build());
                }
            }
        }

        return result;
    }

    @Override
    public List<FarmSectionResponse> getSectionsByFarmId(Integer farmId) {
        // First, check if farm exists
        FarmInfoEntity farm = farmInfoRepository.findById(farmId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 농장을 찾을 수 없습니다."));

        // Get all SNFarmInfo entries for this farm
        List<SNFarmInfoEntity> snFarms = snFarmInfoRepository.findByFarmId(farmId);
        if (snFarms.isEmpty()) {
            return new ArrayList<>();
        }

        List<FarmSectionResponse> sections = new ArrayList<>();

        // For each SNFarm, collect all section types
        for (SNFarmInfoEntity snFarm : snFarms) {
            String snFarmId = snFarm.getSnFarmId();

            // Check each section type and add if exists
            boarsRepository.findBySnFarmId(snFarmId).ifPresent(boars ->
                    sections.add(FarmSectionResponse.builder()
                            .id(boars.getBoarsId())
                            .name("Boars")
                            .type("boars")
                            .snFarmId(snFarmId)
                            .build())
            );

            finishingRepository.findBySnFarmId(snFarmId).ifPresent(finishing ->
                    sections.add(FarmSectionResponse.builder()
                            .id(finishing.getFinishingId())
                            .name("Finishing")
                            .type("finishing")
                            .snFarmId(snFarmId)
                            .build())
            );

            gestationRepository.findBySnFarmId(snFarmId).ifPresent(gestation ->
                    sections.add(FarmSectionResponse.builder()
                            .id(gestation.getGestationId())
                            .name("Gestation")
                            .type("gestation")
                            .snFarmId(snFarmId)
                            .build())
            );

            growingRepository.findBySnFarmId(snFarmId).ifPresent(growing ->
                    sections.add(FarmSectionResponse.builder()
                            .id(growing.getGrowingId())
                            .name("Growing")
                            .type("growing")
                            .snFarmId(snFarmId)
                            .build())
            );

            maternityRepository.findBySnFarmId(snFarmId).ifPresent(maternity ->
                    sections.add(FarmSectionResponse.builder()
                            .id(maternity.getMaternityId())
                            .name("Maternity")
                            .type("maternity")
                            .snFarmId(snFarmId)
                            .build())
            );

            pigletRepository.findBySnFarmId(snFarmId).ifPresent(piglet ->
                    sections.add(FarmSectionResponse.builder()
                            .id(piglet.getPigletId())
                            .name("Piglet")
                            .type("piglet")
                            .snFarmId(snFarmId)
                            .build())
            );

            reserveRepository.findBySnFarmId(snFarmId).ifPresent(reserve ->
                    sections.add(FarmSectionResponse.builder()
                            .id(reserve.getReserveId())
                            .name("Reserve")
                            .type("reserve")
                            .snFarmId(snFarmId)
                            .build())
            );
        }

        return sections;
    }

    @Override
    public List<SectionSensorResponse> getSensorsBySection(Integer sectionId, String sectionType, String sensorType) {
        // Always use "catm1" as the sensor type since we're only using Catm1 sensors now
        return getSensorsBySectionCatm1(sectionId, sectionType);
    }

    private List<SectionSensorResponse> getSensorsBySectionCatm1(Integer sectionId, String sectionType) {
        switch (sectionType.toLowerCase()) {
            case "boars":
                return boarsCatm1Repository.findByBoarsId(sectionId).stream()
                        .map(sensor -> SectionSensorResponse.builder()
                                .sensorId(sensor.getSensorIdc())
                                .sectionId(sectionId)
                                .sectionType(sectionType)
                                .sensorType("catm1")
                                .co2(sensor.getCo2())
                                .temperature(sensor.getTemper())
                                .humidity(sensor.getHumidity())
                                .waterTemperature(sensor.getWtemper())
                                .timestamp(sensor.getTime())
                                .build())
                        .collect(Collectors.toList());

            case "finishing":
                return finishingCatm1Repository.findByFinishingId(sectionId).stream()
                        .map(sensor -> SectionSensorResponse.builder()
                                .sensorId(sensor.getSensorIdc())
                                .sectionId(sectionId)
                                .sectionType(sectionType)
                                .sensorType("catm1")
                                .co2(sensor.getCo2())
                                .temperature(sensor.getTemper())
                                .humidity(sensor.getHumidity())
                                .waterTemperature(sensor.getWtemper())
                                .timestamp(sensor.getTime())
                                .build())
                        .collect(Collectors.toList());

            case "gestation":
                return gestationCatm1Repository.findByGestationId(sectionId).stream()
                        .map(sensor -> SectionSensorResponse.builder()
                                .sensorId(sensor.getSensorIdc())
                                .sectionId(sectionId)
                                .sectionType(sectionType)
                                .sensorType("catm1")
                                .co2(sensor.getCo2())
                                .temperature(sensor.getTemper())
                                .humidity(sensor.getHumidity())
                                .waterTemperature(sensor.getWtemper())
                                .timestamp(sensor.getTime())
                                .build())
                        .collect(Collectors.toList());

            case "growing":
                return growingCatm1Repository.findByGrowingId(sectionId).stream()
                        .map(sensor -> SectionSensorResponse.builder()
                                .sensorId(sensor.getSensorIdc())
                                .sectionId(sectionId)
                                .sectionType(sectionType)
                                .sensorType("catm1")
                                .co2(sensor.getCo2())
                                .temperature(sensor.getTemper())
                                .humidity(sensor.getHumidity())
                                .waterTemperature(sensor.getWtemper())
                                .timestamp(sensor.getTime())
                                .build())
                        .collect(Collectors.toList());

            case "maternity":
                return maternityCatm1Repository.findByMaternityId(sectionId).stream()
                        .map(sensor -> SectionSensorResponse.builder()
                                .sensorId(sensor.getSensorIdc())
                                .sectionId(sectionId)
                                .sectionType(sectionType)
                                .sensorType("catm1")
                                .co2(sensor.getCo2())
                                .temperature(sensor.getTemper())
                                .humidity(sensor.getHumidity())
                                .waterTemperature(sensor.getWtemper())
                                .timestamp(sensor.getTime())
                                .build())
                        .collect(Collectors.toList());

            case "piglet":
                return pigletCatm1Repository.findByPigletId(sectionId).stream()
                        .map(sensor -> SectionSensorResponse.builder()
                                .sensorId(sensor.getSensorIdc())
                                .sectionId(sectionId)
                                .sectionType(sectionType)
                                .sensorType("catm1")
                                .co2(sensor.getCo2())
                                .temperature(sensor.getTemper())
                                .humidity(sensor.getHumidity())
                                .waterTemperature(sensor.getWtemper())
                                .timestamp(sensor.getTime())
                                .build())
                        .collect(Collectors.toList());

            case "reserve":
                return reserveCatm1Repository.findByReserveId(sectionId).stream()
                        .map(sensor -> SectionSensorResponse.builder()
                                .sensorId(sensor.getSensorIdc())
                                .sectionId(sectionId)
                                .sectionType(sectionType)
                                .sensorType("catm1")
                                .co2(sensor.getCo2())
                                .temperature(sensor.getTemper())
                                .humidity(sensor.getHumidity())
                                .waterTemperature(sensor.getWtemper())
                                .timestamp(sensor.getTime())
                                .build())
                        .collect(Collectors.toList());

            default:
                throw new ApiException(ErrorCode.BAD_REQUEST, "지원하지 않는 섹션 유형입니다: " + sectionType);
        }
    }
}