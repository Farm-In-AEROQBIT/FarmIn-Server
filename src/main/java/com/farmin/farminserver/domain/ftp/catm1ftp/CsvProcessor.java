package com.farmin.farminserver.domain.ftp.catm1ftp;

import com.farmin.farminserver.domain.catm1.boarscatm1.dto.BoarsCatm1Request;
import com.farmin.farminserver.domain.catm1.boarscatm1.service.BoarsCatm1Service;
import com.farmin.farminserver.domain.catm1.finishingcatm1.dto.FinishingCatm1Request;
import com.farmin.farminserver.domain.catm1.finishingcatm1.service.FinishingCatm1Service;
import com.farmin.farminserver.domain.catm1.gestationcatm1.dto.GestationCatm1Request;
import com.farmin.farminserver.domain.catm1.gestationcatm1.service.GestationCatm1Service;
import com.farmin.farminserver.domain.catm1.growingcatm1.dto.GrowingCatm1Request;
import com.farmin.farminserver.domain.catm1.growingcatm1.service.GrowingCatm1Service;
import com.farmin.farminserver.domain.catm1.maternitycatm1.dto.MaternityCatm1Request;
import com.farmin.farminserver.domain.catm1.maternitycatm1.service.MaternityCatm1Service;
import com.farmin.farminserver.domain.catm1.pigletcatm1.dto.PigletCatm1Request;
import com.farmin.farminserver.domain.catm1.pigletcatm1.service.PigletCatm1Service;
import com.farmin.farminserver.domain.catm1.reservecatm1.dto.ReserveCatm1Request;
import com.farmin.farminserver.domain.catm1.reservecatm1.service.ReserveCatm1Service;
import com.farmin.farminserver.entity.barns.boars.BoarsRepository;
import com.farmin.farminserver.entity.barns.finishing.FinishingRepository;
import com.farmin.farminserver.entity.barns.gestation.GestationRepository;
import com.farmin.farminserver.entity.barns.growing.GrowingRepository;
import com.farmin.farminserver.entity.barns.maternity.MaternityRepository;
import com.farmin.farminserver.entity.barns.piglet.PigletRepository;
import com.farmin.farminserver.entity.barns.reserve.ReserveRepository;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoEntity;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CsvProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CsvProcessor.class);

    // SNFarmID 추출 패턴들
    private static final Pattern FARM_ID_PATTERN = Pattern.compile("farmin(\\d+)");
    private static final Pattern FILE_ID_PATTERN = Pattern.compile("(\\d+)-\\d+\\.csv$");

    private final SNFarmInfoRepository snFarmInfoRepository;
    private final BoarsRepository boarsRepository;
    private final BoarsCatm1Service boarsCatm1Service;
    private final FinishingRepository finishingRepository;
    private final FinishingCatm1Service finishingCatm1Service;
    private final GestationRepository gestationRepository;
    private final GestationCatm1Service gestationCatm1Service;
    private final GrowingRepository growingRepository;
    private final GrowingCatm1Service growingCatm1Service;
    private final MaternityRepository maternityRepository;
    private final MaternityCatm1Service maternityCatm1Service;
    private final PigletRepository pigletRepository;
    private final PigletCatm1Service pigletCatm1Service;
    private final ReserveRepository reserveRepository;
    private final ReserveCatm1Service reserveCatm1Service;

    // 이미 처리된 파일을 기록하기 위한 Map (날짜별로 관리)
    private final Map<String, Boolean> processedFiles = new HashMap<>();
    // 서버 시작 시간을 저장
    private LocalDate serverStartDate = LocalDate.now();
    // 서버 시작 후 첫 실행 여부 플래그
    private boolean isFirstRun = true;

    // SNFarmID가 있는 축사 유형 찾기 (개선된 방식)
    private enum BarnType {
        BOARS, FINISHING, GESTATION, GROWING, MATERNITY, PIGLET, RESERVE, UNKNOWN
    }

    public void processCSVFile(Path csvFilePath) {
        // 파일명에서 날짜 추출
        String fileDate = extractDateFromFileName(csvFilePath.getFileName().toString());
        LocalDate currentDate = LocalDate.now();

        // 파일 처리 여부 결정
        if (!shouldProcessFile(csvFilePath, fileDate, currentDate)) {
            return;
        }

        // 전체 경로에서 SNFarmID 추출 시도
        String snFarmId = extractSnFarmIdFromPath(csvFilePath);

        // 추출 실패 시 로그 기록 후 종료
        if (snFarmId == null) {
            logger.error("[CSV] SNFarmID 추출 실패: {}", csvFilePath);
            return;
        }

        // SNFarmID 정보 확인
        SNFarmInfoEntity snFarmInfo = snFarmInfoRepository.findById(snFarmId).orElse(null);
        if (snFarmInfo == null) {
            logger.error("[CSV] SNFarmID에 해당하는 Farm 정보 없음: {}", snFarmId);
            return;
        }

        // SNFarmID가 어떤 축사 유형에 속하는지 확인
        BarnType barnType = determineBarnType(snFarmId);
        if (barnType == BarnType.UNKNOWN) {
            logger.error("[CSV] SNFarmID에 해당하는 축사 유형을 찾을 수 없음: {}", snFarmId);
            return;
        }

        // 해당 축사 유형으로 처리
        logger.info("[CSV] 파일 처리 시작: {}, SNFarmID: {}, BarnType: {}", csvFilePath, snFarmId, barnType);
        processFileWithBarnType(csvFilePath, snFarmId, barnType);

        // 처리 완료된 파일 기록
        markFileAsProcessed(csvFilePath.getFileName().toString(), fileDate);
    }

    // 파일명에서 날짜 추출
    private String extractDateFromFileName(String fileName) {
        try {
            String[] parts = fileName.split("-");
            if (parts.length < 2) {
                return null;
            }
            return parts[1].split("\\.")[0].substring(0, 6); // "YYMMDD" 형식으로 추출
        } catch (Exception e) {
            logger.error("[CSV] 파일명에서 날짜 추출 실패: {}", fileName, e);
            return null;
        }
    }

    // 파일 처리 여부 결정 메서드
    private boolean shouldProcessFile(Path csvFilePath, String fileDate, LocalDate currentDate) {
        String fileName = csvFilePath.getFileName().toString();

        // 날짜 추출 실패 시
        if (fileDate == null) {
            logger.warn("[CSV] 파일명에서 날짜 추출 실패, 기본 처리 진행: {}", fileName);
            return true;
        }

        // 현재 날짜와 파일 날짜 비교
        LocalDate fileLocalDate = parseFileDate(fileDate);
        if (fileLocalDate == null) {
            return true; // 날짜 변환 실패 시 기본값으로 처리
        }

        // 날짜가 오늘이 아닌 경우: 서버 시작 후 최초 1회만 처리
        if (!fileLocalDate.isEqual(currentDate)) {
            if (isFirstRun) {
                // 처리된 적이 없는지 확인
                if (isFileAlreadyProcessed(fileName, fileDate)) {
                    logger.info("[CSV] 이전에 처리된 파일이므로 건너뜀: {}", fileName);
                    return false;
                }
                return true; // 서버 시작 후 첫 실행이면서 처리된 적 없는 파일
            } else {
                logger.info("[CSV] 오늘 날짜가 아닌 파일이며 서버 시작 후 첫 실행이 아님: {}", fileName);
                return false; // 최초 실행이 아니면 처리하지 않음
            }
        }

        // 오늘 날짜 파일의 경우: 이미 처리된 파일인지 확인
        if (isFileAlreadyProcessed(fileName, fileDate)) {
            logger.info("[CSV] 이미 처리된 파일: {}", fileName);
            return false;
        }

        return true;
    }

    // 파일 날짜 문자열을 LocalDate로 변환
    private LocalDate parseFileDate(String fileDate) {
        try {
            // "YYMMDD" -> LocalDate
            int year = 2000 + Integer.parseInt(fileDate.substring(0, 2));
            int month = Integer.parseInt(fileDate.substring(2, 4));
            int day = Integer.parseInt(fileDate.substring(4, 6));
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            logger.error("[CSV] 파일 날짜 변환 오류: {}", fileDate, e);
            return null;
        }
    }

    // 파일이 이미 처리되었는지 확인
    private boolean isFileAlreadyProcessed(String fileName, String fileDate) {
        return processedFiles.containsKey(fileDate + "-" + fileName);
    }

    // 처리된 파일 표시
    private void markFileAsProcessed(String fileName, String fileDate) {
        processedFiles.put(fileDate + "-" + fileName, true);

        // 첫 번째 실행이 끝났음을 표시
        if (isFirstRun) {
            isFirstRun = false;
            logger.info("[CSV] 서버 시작 후 첫 번째 처리 완료");
        }
    }

    // SNFarmID가 어떤 축사 유형에 속하는지 확인하는 메서드
    private BarnType determineBarnType(String snFarmId) {
        if (boarsRepository.findBySnFarmId(snFarmId).isPresent()) {
            return BarnType.BOARS;
        } else if (finishingRepository.findBySnFarmId(snFarmId).isPresent()) {
            return BarnType.FINISHING;
        } else if (gestationRepository.findBySnFarmId(snFarmId).isPresent()) {
            return BarnType.GESTATION;
        } else if (growingRepository.findBySnFarmId(snFarmId).isPresent()) {
            return BarnType.GROWING;
        } else if (maternityRepository.findBySnFarmId(snFarmId).isPresent()) {
            return BarnType.MATERNITY;
        } else if (pigletRepository.findBySnFarmId(snFarmId).isPresent()) {
            return BarnType.PIGLET;
        } else if (reserveRepository.findBySnFarmId(snFarmId).isPresent()) {
            return BarnType.RESERVE;
        } else {
            return BarnType.UNKNOWN;
        }
    }

    // 특정 축사 유형으로 파일 처리
    private void processFileWithBarnType(Path csvFilePath, String snFarmId, BarnType barnType) {
        int processedLines = 0;
        int errorLines = 0;
        List<String[]> dataLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath.toFile()))) {
            String line;
            int lineNum = 0;

            // 파일 내용 읽기
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length < 5) {
                    logger.warn("[CSV] 라인 {} - 데이터 형식 오류 (5개 미만): {}", lineNum, line);
                    errorLines++;
                    continue;
                }

                dataLines.add(data);
            }

            // 한 번에 처리 (최적화)
            for (String[] data : dataLines) {
                try {
                    processDataForBarnType(data, snFarmId, barnType, csvFilePath);
                    processedLines++;
                } catch (Exception e) {
                    logger.error("[CSV] 데이터 처리 중 오류: {}", e.getMessage(), e);
                    errorLines++;
                }
            }

            logger.info("[CSV] 파일 처리 완료: {}, 처리된 라인: {}, 오류 라인: {}", csvFilePath, processedLines, errorLines);
        } catch (IOException e) {
            logger.error("[CSV] 파일 읽기 오류: {}", csvFilePath, e);
        }
    }

    // 특정 축사 유형으로 데이터 처리
    private void processDataForBarnType(String[] data, String snFarmId, BarnType barnType, Path csvFilePath) {
        switch (barnType) {
            case BOARS -> processBoars(data, snFarmId, csvFilePath);
            case FINISHING -> processFinishing(data, snFarmId, csvFilePath);
            case GESTATION -> processGestation(data, snFarmId, csvFilePath);
            case GROWING -> processGrowing(data, snFarmId, csvFilePath);
            case MATERNITY -> processMaternity(data, snFarmId, csvFilePath);
            case PIGLET -> processPiglet(data, snFarmId, csvFilePath);
            case RESERVE -> processReserve(data, snFarmId, csvFilePath);
            default -> throw new IllegalArgumentException("지원되지 않는 축사 유형: " + barnType);
        }
    }

    private String extractSnFarmIdFromPath(Path path) {
        try {
            // 1. 파일의 전체 경로를 문자열로 변환
            String fullPath = path.toString();

            // 2. 경로에서 "farmin숫자" 패턴 찾기
            Matcher farmIdMatcher = FARM_ID_PATTERN.matcher(fullPath);
            if (farmIdMatcher.find()) {
                return farmIdMatcher.group(1); // 숫자 부분만 반환
            }

            // 3. 파일명에서 "숫자-숫자.csv" 패턴 찾기
            String fileName = path.getFileName().toString();
            Matcher fileIdMatcher = FILE_ID_PATTERN.matcher(fileName);
            if (fileIdMatcher.find()) {
                String fileIdPart = fileIdMatcher.group(1);
                // 앞 부분이 3자리 이상이면 뒤 3~4자리만 추출 (예: 320690 -> 0690 또는 690)
                if (fileIdPart.length() > 4) {
                    return fileIdPart.substring(Math.max(0, fileIdPart.length() - 4));
                }
                return fileIdPart;
            }

            // 4. 경로의 각 부분을 개별적으로 검사 (디렉토리 구조 세부 검사)
            for (Path part : path) {
                String partStr = part.toString();
                Matcher partMatcher = FARM_ID_PATTERN.matcher(partStr);
                if (partMatcher.find()) {
                    return partMatcher.group(1);
                }
            }

            // 5. 파일명에서 첫 번째 숫자 그룹 추출 (예: 320690-250321.csv)
            if (fileName.contains("-")) {
                String[] parts = fileName.split("-");
                if (parts.length > 0 && parts[0].matches("\\d+")) {
                    // 숫자만 있는 경우 뒤 3~4자리 추출
                    String potentialId = parts[0];
                    if (potentialId.length() > 4) {
                        return potentialId.substring(Math.max(0, potentialId.length() - 4));
                    }
                    return potentialId;
                }
            }

            // 6. 디렉토리명에서 추가 검사 - 직계 상위 디렉토리 확인
            Path parent = path.getParent();
            if (parent != null) {
                String parentName = parent.getFileName().toString();
                // 숫자로만 구성된 디렉토리명 확인
                if (parentName.matches("\\d+")) {
                    return parentName;
                }
            }

            logger.error("[CSV] SNFarmID 추출 실패: {}", fullPath);
            return null;
        } catch (Exception e) {
            logger.error("[CSV] SNFarmID 추출 중 오류 발생: {}", path, e);
            return null;
        }
    }

    // 각 축사별 처리 메서드
    private void processBoars(String[] data, String snFarmId, Path csvFilePath) {
        var barn = boarsRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Boars 정보를 찾을 수 없음: {}", snFarmId);
            return;
        }

        var req = new BoarsCatm1Request();
        req.setBoarsID(String.valueOf(barn.getBoarsId()));
        fillSensorData(req, data, csvFilePath);
        boarsCatm1Service.createBoarsCatm1(req);
    }

    private void processFinishing(String[] data, String snFarmId, Path csvFilePath) {
        var barn = finishingRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Finishing 정보를 찾을 수 없음: {}", snFarmId);
            return;
        }

        var req = new FinishingCatm1Request();
        req.setFinishingID(barn.getFinishingId());
        fillSensorData(req, data, csvFilePath);
        finishingCatm1Service.createFinishingCatm1(req);
    }

    private void processGestation(String[] data, String snFarmId, Path csvFilePath) {
        var barn = gestationRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Gestation 정보를 찾을 수 없음: {}", snFarmId);
            return;
        }

        var req = new GestationCatm1Request();
        req.setGestationID(barn.getGestationId());
        fillSensorData(req, data, csvFilePath);
        gestationCatm1Service.createGestationCatm1(req);
    }

    private void processGrowing(String[] data, String snFarmId, Path csvFilePath) {
        var barn = growingRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Growing 정보를 찾을 수 없음: {}", snFarmId);
            return;
        }

        var req = new GrowingCatm1Request();
        req.setGrowingID(barn.getGrowingId());
        fillSensorData(req, data, csvFilePath);
        growingCatm1Service.createGrowingCatm1(req);
    }

    private void processMaternity(String[] data, String snFarmId, Path csvFilePath) {
        var barn = maternityRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Maternity 정보를 찾을 수 없음: {}", snFarmId);
            return;
        }

        var req = new MaternityCatm1Request();
        req.setMaternityID(barn.getMaternityId());
        fillSensorData(req, data, csvFilePath);
        maternityCatm1Service.createMaternityCatm1(req);
    }

    private void processPiglet(String[] data, String snFarmId, Path csvFilePath) {
        var barn = pigletRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Piglet 정보를 찾을 수 없음: {}", snFarmId);
            return;
        }

        var req = new PigletCatm1Request();
        req.setPigletID(barn.getPigletId());
        fillSensorData(req, data, csvFilePath);
        pigletCatm1Service.createPigletCatm1(req);
    }

    private void processReserve(String[] data, String snFarmId, Path csvFilePath) {
        var barn = reserveRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Reserve 정보를 찾을 수 없음: {}", snFarmId);
            return;
        }

        var req = new ReserveCatm1Request();
        req.setReserveID(barn.getReserveId());
        fillSensorData(req, data, csvFilePath);
        reserveCatm1Service.createReserveCatm1(req);
    }

    private String convertToDateTime(Path filePath, String timeStr) {
        try {
            String fileName = filePath.getFileName().toString(); // 예: 320690-250330.log 또는 320690-250330.csv
            String[] parts = fileName.split("-");
            if (parts.length < 2) {
                logger.error("[CSV] 파일명에서 날짜 추출 실패: {}", fileName);
                return null;
            }

            // 확장자 제거 (예: "250330.log" -> "250330" 또는 "250330.csv" -> "250330")
            String datePart = parts[1].split("\\.")[0];

            // 날짜 형식이 맞는지 확인 (6자리 숫자여야 함)
            if (!datePart.matches("\\d{6}")) {
                logger.error("[CSV] 날짜 형식 오류: {}", datePart);
                return null;
            }

            int year = 2000 + Integer.parseInt(datePart.substring(0, 2));
            int month = Integer.parseInt(datePart.substring(2, 4));
            int day = Integer.parseInt(datePart.substring(4, 6));

            // 시:분:초 형태의 문자열과 합치기
            return String.format("%04d-%02d-%02d %s", year, month, day, timeStr);
        } catch (Exception e) {
            logger.error("[CSV] 날짜 변환 중 오류: {}", e.getMessage(), e);
            return null;
        }
    }

    private void fillSensorData(Object req, String[] data, Path csvFilePath) {
        try {
            String timeStr = data[0]; // 시:분:초 형태
            String fullDateTime = convertToDateTime(csvFilePath, timeStr); // 날짜와 시간 합치기

            if (fullDateTime == null) {
                logger.error("[CSV] 날짜시간 변환 실패: {}, {}", csvFilePath, timeStr);
                throw new IllegalArgumentException("날짜시간 변환 실패");
            }

            // 센서 데이터 처리 - 10으로 나누는 로직 제거
            String temper = formatDecimalCorrectly(data[1]);
            String humidity = formatDecimalCorrectly(data[2]);
            String wtemper = formatDecimalCorrectly(data[3]);
            String co2 = data[4];

            if (req instanceof BoarsCatm1Request r) {
                r.setTime(fullDateTime);
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWtemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof FinishingCatm1Request r) {
                r.setTime(fullDateTime);
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWTemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof GestationCatm1Request r) {
                r.setTime(fullDateTime);
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWTemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof GrowingCatm1Request r) {
                r.setTime(fullDateTime);
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWTemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof MaternityCatm1Request r) {
                r.setTime(fullDateTime);
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWTemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof PigletCatm1Request r) {
                r.setTime(fullDateTime);
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWTemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof ReserveCatm1Request r) {
                r.setTime(fullDateTime);
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWTemper(wtemper);
                r.setCo2(co2);
            }
        } catch (Exception e) {
            logger.error("[CSV] 센서 데이터 파싱 오류: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 수정된 포맷 메서드 - 10으로 나누지 않고 그대로 소수점 1자리로 포맷
    private String formatDecimalCorrectly(String value) {
        try {
            float number = Float.parseFloat(value);
            // 10으로 나누는 대신 그대로 소수점 1자리로 포맷
            return String.format("%.1f", number);
        } catch (NumberFormatException e) {
            logger.warn("[CSV] 숫자 변환 오류: {}", value);
            return "0";
        }
    }

    // 기존 메서드는 하위 호환성을 위해 남겨둠
    private String formatDecimal(String value) {
        try {
            float number = Float.parseFloat(value);
            return String.format("%.1f", number / 10f);
        } catch (NumberFormatException e) {
            logger.warn("[CSV] 숫자 변환 오류: {}", value);
            return "0";
        }
    }
}