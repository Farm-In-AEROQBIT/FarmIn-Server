package com.farmin.farminserver.domain.ftp.catm1ftp;

import java.sql.*;

public class DbUtils {
    private static final String DB_URL = "jdbc:mysql://211.230.2.77:3306/FarmIn";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "230130";

    public static int getSensorId(String tableName, String idColumn) {
        String sql = "SELECT MAX(" + idColumn + ") FROM " + tableName;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void insertSensorData(String table, int sensorId, String[] data) {
        String sql = "INSERT INTO " + table + " (SensorID, Time, Temper, Humidity, WTemper, Co2, SensorIdc) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sensorId);          // SensorID
            stmt.setString(2, data[0]);        // Time
            stmt.setString(3, data[1]);        // Temper
            stmt.setString(4, data[2]);        // Humidity
            stmt.setString(5, data[3]);        // WTemper
            stmt.setString(6, data[4]);        // Co2
            stmt.setString(7, "catm1");        // SensorIdc - 구분용 고정값

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] 삽입 실패");
            e.printStackTrace();
        }
    }

}
