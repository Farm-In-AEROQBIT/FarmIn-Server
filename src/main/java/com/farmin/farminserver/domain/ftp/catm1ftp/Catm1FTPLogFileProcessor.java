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

@RequiredArgsConstructor
@Service
public class Catm1FTPLogFileProcessor {
    private static final String FTP_SERVER = "211.230.2.77";
    private static final int FTP_PORT = 2301;
    private static final String FTP_USER = "farmin";
    private static final String FTP_PASSWORD = "230130";
    private static final String FTP_LOG_DIRECTORY = "/home/farmin/ftp/modem";
    private static final String LOCAL_SAVE_DIRECTORY = "/home/farmin/바탕화면/Catm1/log";

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
                    FTPFile[] files = ftpService.listFiles(FTP_LOG_DIRECTORY);
                    if (files == null || files.length == 0) {
                        System.out.println("[FTP] 파일 없음");
                        return false;
                    }

                    Files.createDirectories(Paths.get(LOCAL_SAVE_DIRECTORY));

                    for (FTPFile file : files) {
                        if (file.isFile() && file.getName().endsWith(".log")) {
                            String remotePath = FTP_LOG_DIRECTORY + "/" + file.getName();
                            String localPath = LOCAL_SAVE_DIRECTORY + "/" + file.getName();

                            if (ftpService.downloadFile(remotePath, localPath)) {
                                logConverter.convertLogToCSV(Paths.get(localPath));
                            }
                        }
                    }
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    ftpService.disconnect();
                }
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
            Files.walk(Paths.get(LOCAL_SAVE_DIRECTORY))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".log"))
                    .forEach(logConverter::convertLogToCSV);
        } catch (IOException e) {
            System.err.println("[LOG] 변환 실패");
            e.printStackTrace();
        }
    }

    private void processAllCSVFiles() {
        try {
            Files.walk(Paths.get(LOCAL_SAVE_DIRECTORY))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(csvProcessor::processCSVFile);
        } catch (IOException e) {
            System.err.println("[CSV] 처리 실패");
            e.printStackTrace();
        }
    }
}
