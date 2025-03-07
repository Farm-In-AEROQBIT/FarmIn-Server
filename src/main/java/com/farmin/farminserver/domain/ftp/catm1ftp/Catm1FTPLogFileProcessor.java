package com.farmin.farminserver.domain.ftp.catm1ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class Catm1FTPLogFileProcessor {
    // FTP 설정
    private static final String FTP_SERVER = "farm-in.ipdisk.co.kr";
    private static final String FTP_USER = "farmin";
    private static final String FTP_PASSWORD = "farmin230130";
    private static final String FTP_LOG_DIRECTORY = "/HDD1/LoRa/fi1030";
    private static final String LOCAL_SAVE_DIRECTORY = "/home/farmin/바탕화면/Catm1/log";

    // DB 설정
    private static final String DB_URL = "jdbc:mysql://192.168.0.20:3306/FarmIn";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "230130";

    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private volatile boolean isProcessing = false;
    private final Set<String> processedFiles = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> processedTimestamps = Collections.synchronizedSet(new HashSet<>());

    private static final Map<String, String> SENSOR_TABLE_MAPPING = Map.of(
            "BoarsSensor", "BoarsID",
            "FinishingSensor", "FinishingID",
            "GestationSensor", "GestationID",
            "GrowingSensor", "GrowingID",
            "MaternitySensor", "MaternityID",
            "PigletSensor", "PigletID",
            "ReserveSensor", "ReserveID"
    );

    private String tableName = "GrowingSensor";

    public Catm1FTPLogFileProcessor() {
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
                }
            } finally {
                isProcessing = false;
            }
        }
    }

    private int getBarnIdFromDB(int sensorId, String tableName) {
        String barnColumn = tableName.replace("Sensor", "ID"); // BoarsSensor → BoarsID, FinishingSensor → FinishingID
        return getRelatedId(barnColumn, tableName, "SensorID", sensorId);
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
                for (FTPFile logFile : logFiles) {
                    if (logFile.getName().endsWith(".log")) {
                        downloadFile(ftpClient, logFile);
                    }
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

    private void convertAllLogFiles() {
        File directory = new File(LOCAL_SAVE_DIRECTORY);
        File[] logFiles = directory.listFiles((dir, name) -> name.endsWith(".log"));

        if (logFiles != null && logFiles.length > 0) {
            Arrays.stream(logFiles).forEach(this::convertLogToCSV);
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

    private void convertLogToCSV(File logFile) {
        String csvFileName = logFile.getAbsolutePath().replace(".log", ".csv");
        File csvFile = new File(csvFileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {

            String line;
            int lineNumber = 0;

            // CSV 헤더 추가
            writer.write("Time,ID,Temper,WTemper,Humidity,Co2");
            writer.newLine();

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] data = line.split("\\s+");  // 공백 기준으로 데이터 분할

                // 최소한 6개의 데이터가 있어야 유효한 행으로 간주
                if (data.length < 6) continue;

                writer.write(String.join(",", data[0], data[1], data[2], data[3], data[4], data[5]));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("CSV 변환 중 오류 발생: " + logFile.getName());
            e.printStackTrace();
        }
    }


    private void downloadFile(FTPClient ftpClient, FTPFile logFile) {
        Path localPath = Paths.get(LOCAL_SAVE_DIRECTORY, logFile.getName());
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localPath.toFile()))) {
            boolean success = ftpClient.retrieveFile(logFile.getName(), outputStream);
            if (success) {
                System.out.println("[다운로드 성공] 파일 다운로드 성공: " + logFile.getName());
            } else {
                System.out.println("[다운로드 실패] 파일 다운로드 실패: " + logFile.getName());
            }
        } catch (IOException e) {
            System.out.println("[파일 저장 오류] 파일 다운로드 및 저장 중 오류 발생: " + logFile.getName());
            e.printStackTrace();
        }
    }


    private void processCSVFile(File csvFile) {
        if (processedFiles.contains(csvFile.getName())) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 7) continue;

                String timestamp = data[0];
                if (!processedTimestamps.add(timestamp)) continue;

                int sensorId = getSensorIdFromDB();
                int snFarmId = getSnFarmIdFromDB(sensorId);
                int barnId = getBarnIdFromDB(sensorId, tableName);
                int farmId = getFarmIdFromDB(snFarmId);
                int userId = getUserIdFromDB(farmId);

                saveSensorDataToDatabase(sensorId, snFarmId, barnId, farmId, userId, timestamp, data[1], data[2], data[3], data[4], data[5]);
            }
            processedFiles.add(csvFile.getName());
        } catch (IOException e) {
            System.out.println("CSV 파일 처리 중 오류 발생: " + csvFile.getName());
            e.printStackTrace();
        }
    }

    private void saveSensorDataToDatabase(int sensorId, int snFarmId, int barnsId,int farmId, int userId, String time, String co2, String temper, String wtemper, String humidity, String sensorId3) {
        String idColumn = SENSOR_TABLE_MAPPING.get(tableName);
        if (idColumn == null) return;

        String insertSQL = "INSERT INTO " + tableName + " (" + idColumn + ", SNFarmID, BarnsID, FarmID, UserID, Time, Co2, Temper, WTemper, Humidity, SensorId3) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setInt(1, sensorId);
            pstmt.setInt(2, snFarmId);
            pstmt.setInt(3, barnsId);
            pstmt.setInt(4, farmId);
            pstmt.setInt(5, userId);
            pstmt.setString(6, time);
            pstmt.setString(7, co2);
            pstmt.setString(8, temper);
            pstmt.setString(9, wtemper);
            pstmt.setString(10, humidity);
            pstmt.setString(11, sensorId3);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("DB 저장 중 오류 발생");
            e.printStackTrace();
        }
    }

    private int getSensorIdFromDB() {
        return getMaxId("SensorID", tableName);
    }

    private int getSnFarmIdFromDB(int sensorId) {
        return getRelatedId("SNFarmID", "Sensor", "SensorID", sensorId);
    }

    private int getFarmIdFromDB(int snFarmId) {
        return getRelatedId("FarmID", "SNFarmInfo", "SNFarmID", snFarmId);
    }

    private int getUserIdFromDB(int farmId) {
        return getRelatedId("UserID", "FarmInfo", "FarmID", farmId);
    }

    private int getMaxId(String idColumn, String table) {
        String sql = "SELECT MAX(" + idColumn + ") FROM " + table;
        return executeIdQuery(sql);
    }

    private int getRelatedId(String targetColumn, String table, String conditionColumn, int conditionValue) {
        String sql = "SELECT " + targetColumn + " FROM " + table + " WHERE " + conditionColumn + " = ?";
        return executeIdQuery(sql, conditionValue);
    }

    private int executeIdQuery(String sql, Object... params) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (params.length > 0) pstmt.setInt(1, (Integer) params[0]);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
