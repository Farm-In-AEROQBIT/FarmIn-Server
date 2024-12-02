package com.farmin.farminserver.domain.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class FTPLogFileProcessor {
    // FTP 설정
    private static final String FTP_SERVER = "farm-in.ipdisk.co.kr";
    private static final String FTP_USER = "farmin";
    private static final String FTP_PASSWORD = "farmin230130";
    private static final String FTP_LOG_DIRECTORY = "/HDD1/monitoring/fi1030";
    private static final String LOCAL_SAVE_DIRECTORY = "/home/farmin/바탕화면/log";

    // DB 설정
    private static final String DB_URL = "jdbc:mysql://192.168.0.20:3306/FarmIn";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "230130";

    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private volatile boolean isProcessing = false;
    private final Set<String> processedFiles = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> processedTimestamps = Collections.synchronizedSet(new HashSet<>());
    private String tableName = "GrowingSensor";

    public FTPLogFileProcessor() {
        initialProcess();
    }

    private void initialProcess() {
        try {
            if (downloadFTPFiles(true)) {
                convertAllLogFiles();
                processAllCSVFiles();
            } else {
                System.out.println("초기 FTP 다운로드 실패");
            }
        } catch (Exception e) {
            System.out.println("초기 처리 중 오류 발생");
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 300000) // 5분마다 실행
    public void scheduledFTPDownload() {
        if (!isProcessing) {
            try {
                isProcessing = true;
                if (downloadFTPFiles(false)) {
                    convertAllLogFiles();
                    processAllCSVFiles();
                    cleanupProcessedRecords();
                }
            } finally {
                isProcessing = false;
            }
        }
    }

    private boolean downloadFTPFiles(boolean downloadAll) {
        FTPClient ftpClient = new FTPClient();
        boolean success = false;

        try {
            ftpClient.connect(FTP_SERVER);
            boolean login = ftpClient.login(FTP_USER, FTP_PASSWORD);
            if (!login) {
                System.out.println("FTP 로그인 실패");
                return false;
            }

            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            Files.createDirectories(Paths.get(LOCAL_SAVE_DIRECTORY));

            if (!ftpClient.changeWorkingDirectory(FTP_LOG_DIRECTORY)) {
                System.out.println("FTP 디렉토리 변경 실패: " + FTP_LOG_DIRECTORY);
                return false;
            }

            FTPFile[] logFiles = ftpClient.listFiles();
            if (logFiles != null && logFiles.length > 0) {
                Optional<FTPFile> latestFileOpt = Arrays.stream(logFiles)
                        .filter(file -> file.getName().endsWith(".log"))
                        .max((file1, file2) -> file1.getName().compareTo(file2.getName()));

                if (downloadAll) {
                    for (FTPFile logFile : logFiles) {
                        if (logFile.getName().endsWith(".log")) {
                            downloadFile(ftpClient, logFile);
                        }
                    }
                } else {
                    latestFileOpt.ifPresent(file -> downloadFile(ftpClient, file));
                }
                success = true;
            }
        } catch (IOException e) {
            System.out.println("FTP 파일 다운로드 중 오류 발생");
            e.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    private void downloadFile(FTPClient ftpClient, FTPFile logFile) {
        Path localPath = Paths.get(LOCAL_SAVE_DIRECTORY, logFile.getName());
        try {
            // 기존 파일이 있으면 삭제 후 저장
            if (Files.exists(localPath)) {
                Files.delete(localPath);
                System.out.println("[파일 삭제] 기존 파일 삭제 완료: " + localPath);
            }

            try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localPath.toFile()))) {
                boolean success = ftpClient.retrieveFile(logFile.getName(), outputStream);
                if (success) {
                    System.out.println("[다운로드 성공] 파일 다운로드 성공: " + logFile.getName());
                } else {
                    System.out.println("[다운로드 실패] 파일 다운로드 실패: " + logFile.getName());
                }
            }
        } catch (IOException e) {
            System.out.println("[파일 저장 오류] 파일 다운로드 및 저장 중 오류 발생: " + logFile.getName());
            e.printStackTrace();
        }
    }

    private void convertAllLogFiles() {
        File directory = new File(LOCAL_SAVE_DIRECTORY);
        File[] logFiles = directory.listFiles((dir, name) -> name.endsWith(".log"));

        if (logFiles != null && logFiles.length > 0) {
            Arrays.stream(logFiles).forEach(this::convertLogToCSV);
        }
    }

    private void convertLogToCSV(File logFile) {
        String csvFileName = logFile.getAbsolutePath().replace(".log", ".csv");
        File csvFile = new File(csvFileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {

            String line;
            int lineNumber = 0;
            String fileName = logFile.getName();
            String datePattern = "\\d{4}";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(datePattern);
            java.util.regex.Matcher matcher = pattern.matcher(fileName);

            String month = "01", day = "01", year = String.valueOf(LocalDate.now().getYear());
            if (matcher.find()) {
                String dateStr = matcher.group();
                month = dateStr.substring(0, 2);
                day = dateStr.substring(2, 4);
            }

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber <= 2) continue;

                String[] data = line.split("\\s+");
                if (data.length >= 7 && data[0].matches("\\d{4}")) {
                    String hours = data[0].substring(0, 2);
                    String minutes = data[0].substring(2, 4);

                    LocalDateTime dateTime = LocalDateTime.of(
                            Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),
                            Integer.parseInt(hours), Integer.parseInt(minutes), 0);

                    String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    if (isValidSensorData(data[2], data[3], data[4], data[5], data[6])) {
                        writer.write(String.join(",", formattedDateTime, data[2], data[3], data[4], data[5], data[6]));
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("CSV 변환 중 오류 발생: " + logFile.getName());
            e.printStackTrace();
        }
    }

    private void processAllCSVFiles() {
        File directory = new File(LOCAL_SAVE_DIRECTORY);
        File[] csvFiles = directory.listFiles((dir, name) -> name.endsWith(".csv"));

        if (csvFiles != null && csvFiles.length > 0) {
            for (File csvFile : csvFiles) {
                dbExecutor.submit(() -> processCSVFile(csvFile));
            }
        }
    }

    private void processCSVFile(File csvFile) {
        if (processedFiles.contains(csvFile.getName())) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) continue;

                String timestamp = data[0];
                if (!processedTimestamps.add(timestamp)) continue;

                saveSensorDataToDatabase(1, timestamp, data[4], data[5], data[3], data[1], data[2]);
            }
            processedFiles.add(csvFile.getName());
        } catch (IOException e) {
            System.out.println("CSV 파일 처리 중 오류 발생: " + csvFile.getName());
            e.printStackTrace();
        }
    }

    private void saveSensorDataToDatabase(int id, String time, String co2, String nh3, String pm, String temper, String humidity) {
        String idColumn = tableName.equals("GrowingSensor") ? "GrowingID" : "BoarsID";
        String checkSQL = "SELECT COUNT(*) FROM " + tableName + " WHERE Time = ?";
        String insertSQL = "INSERT INTO " + tableName + " (" + idColumn + ", Time, Co2, Nh3, PM, Temper, Humidity) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
                checkStmt.setString(1, time);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) return;
            }
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, time);
                pstmt.setString(3, co2);
                pstmt.setString(4, nh3);
                pstmt.setString(5, pm);
                pstmt.setString(6, temper);
                pstmt.setString(7, humidity);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("DB 저장 중 오류 발생");
            e.printStackTrace();
        }
    }

    private boolean isValidSensorData(String... values) {
        try {
            for (String value : values) Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void cleanupProcessedRecords() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        processedTimestamps.removeIf(timestamp -> {
            try {
                return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).isBefore(cutoff);
            } catch (Exception e) {
                return false;
            }
        });

        File directory = new File(LOCAL_SAVE_DIRECTORY);
        Set<String> existingFiles = Optional.ofNullable(directory.listFiles())
                .stream().flatMap(Arrays::stream).map(File::getName).collect(Collectors.toSet());
        processedFiles.removeIf(filename -> !existingFiles.contains(filename));
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
