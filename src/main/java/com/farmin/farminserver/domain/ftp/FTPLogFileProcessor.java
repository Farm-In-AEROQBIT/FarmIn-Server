package com.farmin.farminserver.domain.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;

@Service
public class FTPLogFileProcessor {

    private static final String FTP_SERVER = " ftp://farm-in.ipdisk.co.kr";
    private static final String FTP_USER = "farmin";
    private static final String FTP_PASSWORD = "farmin230130";
    private static final String FTP_LOG_DIRECTORY = "HDD1/monitoring/fi1030";  // FTP 서버의 로그 파일 경로
    private static final String LOCAL_SAVE_DIRECTORY = "/your/desired/path";  // 로컬에 저장할 폴더 경로

    private static final String DB_URL = "jdbc:mysql://localhost:3306/farmin-server";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "honggyo2830";

    // 1시간마다 FTP 파일 다운로드
    @Scheduled(fixedRate = 3600000)
    public void downloadAndProcessLogs() {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(FTP_SERVER);
            ftpClient.login(FTP_USER, FTP_PASSWORD);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(FTP_LOG_DIRECTORY);

            String[] logFiles = ftpClient.listNames("*.log");
            if (logFiles != null) {
                for (String logFile : logFiles) {
                    File localFile = new File(LOCAL_SAVE_DIRECTORY + "/" + logFile);
                    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFile))) {
                        if (ftpClient.retrieveFile(logFile, outputStream)) {
                            System.out.println(logFile + " 다운로드 성공");
                            processLogFileAndSaveToDB(localFile);  // DB에 저장
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 로그 파일을 읽어 DB에 저장
    private void processLogFileAndSaveToDB(File logFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                saveSensorDataToDatabase(line);  // 각 줄을 Sensor 테이블에 저장
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sensor 테이블에 데이터를 저장
    private void saveSensorDataToDatabase(String logLine) {
        String insertSQL = "INSERT INTO your_sensor_table (sensor_data_column) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, logLine);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
