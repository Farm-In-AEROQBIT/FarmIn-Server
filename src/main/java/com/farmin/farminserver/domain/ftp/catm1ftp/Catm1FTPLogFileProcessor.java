package com.farmin.farminserver.domain.ftp.catm1ftp;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class Catm1FTPLogFileProcessor {
    private static final String FTP_SERVER = "211.230.2.77";
    private static final int FTP_PORT = 2301;
    private static final String FTP_USER = "farmin";
    private static final String FTP_PASSWORD = "230130";

    private static final String FTP_MODEM_PATH = "/home/farmin/ftp/modem";
    private static final String LOCAL_BASE_DIRECTORY = "/home/farmin/바탕화면/Catm1/log";

    private static final int RETRY_COUNT = 3;
    private static final long RETRY_DELAY_MS = 10_000;

    // 의존성 주입 받는 필드들
    private final CsvProcessor csvProcessor;
    private final ExecutorService dbExecutor;
    private final Set<String> processedFiles;
    @Qualifier("sensorTableMapping")
    private final Map<String, String> SENSOR_TABLE_MAPPING;

    @Qualifier("sensorIdMapping")
    private final Map<String, String> SENSOR_ID_MAPPING;

    // 내부에서 직접 생성
    private final FtpService ftpService = new FtpService();
    private final LogConverter logConverter = new LogConverter();

    private volatile boolean isProcessing = false;

    @PostConstruct
    public void init() {
        initialProcess();
    }

    private void initialProcess() {
        System.out.println("[초기 실행] FTP 처리 시작");
        if (downloadAllDirectories()) {
            convertAllLogFiles();
            processAllCSVFiles();
            System.out.println("[초기 실행] 완료");
        } else {
            System.err.println("[초기 실행] 다운로드 실패");
        }
    }

    @Scheduled(fixedRate = 300000)
    public void scheduledDownload() {
        if (!isProcessing) {
            try {
                isProcessing = true;
                System.out.println("[스케줄러] FTP 처리 시작");
                if (downloadAllDirectories()) {
                    convertAllLogFiles();
                    processAllCSVFiles();
                    System.out.println("[스케줄러] 완료");
                } else {
                    System.err.println("[스케줄러] 다운로드 실패");
                }
            } finally {
                isProcessing = false;
            }
        }
    }

    private boolean downloadAllDirectories() {
        for (int attempt = 1; attempt <= RETRY_COUNT; attempt++) {
            System.out.println("[FTP] 연결 시도: " + attempt);
            if (ftpService.connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PASSWORD)) {
                try {
                    ftpService.configure();

                    // 현재 작업 디렉토리 출력
                    try {
                        String currentDir = ftpService.getClient().printWorkingDirectory();
                        System.out.println("[FTP] 현재 작업 디렉토리: " + currentDir);
                    } catch (IOException e) {
                        System.err.println("[FTP] 현재 디렉토리 확인 실패: " + e.getMessage());
                    }

                    // 기본 디렉토리 생성
                    try {
                        Files.createDirectories(Paths.get(LOCAL_BASE_DIRECTORY));
                    } catch (IOException e) {
                        System.err.println("[FTP] 기본 디렉토리 생성 실패: " + e.getMessage());
                        return false;
                    }

                    // 메인 모뎀 디렉토리 처리
                    boolean anySuccess = false;

                    try {
                        System.out.println("[FTP] 메인 모뎀 디렉토리 확인: " + FTP_MODEM_PATH);
                        FTPFile[] farmDirs = ftpService.getClient().listFiles(FTP_MODEM_PATH);

                        if (farmDirs != null && farmDirs.length > 0) {
                            System.out.println("[FTP] 농장 디렉토리 수: " + farmDirs.length);

                            // 각 농장 디렉토리 처리
                            for (FTPFile farmDir : farmDirs) {
                                if (farmDir.isDirectory() && !farmDir.getName().equals(".") && !farmDir.getName().equals("..")) {
                                    String farmName = farmDir.getName();
                                    System.out.println("[FTP] 농장 디렉토리 처리: " + farmName);

                                    // 로컬 농장 디렉토리 생성
                                    Path localFarmDir = Paths.get(LOCAL_BASE_DIRECTORY, farmName);
                                    try {
                                        Files.createDirectories(localFarmDir);
                                        System.out.println("[FTP] 로컬 농장 디렉토리 생성 또는 확인: " + localFarmDir);
                                    } catch (IOException e) {
                                        System.err.println("[FTP] 로컬 농장 디렉토리 생성 실패: " + e.getMessage());
                                        continue;
                                    }

                                    String farmPath = FTP_MODEM_PATH + "/" + farmName;

                                    // 농장 디렉토리 내의 모든 파일 목록
                                    FTPFile[] farmFiles = ftpService.getClient().listFiles(farmPath);

                                    if (farmFiles != null && farmFiles.length > 0) {
                                        System.out.println("[FTP] 농장 " + farmName + "에서 " + farmFiles.length + "개 항목 발견");

                                        // 로그 파일 카운터
                                        int logFileCount = 0;

                                        // 디렉토리 내의 모든 파일 처리
                                        for (FTPFile farmFile : farmFiles) {
                                            if (farmFile.isFile()) {
                                                String fileName = farmFile.getName();

                                                // .log 확장자를 가진 파일만 처리
                                                if (fileName.toLowerCase().endsWith(".log")) {
                                                    String remotePath = farmPath + "/" + fileName;
                                                    String localPath = localFarmDir.resolve(fileName).toString();

                                                    System.out.println("[FTP] 로그 파일 다운로드 시도: " + remotePath + " -> " + localPath);

                                                    if (ftpService.downloadFile(remotePath, localPath)) {
                                                        System.out.println("[FTP] 로그 파일 다운로드 성공: " + fileName);
                                                        logFileCount++;

                                                        // CSV로 변환
                                                        logConverter.convertLogToCSV(Paths.get(localPath));
                                                        anySuccess = true;
                                                    } else {
                                                        System.err.println("[FTP] 로그 파일 다운로드 실패: " + fileName);
                                                    }
                                                }
                                            } else if (farmFile.isDirectory() && !farmFile.getName().equals(".") && !farmFile.getName().equals("..")) {
                                                // 서브 디렉토리가 있는 경우 (추가 레벨)
                                                String subDirName = farmFile.getName();
                                                String subDirPath = farmPath + "/" + subDirName;

                                                // 서브 디렉토리용 로컬 디렉토리 생성
                                                Path localSubDir = localFarmDir.resolve(subDirName);
                                                try {
                                                    Files.createDirectories(localSubDir);
                                                    System.out.println("[FTP] 로컬 서브 디렉토리 생성 또는 확인: " + localSubDir);
                                                } catch (IOException e) {
                                                    System.err.println("[FTP] 로컬 서브 디렉토리 생성 실패: " + e.getMessage());
                                                    continue;
                                                }

                                                System.out.println("[FTP] 서브 디렉토리 확인: " + subDirPath);

                                                FTPFile[] subDirFiles = ftpService.getClient().listFiles(subDirPath);

                                                if (subDirFiles != null && subDirFiles.length > 0) {
                                                    System.out.println("[FTP] 서브 디렉토리 " + subDirName + "에서 " + subDirFiles.length + "개 항목 발견");

                                                    for (FTPFile subFile : subDirFiles) {
                                                        if (subFile.isFile()) {
                                                            String subFileName = subFile.getName();

                                                            // .log 확장자를 가진 파일만 처리
                                                            if (subFileName.toLowerCase().endsWith(".log")) {
                                                                String subRemotePath = subDirPath + "/" + subFileName;
                                                                String subLocalPath = localSubDir.resolve(subFileName).toString();

                                                                System.out.println("[FTP] 서브 로그 파일 다운로드 시도: " + subRemotePath + " -> " + subLocalPath);

                                                                if (ftpService.downloadFile(subRemotePath, subLocalPath)) {
                                                                    System.out.println("[FTP] 서브 로그 파일 다운로드 성공: " + subFileName);
                                                                    logFileCount++;

                                                                    // CSV로 변환
                                                                    logConverter.convertLogToCSV(Paths.get(subLocalPath));
                                                                    anySuccess = true;
                                                                } else {
                                                                    System.err.println("[FTP] 서브 로그 파일 다운로드 실패: " + subFileName);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        System.out.println("[FTP] 농장 " + farmName + "에서 " + logFileCount + "개 로그 파일 다운로드 완료");
                                    } else {
                                        System.out.println("[FTP] 농장 " + farmName + "에 파일이 없습니다.");
                                    }
                                }
                            }
                        } else {
                            System.out.println("[FTP] " + FTP_MODEM_PATH + "에 농장 디렉토리가 없습니다.");
                        }
                    } catch (IOException e) {
                        System.err.println("[FTP] 모뎀 디렉토리 처리 오류: " + e.getMessage());
                        e.printStackTrace();
                    }

                    return anySuccess;
                } catch (Exception e) {
                    System.err.println("[FTP] 처리 중 오류 발생: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    ftpService.disconnect();
                }
            } else {
                System.err.println("[FTP] 연결 또는 로그인 실패");
                System.err.println("[FTP] 응답 코드: " + ftpService.getServerReplyCode() + ", 메시지: " + ftpService.getLastServerResponse());
            }

            System.err.println("[FTP] 실패, " + RETRY_DELAY_MS / 1000 + "초 후 재시도");
            try {
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    private void convertAllLogFiles() {
        try {
            System.out.println("[LOG] 모든 로그 파일 변환 시작");

            // 로컬 디렉토리의 모든 .log 파일 리스트 (모든 하위 디렉토리 포함)
            List<Path> logFiles = Files.walk(Paths.get(LOCAL_BASE_DIRECTORY))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".log"))
                    .collect(Collectors.toList());

            System.out.println("[LOG] 변환할 로그 파일 수: " + logFiles.size());

            for (Path logFile : logFiles) {
                try {
                    System.out.println("[LOG] 파일 변환 시도: " + logFile);
                    logConverter.convertLogToCSV(logFile);
                } catch (Exception e) {
                    System.err.println("[LOG] 파일 변환 오류: " + logFile);
                    e.printStackTrace();
                }
            }

            System.out.println("[LOG] 모든 로그 파일 변환 완료");
        } catch (IOException e) {
            System.err.println("[LOG] 변환 실패");
            e.printStackTrace();
        }
    }

    private void processAllCSVFiles() {
        try {
            System.out.println("[CSV] 모든 CSV 파일 처리 시작");

            // 모든 하위 디렉토리의 CSV 파일 검색
            List<Path> csvFiles = Files.walk(Paths.get(LOCAL_BASE_DIRECTORY))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".csv"))
                    .collect(Collectors.toList());

            System.out.println("[CSV] 처리할 CSV 파일 수: " + csvFiles.size());

            // 각 파일에 대해 isLatestFile 설정 (마지막 파일만 true)
            for(int i = 0; i < csvFiles.size(); i++) {
                final boolean isLatestFile = (i == csvFiles.size() - 1);
                Path csvFile = csvFiles.get(i);
                System.out.println("[CSV] 파일 처리: " + csvFile + (isLatestFile ? " (최신 파일)" : ""));
                csvProcessor.processCSVFile(csvFile, isLatestFile);
            }

            System.out.println("[CSV] 모든 CSV 파일 처리 완료");
        } catch (IOException e) {
            System.err.println("[CSV] 처리 실패");
            e.printStackTrace();
        }
    }
}