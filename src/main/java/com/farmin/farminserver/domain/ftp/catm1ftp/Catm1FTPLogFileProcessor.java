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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class Catm1FTPLogFileProcessor {
    // FTP 설정
    private static final String FTP_SERVER = "118.42.54.88";
    private static final int FTP_PORT = 2301;
    private static final String FTP_USER = "farmin";
    private static final String FTP_PASSWORD = "230130";
    private static final String FTP_LOG_DIRECTORY = "/home/farmin/ftp/modem";
    private static final String LOCAL_SAVE_DIRECTORY = "/home/farmin/바탕화면/Catm1/log";

    // DB 설정
    private static final String DB_URL = "jdbc:mysql://192.168.0.20:3306/FarmIn";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "230130";

    private final ExecutorService dbExecutor = Executors.newFixedThreadPool(5);
    private volatile boolean isProcessing = false;
    private final Set<String> processedFiles = Collections.synchronizedSet(new HashSet<>());

    private static final Map<String, String> SENSOR_TABLE_MAPPING = Map.of(
            "BoarsSensor", "BoarsID",
            "FinishingSensor", "FinishingID",
            "GestationSensor", "GestationID",
            "GrowingSensor", "GrowingID",
            "MaternitySensor", "MaternityID",
            "PigletSensor", "PigletID",
            "ReserveSensor", "ReserveID"
    );

    // 초기 실행 처리
    public Catm1FTPLogFileProcessor() {
        initialProcess();
    }

    private void initialProcess() {
        System.out.println("[초기 실행] 데이터 다운로드 및 처리 시작");
        if (downloadAllDirectories()) {
            convertAllLogFiles();
            processAllCSVFiles();
            System.out.println("[초기 실행] 데이터 다운로드 및 처리 완료");
        } else {
            System.err.println("[초기 실행] 데이터 다운로드 실패");
        }
    }

    @Scheduled(fixedRate = 300000) // 5분마다 실행
    public void scheduledFTPDownload() {
        if (!isProcessing) {
            try {
                isProcessing = true;
                System.out.println("[스케줄러] 데이터 다운로드 및 처리 시작");
                if (downloadAllDirectories()) {
                    convertAllLogFiles();
                    processAllCSVFiles();
                    System.out.println("[스케줄러] 데이터 다운로드 및 처리 완료");
                } else {
                    System.err.println("[스케줄러] 데이터 다운로드 실패");
                }
            } finally {
                isProcessing = false;
            }
        }
    }

    private boolean downloadAllDirectories() {
        int attempt = 0;
        while (attempt < 3) {
            attempt++;
            FTPClient ftpClient = new FTPClient();
            boolean success = false;

            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                boolean login = ftpClient.login(FTP_USER, FTP_PASSWORD);
                if (!login) {
                    System.err.println("[FTP] 로그인 실패 (시도 " + attempt + "회)");
                    continue;
                }

                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                downloadDirectory(ftpClient, FTP_LOG_DIRECTORY, LOCAL_SAVE_DIRECTORY);
                success = true;
                System.out.println("[FTP] 데이터 다운로드 성공");
                return true;

            } catch (IOException e) {
                System.err.println("[FTP] 다운로드 중 오류 발생 (시도 " + attempt + "회)");
                e.printStackTrace();
            } finally {
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (success) return true;
        }
        System.err.println("[FTP] 모든 시도 실패 - 다운로드 중단");
        return false;
    }

    private void convertAllLogFiles() {
        try {
            Files.walk(Paths.get(LOCAL_SAVE_DIRECTORY))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".log"))
                    .forEach(this::convertLogToCSV);
        } catch (IOException e) {
            System.err.println("[변환 오류] 로그 파일 변환 중 오류 발생");
            e.printStackTrace();
        }
    }

    private void convertLogToCSV(Path logFilePath) {
        String csvFileName = logFilePath.toString().replace(".log", ".csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath.toFile()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {

            // CSV 헤더 추가
            writer.write("Time,ID,Temper,WTemper,Humidity,Co2");
            writer.newLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\s+");
                if (data.length < 6) continue;
                writer.write(String.join(",", data));
                writer.newLine();
            }
            System.out.println("[CSV 변환 성공] " + csvFileName);
        } catch (IOException e) {
            System.err.println("[CSV 변환 오류] 파일: " + logFilePath);
            e.printStackTrace();
        }
    }

    private int executeIdQuery(String sql, Object... params) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 파라미터 설정
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            // SQL 실행 및 결과 반환
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // 첫 번째 컬럼의 정수 값 반환
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB 조회 오류] SQL: " + sql);
            e.printStackTrace();
        }
        return 0; // 조회 실패 시 0 반환
    }

    private void processAllCSVFiles() {
        try {
            Files.walk(Paths.get(LOCAL_SAVE_DIRECTORY))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".csv"))
                    .forEach(csvFile -> dbExecutor.submit(() -> processCSVFile(csvFile)));
        } catch (IOException e) {
            System.err.println("[처리 오류] CSV 파일 처리 중 오류 발생");
            e.printStackTrace();
        }
    }

    private int getSensorIdFromDB() {
        return getMaxId("SensorID", "Sensor");
    }

    private int getSnFarmIdFromDB(int sensorId) {
        return getRelatedId("SNFarmID", "Sensor", "SensorID", sensorId);
    }

    private int getBarnIdFromDB(int sensorId) {
        return getRelatedId("BarnID", "Sensor", "SensorID", sensorId);
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

    private void downloadDirectory(FTPClient ftpClient, String remotePath, String localPath) throws IOException {
        FTPFile[] files = ftpClient.listFiles(remotePath);
        for (FTPFile file : files) {
            String remoteFilePath = remotePath + "/" + file.getName();
            String localFilePath = localPath + "/" + file.getName();
            if (file.isDirectory()) {
                Files.createDirectories(Paths.get(localFilePath));
                downloadDirectory(ftpClient, remoteFilePath, localFilePath);
            } else if (file.getName().endsWith(".log")) {
                downloadFile(ftpClient, remoteFilePath, localFilePath);
            }
        }
    }

    private void downloadFile(FTPClient ftpClient, String remoteFilePath, String localFilePath) {
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFilePath))) {
            boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream);
            if (success) {
                System.out.println("[FTP] 파일 다운로드 성공: " + remoteFilePath);
            } else {
                System.err.println("[FTP] 파일 다운로드 실패: " + remoteFilePath);
            }
        } catch (IOException e) {
            System.err.println("[FTP] 파일 저장 오류: " + remoteFilePath);
            e.printStackTrace();
        }
    }

    private void processCSVFile(Path csvFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) continue;

                int sensorId = getSensorIdFromDB();
                int snFarmId = getSnFarmIdFromDB(sensorId);
                int barnId = getBarnIdFromDB(sensorId);
                int farmId = getFarmIdFromDB(snFarmId);
                int userId = getUserIdFromDB(farmId);

                saveSensorDataToDatabase(sensorId, snFarmId, barnId, farmId, userId, data[0], data[1], data[2], data[3], data[4], data[5]);
            }
            System.out.println("[CSV] 파일 처리 완료: " + csvFilePath);
        } catch (IOException e) {
            System.err.println("[CSV] 파일 처리 오류: " + csvFilePath);
            e.printStackTrace();
        }
    }

    private void saveSensorDataToDatabase(int sensorId, int snFarmId, int barnId, int farmId, int userId, String time, String co2, String temper, String wtemper, String humidity, String sensorId3) {
        String insertSQL = "INSERT INTO GrowingSensor (SensorID, SNFarmID, BarnID, FarmID, UserID, Time, Co2, Temper, WTemper, Humidity, SensorId3) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, sensorId);
            pstmt.setInt(2, snFarmId);
            pstmt.setInt(3, barnId);
            pstmt.setInt(4, farmId);
            pstmt.setInt(5, userId);
            pstmt.setString(6, time);
            pstmt.setString(7, co2);
            pstmt.setString(8, temper);
            pstmt.setString(9, wtemper);
            pstmt.setString(10, humidity);
            pstmt.setString(11, sensorId3);
            pstmt.executeUpdate();
            System.out.println("[DB] 데이터 저장 성공");
        } catch (SQLException e) {
            System.err.println("[DB] 데이터 저장 실패");
            e.printStackTrace();
        }
    }
}
