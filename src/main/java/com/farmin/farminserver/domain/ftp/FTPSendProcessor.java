package com.farmin.farminserver.domain.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FTPSendProcessor {
    private static final String FTP_SERVER = "106.10.49.73";
    private static final int FTP_PORT = 9021;
    private static final String FTP_USER = "yuhan0";
    private static final String FTP_PASSWORD = "farmin230130";
    private static final String FTP_UPLOAD_DIRECTORY = "/";
//    private static final String FTP_SERVER = "farm-in.ipdisk.co.kr";
//    private static final int FTP_PORT = 21;
//    private static final String FTP_USER = "farmin";
//    private static final String FTP_PASSWORD = "farmin230130";
//    private static final String FTP_UPLOAD_DIRECTORY = "/HDD1/TEST";

    private static final String LOCAL_LOG_DIRECTORY = "/home/farmin/바탕화면/KIFAPQE";

    private static final String DB_URL = "jdbc:mysql://192.168.0.20:3306/FarmIn";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "230130";

    private static final String[] EQUIPMENT_CODES = {"ES01", "ES02", "ES03", "ES04"};
    private static final String[] SENSOR_COLUMNS = {"Temper", "Humidity", "Co2", "Nh3"};

    private static final String MAKER_ID = "yuhan0";
    private static final String LSIND_REGIST_NO = "TEST01";
    private static final String ITEM_CODE = "P00";
    private static final String STALL_TY_CODE = "SP06";

    private static final DateTimeFormatter FILE_NAME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    static {
        try {
            // MySQL JDBC 드라이버 명시적 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC 드라이버 로드 실패: " + e.getMessage());
        }
    }

    public FTPSendProcessor() {
        File directory = new File(LOCAL_LOG_DIRECTORY);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            System.out.println("로그 디렉토리 생성 " + (created ? "성공" : "실패"));
        }
    }

    @Scheduled(fixedRate = 600000, initialDelay = 10000) // 10분마다 실행
    public void generateAndSendLogFiles() {
        for (int i = 0; i < EQUIPMENT_CODES.length; i++) {
            try {
                System.out.println("Processing equipment code: " + EQUIPMENT_CODES[i]); // 디버깅용 로그 추가
                String filename = generateLogFile(EQUIPMENT_CODES[i], SENSOR_COLUMNS[i]);
                if (filename != null) {
                    uploadFileToFTP(filename);
                } else {
                    System.err.println(EQUIPMENT_CODES[i] + " 장비의 로그 파일 생성 실패");
                }
            } catch (Exception e) {
                System.err.println(EQUIPMENT_CODES[i] + " 장비 처리 중 예외 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String generateLogFile(String equipmentCode, String sensorColumn) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(FILE_NAME_FORMATTER);
            String filename = String.format("%s_%s_%s_%s.log",
                    MAKER_ID, LSIND_REGIST_NO, equipmentCode, formattedDateTime);

            String fullPath = LOCAL_LOG_DIRECTORY + File.separator + filename;

            List<SensorData> sensorDataList = fetchSensorData(conn, sensorColumn);

            if (sensorDataList.isEmpty()) {
                System.err.println(sensorColumn + " 센서의 데이터가 없습니다.");
                return null;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
                for (SensorData data : sensorDataList) {
                    String logEntry = generateLogEntry(data, equipmentCode);
                    writer.write(logEntry);
                    writer.newLine();
                }
                System.out.println("로그 파일 생성 성공: " + filename);
                return filename;
            } catch (IOException e) {
                System.err.println("로그 파일 쓰기 중 오류: " + e.getMessage());
                return null;
            }
        } catch (SQLException e) {
            System.err.println("DB 연결 중 오류: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private List<SensorData> fetchSensorData(Connection conn, String sensorColumn) {
        List<SensorData> sensorDataList = new ArrayList<>();
        String sql = "SELECT GrowingID, Time, " + sensorColumn +
                " FROM GrowingSensor WHERE " + sensorColumn + " IS NOT NULL AND DATE(Time) = CURDATE() ORDER BY Time";

        System.out.println("Executing SQL: " + sql); // SQL 쿼리 로깅

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // 쿼리 결과 로깅 추가
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                SensorData data = new SensorData();
                data.eqpmnNo = String.format("1-%d번", rs.getInt("GrowingID"));
                data.mesureDt = rs.getString("Time");
                data.mesureVal01 = rs.getString(sensorColumn);
                sensorDataList.add(data);

                System.out.println("센서 데이터 추출: " + data.eqpmnNo + ", " + data.mesureDt + ", " + data.mesureVal01);
            }

            System.out.println("Total rows fetched for " + sensorColumn + ": " + rowCount);

            if (sensorDataList.isEmpty()) {
                System.err.println(sensorColumn + " 센서의 데이터가 없습니다.");
            }

        } catch (SQLException e) {
            System.err.println(sensorColumn + " 센서 데이터 쿼리 중 오류: " + e.getMessage());
            e.printStackTrace();
        }

        return sensorDataList;
    }

    private String generateLogEntry(SensorData data, String equipmentCode) {
        LocalDateTime now = LocalDateTime.now();
        return String.format("{ \"lsindRegistNo\" : \"%s\", \"itemCode\" : \"%s\", \"makrId\" : \"%s\", " +
                        "\"eqpmnCode\" : \"%s\", \"eqpmnEsntlSn\" : \"SN-%s\", \"eqpmnNo\" : \"%s\", " +
                        "\"stallTyCode\" : \"%s\", \"stallNo\" : \"01\", \"roomNo\" : \"01\", \"roomDtlNo\" : \"001\", " +
                        "\"mesureDt\" : \"%s\", \"mesureVal01\" : \"%s\", \"mesureVal02\" : \"\", " +
                        "\"mesureVal03\" : \"\", \"mesureVal04\" : \"\", \"mesureVal05\" : \"\", " +
                        "\"mesureVal06\" : \"\", \"mesureVal07\" : \"\", \"mesureVal08\" : \"\", " +
                        "\"mesureVal09\" : \"\", \"mesureVal10\" : \"\", \"mesureVal11\" : \"\", " +
                        "\"mesureVal12\" : \"\", \"mesureVal13\" : \"\", \"mesureVal14\" : \"\", " +
                        "\"mesureVal15\" : \"\" }",
                LSIND_REGIST_NO, ITEM_CODE, MAKER_ID, equipmentCode,
                now.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                data.eqpmnNo, STALL_TY_CODE,
                data.mesureDt,
                data.mesureVal01);
    }

    private void uploadFileToFTP(String filename) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_SERVER, FTP_PORT);
            boolean login = ftpClient.login(FTP_USER, FTP_PASSWORD);

            if (!login) {
                System.err.println("FTP 로그인 실패");
                return;
            }

            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            if (!ftpClient.changeWorkingDirectory(FTP_UPLOAD_DIRECTORY)) {
                System.err.println("FTP 디렉토리 변경 실패");
                return;
            }

            String localFilePath = LOCAL_LOG_DIRECTORY + File.separator + filename;

            try (FileInputStream fileInputStream = new FileInputStream(localFilePath)) {
                boolean uploaded = ftpClient.storeFile(filename, fileInputStream);
                if (uploaded) {
                    System.out.println("파일 업로드 성공: " + filename);
                } else {
                    System.err.println("파일 업로드 실패: " + filename);
                }
            }
        } catch (IOException e) {
            System.err.println("FTP 업로드 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                System.err.println("FTP 연결 종료 중 오류: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static class SensorData {
        String eqpmnNo;
        String mesureDt;
        String mesureVal01;
    }
}