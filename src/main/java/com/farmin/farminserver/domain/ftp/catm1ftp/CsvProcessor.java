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
import com.farmin.farminserver.entity.catm1.boarscatm1sensor.BoarsCatm1Repository;
import com.farmin.farminserver.entity.catm1.finishingcatm1sensor.FinishingCatm1Repository;
import com.farmin.farminserver.entity.catm1.gestationcatm1sensor.GestationCatm1Repository;
import com.farmin.farminserver.entity.catm1.growingcatm1sensor.GrowingCatm1Repository;
import com.farmin.farminserver.entity.catm1.maternitycatm1sensor.MaternityCatm1Repository;
import com.farmin.farminserver.entity.catm1.pigletcatm1sensor.PigletCatm1Repository;
import com.farmin.farminserver.entity.catm1.reservecatm1sensor.ReserveCatm1Repository;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoEntity;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CsvProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CsvProcessor.class);

    // SNFarmID 추출 패턴들
    private static final Pattern FARM_ID_PATTERN = Pattern.compile("farmin(\\d+)");
    private static final Pattern FILE_ID_PATTERN = Pattern.compile("(\\d+)-(\\d+)\\.csv$");

    // FTP 모뎀 디렉토리 경로
    private static final String FTP_MODEM_PATH = "/home/farmin/ftp/modem";

    private final SNFarmInfoRepository snFarmInfoRepository;

    private final BoarsRepository boarsRepository;
    private final BoarsCatm1Service boarsCatm1Service;
    private final BoarsCatm1Repository boarsCatm1Repository;

    private final FinishingRepository finishingRepository;
    private final FinishingCatm1Service finishingCatm1Service;
    private final FinishingCatm1Repository finishingCatm1Repository;

    private final GestationRepository gestationRepository;
    private final GestationCatm1Service gestationCatm1Service;
    private final GestationCatm1Repository gestationCatm1Repository;

    private final GrowingRepository growingRepository;
    private final GrowingCatm1Service growingCatm1Service;
    private final GrowingCatm1Repository growingCatm1Repository;

    private final MaternityRepository maternityRepository;
    private final MaternityCatm1Service maternityCatm1Service;
    private final MaternityCatm1Repository maternityCatm1Repository;

    private final PigletRepository pigletRepository;
    private final PigletCatm1Service pigletCatm1Service;
    private final PigletCatm1Repository pigletCatm1Repository;

    private final ReserveRepository reserveRepository;
    private final ReserveCatm1Service reserveCatm1Service;
    private final ReserveCatm1Repository reserveCatm1Repository;

    // 개선된 파일 처리 상태 추적 맵
    private final Map<String, Long> processedFiles = new ConcurrentHashMap<>();
    private final Map<String, Long> lastProcessedPosition = new ConcurrentHashMap<>();

    // 중복 처리 방지를 위한 타임스탬프 캐시
    private final Set<String> processedTimestamps = Collections.synchronizedSet(ConcurrentHashMap.newKeySet());

    // 파일별 락 관리
    private final Map<String, Object> fileLocks = new ConcurrentHashMap<>();

    // DB 작업을 위한 스레드 풀 - 크기 조정
    private final ExecutorService dbExecutor = Executors.newFixedThreadPool(5);

    // 타임스탬프 캐시 정리 주기
    private static final long CACHE_CLEANUP_INTERVAL = 24 * 60 * 60 * 1000; // 24시간

    // 모니터링 중인 디렉토리 목록 캐시
    private Set<String> monitoredDirectories = new HashSet<>();
    private Set<String> currentDirectories = new HashSet<>();

    // 서버 시작 시 실행
    @PostConstruct
    public void init() {
        logger.info("[CSV] 서버 시작, 디렉토리 및 파일 초기 스캔 시작");
        scanAndUpdateDirectories(); // 디렉토리 + 파일까지 탐색
    }

    /**
     * 정기적으로 타임스탬프 캐시를 정리하는 스케줄러
     */
    @Scheduled(fixedRate = CACHE_CLEANUP_INTERVAL)
    public void cleanupTimestampCache() {
        processedTimestamps.clear();
        logger.info("[CSV] 타임스탬프 캐시 정리 완료: {}", LocalDateTime.now());
    }

    // 정기적으로 디렉토리 스캔 및 업데이트
    @Scheduled(fixedRate = 10000)
    public void scanAndUpdateDirectories() {
        logger.info("[CSV] 디렉토리 스캔 및 로그 파일 변환 시작");

        try {
            Path modemPath = Paths.get(FTP_MODEM_PATH);
            if (!Files.exists(modemPath)) {
                logger.error("[CSV] 모뎀 디렉토리가 존재하지 않음: {}", FTP_MODEM_PATH);
                return;
            }

            // 복사 대상 디렉토리 생성 (없으면)
            Path targetDir = Paths.get("/home/farmin/바탕화면/Catm1/log");
            if (!Files.exists(targetDir)) {
                try {
                    Files.createDirectories(targetDir);
                    logger.info("[CSV] 대상 디렉토리 생성: {}", targetDir);
                } catch (IOException e) {
                    logger.error("[CSV] 대상 디렉토리 생성 실패: {}", targetDir, e);
                    return;
                }
            }

            // 기존 목록 백업
            Set<String> previousDirectories = new HashSet<>(monitoredDirectories);

            // 현재 디렉토리 목록 새로 가져오기
            currentDirectories.clear();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(modemPath, Files::isDirectory)) {
                for (Path path : stream) {
                    String dirName = path.getFileName().toString();
                    currentDirectories.add(dirName);
                    logger.debug("[CSV] 디렉토리 발견: {}", dirName);

                    // 이 디렉토리 내의 .log 파일 복사 및 CSV로 변환
                    copyAndConvertLogFiles(path, targetDir, dirName);
                }
            }

            logger.info("[CSV] 발견된 모든 디렉토리 수: {}", currentDirectories.size());

            // 새로운 디렉토리 확인
            Set<String> newDirectories = new HashSet<>(currentDirectories);
            newDirectories.removeAll(previousDirectories);

            // 사라진 디렉토리 확인
            Set<String> removedDirectories = new HashSet<>(previousDirectories);
            removedDirectories.removeAll(currentDirectories);

            // 모니터링 목록 완전히 갱신 (현재 발견된 모든 디렉토리로)
            monitoredDirectories = new HashSet<>(currentDirectories);

            // 변경사항 로깅
            if (!newDirectories.isEmpty()) {
                logger.info("[CSV] 새로운 디렉토리 발견: {}", newDirectories);
            }

            if (!removedDirectories.isEmpty()) {
                logger.info("[CSV] 사라진 디렉토리: {}", removedDirectories);
                // 사라진 디렉토리에 관련된 파일 처리 상태 제거
                for (String removedDir : removedDirectories) {
                    processedFiles.entrySet().removeIf(e -> e.getKey().contains(removedDir));
                    lastProcessedPosition.entrySet().removeIf(e -> e.getKey().contains(removedDir));
                    fileLocks.entrySet().removeIf(e -> e.getKey().contains(removedDir));
                }
            }

            if (newDirectories.isEmpty() && removedDirectories.isEmpty()) {
                logger.info("[CSV] 디렉토리 변경사항 없음, 총 모니터링 중인 디렉토리: {}", monitoredDirectories.size());
            }

            // 현재 모든 디렉토리에 있는 CSV 파일 처리
            processAllCsvFiles(targetDir);

        } catch (IOException e) {
            logger.error("[CSV] 디렉토리 스캔 중 오류 발생", e);
        }

        // 오래된 처리 기록 정리
        cleanupProcessedRecords();
    }

    private void cleanupProcessedRecords() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);

        // 오래된 타임스탬프 정리
        processedTimestamps.removeIf(timestamp -> {
            try {
                String[] parts = timestamp.split("_");
                if (parts.length >= 2) {
                    LocalDateTime time = LocalDateTime.parse(parts[1],
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    return time.isBefore(cutoff);
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        });

        logger.info("[CSV] 정리 완료 - 파일 상태: {}, 타임스탬프: {}",
                processedFiles.size(), processedTimestamps.size());
    }

    private void copyAndConvertLogFiles(Path sourceDir, Path targetBaseDir, String dirName) {
        try {
            // 대상 디렉토리가 존재하지 않으면 생성
            Path specificTargetDir = targetBaseDir.resolve(dirName);
            if (!Files.exists(specificTargetDir)) {
                Files.createDirectories(specificTargetDir);
                logger.info("[CSV] 디렉토리별 대상 디렉토리 생성: {}", specificTargetDir);
            }

            // .log 파일만 필터링하여 복사 및 CSV로 변환
            try (DirectoryStream<Path> logFiles = Files.newDirectoryStream(sourceDir, "*.log")) {
                int copyCount = 0;
                for (Path logFile : logFiles) {
                    Path targetCsvFile = specificTargetDir.resolve(
                            logFile.getFileName().toString().replace(".log", ".csv"));

                    // 이미 존재하는 CSV 파일인 경우, 원본 로그 파일이 업데이트되었는지 확인
                    boolean needsUpdate = true;
                    if (Files.exists(targetCsvFile)) {
                        BasicFileAttributes logAttrs = Files.readAttributes(logFile, BasicFileAttributes.class);
                        BasicFileAttributes csvAttrs = Files.readAttributes(targetCsvFile, BasicFileAttributes.class);

                        // 로그 파일이 CSV 파일보다 최신이 아니면 업데이트 불필요
                        if (logAttrs.lastModifiedTime().toMillis() <= csvAttrs.lastModifiedTime().toMillis()) {
                            needsUpdate = false;
                        }
                    }

                    if (needsUpdate) {
                        // 로그 파일을 CSV로 변환
                        convertLogToCsv(logFile, targetCsvFile);
                        copyCount++;
                        logger.debug("[CSV] 로그 파일 변환: {} -> {}", logFile, targetCsvFile);
                    }
                }

                if (copyCount > 0) {
                    logger.info("[CSV] 디렉토리 {} 에서 {} 개의 로그 파일 변환 완료", dirName, copyCount);
                }
            }
        } catch (IOException e) {
            logger.error("[CSV] 로그 파일 변환 중 오류 발생 - 디렉토리: {}", sourceDir, e);
        }
    }

    // 로그 파일을 CSV로 변환하는 메서드
    private void convertLogToCsv(Path logFile, Path csvFile) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(logFile);
             BufferedWriter writer = Files.newBufferedWriter(csvFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            String line;
            while ((line = reader.readLine()) != null) {
                // 로그 형식에 맞게 CSV 형식으로 변환 (필요에 따라 조정)
                // 예시: 로그가 "HH:MM:SS DATA1 DATA2 DATA3 DATA4" 형식이라면
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 5) {
                    // CSV 형식으로 작성 (시간,온도,습도,수온,CO2)
                    writer.write(String.format("%s,%s,%s,%s,%s%n",
                            parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        }
    }

    // 모든 CSV 파일 처리
    private void processAllCsvFiles(Path baseDir) {
        for (String dirName : currentDirectories) {
            Path dirPath = baseDir.resolve(dirName);
            if (!Files.exists(dirPath)) continue;

            logger.debug("[CSV] 디렉토리 내 CSV 파일 스캔: {}", dirPath);

            try (DirectoryStream<Path> files = Files.newDirectoryStream(dirPath, "*.csv")) {
                List<Path> csvFiles = new ArrayList<>();
                files.forEach(csvFiles::add);

                // 파일을 날짜순으로 정렬 (최신 파일이 마지막에 오도록)
                csvFiles.sort(Comparator.comparing(path -> {
                    try {
                        return Files.getLastModifiedTime(path);
                    } catch (IOException e) {
                        return FileTime.fromMillis(0);
                    }
                }));

                // 모든 파일 처리
                for (int i = 0; i < csvFiles.size(); i++) {
                    Path filePath = csvFiles.get(i);
                    boolean isLatestFile = (i == csvFiles.size() - 1);

                    // 비동기 처리 요청
                    dbExecutor.submit(() -> processCSVFile(filePath, isLatestFile));
                }
            } catch (IOException e) {
                logger.error("[CSV] CSV 파일 스캔 중 오류 - 디렉토리: {}", dirPath, e);
            }
        }
    }

    /**
     * CSV 파일 처리 메인 메서드
     * @param csvFilePath 처리할 CSV 파일 경로
     * @param isLatestFile 최신 파일 여부
     */
    public void processCSVFile(Path csvFilePath, boolean isLatestFile) {
        String fileKey = csvFilePath.toString();

        // 파일별 락 획득
        Object fileLock = fileLocks.computeIfAbsent(fileKey, k -> new Object());

        synchronized (fileLock) {
            try {
                // 파일 크기 확인
                long fileSize = Files.size(csvFilePath);
                Long lastSize = processedFiles.get(fileKey);

                // 이미 처리된 파일이고 크기가 변경되지 않았으면 스킵
                if (lastSize != null && lastSize == fileSize && !isLatestFile) {
                    logger.debug("[CSV] 이미 처리된 파일 건너뛰기: {}", csvFilePath);
                    return;
                }

                // 파일 처리 위치 가져오기
                long position = lastProcessedPosition.getOrDefault(fileKey, 0L);

                logger.info("[CSV] 파일 처리 시작: {}, 이전 위치: {}, 현재 크기: {}",
                        csvFilePath, position, fileSize);

                // 파일 처리 로직
                processFileContent(csvFilePath, position);

                // 처리 상태 업데이트
                processedFiles.put(fileKey, fileSize);
                lastProcessedPosition.put(fileKey, fileSize);

            } catch (IOException e) {
                logger.error("[CSV] 파일 처리 중 오류: {}", csvFilePath, e);
            }
        }
    }

    /**
     * 파일 내용 처리
     * @param csvFilePath CSV 파일 경로
     * @param startPosition 시작 위치
     */
    private void processFileContent(Path csvFilePath, long startPosition) throws IOException {
        String snFarmId = extractSnFarmIdFromPath(csvFilePath);
        if (snFarmId == null || snFarmId.isEmpty()) {
            logger.error("[CSV] SNFarmID 추출 실패: {}", csvFilePath);
            return;
        }

        // 축사 유형 결정
        BarnType barnType = determineBarnType(snFarmId);
        if (barnType == BarnType.UNKNOWN) {
            logger.error("[CSV] SNFarmID에 해당하는 축사 유형을 찾을 수 없음: {}", snFarmId);
            return;
        }

        int processedLines = 0;
        int skippedLines = 0;
        int errorLines = 0;

        try (RandomAccessFile raf = new RandomAccessFile(csvFilePath.toFile(), "r")) {
            // 시작 위치로 이동
            if (startPosition > 0) {
                raf.seek(startPosition);
            }

            String line;
            while ((line = raf.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length < 5) {
                    logger.warn("[CSV] 데이터 형식 오류 (5개 미만): {}", line);
                    errorLines++;
                    continue;
                }

                try {
                    // 라인 처리
                    processLine(data, snFarmId, csvFilePath, barnType);
                    processedLines++;
                } catch (Exception e) {
                    logger.error("[CSV] 라인 처리 중 오류: {}", e.getMessage());
                    errorLines++;
                }
            }

            logger.info("[CSV] 파일 처리 완료: {}, 처리된 라인: {}, 건너뛴 라인: {}, 오류 라인: {}",
                    csvFilePath, processedLines, skippedLines, errorLines);
        }
    }

    /**
     * 개별 CSV 라인 처리
     * @param data CSV 라인 데이터 배열
     * @param snFarmId 농장 ID
     * @param csvFilePath 파일 경로
     * @param barnType 축사 유형
     */
    private void processLine(String[] data, String snFarmId, Path csvFilePath, BarnType barnType) {
        // 파일에서 날짜/시간 추출
        String timeStr = data[0]; // 시:분:초 형태
        String fullDateTime = convertToDateTime(csvFilePath, timeStr);

        if (fullDateTime == null) {
            logger.error("[CSV] 날짜시간 변환 실패: {}, {}", csvFilePath, timeStr);
            return;
        }

        // 중복 체크 키 생성
        String dupeKey = snFarmId + "_" + fullDateTime + "_" + barnType;

        // 중복 등록 시도 - 원자적 연산으로 경쟁 상태 방지
        if (!tryRegisterProcessing(dupeKey)) {
            logger.debug("[CSV] 중복 데이터 건너뛰기: FarmId: {}, 시간: {}", snFarmId, fullDateTime);
            return;
        }

        try {
            // 중복 검사 (DB 수준)
            if (isDuplicate(snFarmId, fullDateTime, barnType)) {
                logger.debug("[CSV] DB에 이미 존재하는 데이터: FarmId: {}, 시간: {}", snFarmId, fullDateTime);
                return;
            }

            // 축사 유형에 따라 데이터 처리
            boolean success = processDataForBarnType(data, snFarmId, barnType, csvFilePath, fullDateTime);

            // 처리 실패 시 타임스탬프 제거하여 재시도 가능하게 함
            if (!success) {
                removeProcessedTimestamp(dupeKey);
            }
        } catch (Exception e) {
            // 예외 발생 시 타임스탬프 제거하여 재시도 가능하게 함
            removeProcessedTimestamp(dupeKey);
            logger.error("[CSV] 데이터 처리 중 오류: {}", e.getMessage(), e);
        }
    }

    /**
     * 중복 등록 시도 - 원자적 연산으로 경쟁 상태 방지
     * @param key 중복 체크 키
     * @return 등록 성공 여부
     */
    private boolean tryRegisterProcessing(String key) {
        synchronized (processedTimestamps) {
            if (processedTimestamps.contains(key)) {
                return false; // 이미 처리 중이거나 처리됨
            }
            // 처리 중 표시
            processedTimestamps.add(key);
            return true;
        }
    }

    /**
     * 처리된 타임스탬프 제거
     * @param key 중복 체크 키
     */
    private void removeProcessedTimestamp(String key) {
        synchronized (processedTimestamps) {
            processedTimestamps.remove(key);
        }
    }

    // 수정된 processDataForBarnType 메서드 - 기존 코드 유지
    private boolean processDataForBarnType(String[] data, String snFarmId, BarnType barnType, Path csvFilePath, String fullDateTime) {
        switch (barnType) {
            case BOARS:
                return processBoars(data, snFarmId, csvFilePath, fullDateTime);
            case FINISHING:
                return processFinishing(data, snFarmId, csvFilePath, fullDateTime);
            case GESTATION:
                return processGestation(data, snFarmId, csvFilePath, fullDateTime);
            case GROWING:
                return processGrowing(data, snFarmId, csvFilePath, fullDateTime);
            case MATERNITY:
                return processMaternity(data, snFarmId, csvFilePath, fullDateTime);
            case PIGLET:
                return processPiglet(data, snFarmId, csvFilePath, fullDateTime);
            case RESERVE:
                return processReserve(data, snFarmId, csvFilePath, fullDateTime);
            default:
                throw new IllegalArgumentException("지원되지 않는 축사 유형: " + barnType);
        }
    }

    // 나머지 기존 메서드들은 그대로 유지
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

    private boolean isDuplicate(String snFarmId, String dateTime, BarnType barnType) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.parse(dateTime, formatter);

        switch (barnType) {
            case BOARS:
                var boarsBarn = boarsRepository.findBySnFarmId(snFarmId).orElse(null);
                if (boarsBarn == null) return false;
                return boarsCatm1Repository.existsByBoarsIdAndTime(boarsBarn.getBoarsId(), time);
            case FINISHING:
                var finishingBarn = finishingRepository.findBySnFarmId(snFarmId).orElse(null);
                if (finishingBarn == null) return false;
                return finishingCatm1Repository.existsByFinishingIdAndTime(finishingBarn.getFinishingId(), time);
            case GESTATION:
                var gestationBarn = gestationRepository.findBySnFarmId(snFarmId).orElse(null);
                if (gestationBarn == null) return false;
                return gestationCatm1Repository.existsByGestationIdAndTime(gestationBarn.getGestationId(), time);
            case GROWING:
                var growingBarn = growingRepository.findBySnFarmId(snFarmId).orElse(null);
                if (growingBarn == null) return false;
                return growingCatm1Repository.existsByGrowingIdAndTime(growingBarn.getGrowingId(), time);
            case MATERNITY:
                var maternityBarn = maternityRepository.findBySnFarmId(snFarmId).orElse(null);
                if (maternityBarn == null) return false;
                return maternityCatm1Repository.existsByMaternityIdAndTime(maternityBarn.getMaternityId(), time);
            case PIGLET:
                var pigletBarn = pigletRepository.findBySnFarmId(snFarmId).orElse(null);
                if (pigletBarn == null) return false;
                return pigletCatm1Repository.existsByPigletIdAndTime(pigletBarn.getPigletId(), time);
            case RESERVE:
                var reserveBarn = reserveRepository.findBySnFarmId(snFarmId).orElse(null);
                if (reserveBarn == null) return false;
                return reserveCatm1Repository.existsByReserveIdAndTime(reserveBarn.getReserveId(), time);
            default:
                return false;
        }
    }

    // 각 축사별 처리 메서드들도 기존 구현 유지
    private boolean processBoars(String[] data, String snFarmId, Path csvFilePath, String fullDateTime) {
        var barn = boarsRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Boars 정보를 찾을 수 없음: {}", snFarmId);
            return false;
        }

        var req = new BoarsCatm1Request();
        req.setBoarsId(String.valueOf(barn.getBoarsId()));

        // 이미 설정된 시간 사용
        req.setTime(LocalDateTime.parse(fullDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        fillSensorData(req, data);

        boarsCatm1Service.createBoarsCatm1(req);
        return true;
    }

    private boolean processFinishing(String[] data, String snFarmId, Path csvFilePath, String fullDateTime) {
        var barn = finishingRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Finishing 정보를 찾을 수 없음: {}", snFarmId);
            return false;
        }

        var req = new FinishingCatm1Request();
        req.setFinishingId(barn.getFinishingId());

        req.setTime(LocalDateTime.parse(fullDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        fillSensorData(req, data);

        finishingCatm1Service.createFinishingCatm1(req);
        return true;
    }

    private boolean processGestation(String[] data, String snFarmId, Path csvFilePath, String fullDateTime) {
        var barn = gestationRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Gestation 정보를 찾을 수 없음: {}", snFarmId);
            return false;
        }

        var req = new GestationCatm1Request();
        req.setGestationId(barn.getGestationId());

        req.setTime(LocalDateTime.parse(fullDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        fillSensorData(req, data);

        gestationCatm1Service.createGestationCatm1(req);
        return true;
    }

    private boolean processGrowing(String[] data, String snFarmId, Path csvFilePath, String fullDateTime) {
        var barn = growingRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Growing 정보를 찾을 수 없음: {}", snFarmId);
            return false;
        }

        var req = new GrowingCatm1Request();
        req.setGrowingId(barn.getGrowingId());

        req.setTime(LocalDateTime.parse(fullDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        fillSensorData(req, data);

        growingCatm1Service.createGrowingCatm1(req);
        return true;
    }

    private boolean processMaternity(String[] data, String snFarmId, Path csvFilePath, String fullDateTime) {
        var barn = maternityRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Maternity 정보를 찾을 수 없음: {}", snFarmId);
            return false;
        }

        var req = new MaternityCatm1Request();
        req.setMaternityId(barn.getMaternityId());

        req.setTime(LocalDateTime.parse(fullDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        fillSensorData(req, data);

        maternityCatm1Service.createMaternityCatm1(req);
        return true;
    }

    private boolean processPiglet(String[] data, String snFarmId, Path csvFilePath, String fullDateTime) {
        var barn = pigletRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Piglet 정보를 찾을 수 없음: {}", snFarmId);
            return false;
        }

        var req = new PigletCatm1Request();
        req.setPigletId(barn.getPigletId());

        req.setTime(LocalDateTime.parse(fullDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        fillSensorData(req, data);

        pigletCatm1Service.createPigletCatm1(req);
        return true;
    }

    private boolean processReserve(String[] data, String snFarmId, Path csvFilePath, String fullDateTime) {
        var barn = reserveRepository.findBySnFarmId(snFarmId).orElse(null);
        if (barn == null) {
            logger.error("[CSV] Reserve 정보를 찾을 수 없음: {}", snFarmId);
            return false;
        }

        var req = new ReserveCatm1Request();
        req.setReserveId(barn.getReserveId());

        req.setTime(LocalDateTime.parse(fullDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        fillSensorData(req, data);

        reserveCatm1Service.createReserveCatm1(req);
        return true;
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

    private void fillSensorData(Object req, String[] data) {
        try {
            // 센서 데이터 처리
            String temper = formatDecimalCorrectly(data[1]);
            String humidity = formatDecimalCorrectly(data[2]);
            String wtemper = formatDecimalCorrectly(data[3]);
            String co2 = data[4];

            if (req instanceof BoarsCatm1Request r) {
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWtemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof FinishingCatm1Request r) {
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWTemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof GestationCatm1Request r) {
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWTemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof GrowingCatm1Request r) {
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWTemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof MaternityCatm1Request r) {
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWTemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof PigletCatm1Request r) {
                r.setTemper(temper);
                r.setHumidity(humidity);
                r.setWTemper(wtemper);
                r.setCo2(co2);
            } else if (req instanceof ReserveCatm1Request r) {
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

    private String formatDecimalCorrectly(String value) {
        try {
            float number = Float.parseFloat(value);
            return String.format("%.1f", number);
        } catch (NumberFormatException e) {
            logger.warn("[CSV] 숫자 변환 오류: {}", value);
            return "0";
        }
    }

    // BarnType 열거형은 기존 그대로 유지
    private enum BarnType {
        BOARS, FINISHING, GESTATION, GROWING, MATERNITY, PIGLET, RESERVE, UNKNOWN
    }
}