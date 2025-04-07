package com.farmin.farminserver.domain.ftp.catm1ftp;

import java.io.*;
import java.nio.file.Path;

public class LogConverter {
    public void convertLogToCSV(Path logPath) {
        String csvPath = logPath.toString().replace(".log", ".csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(logPath.toFile()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath, true))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] rawData = line.trim().split(",\\s*");

                if (rawData.length < 6) continue;

                // 순번은 무시 (index 0), 시간은 그대로, 온도/수온은 1/10 처리
                String time = rawData[1];
                double temp = parseAndScale(rawData[2]);
                String humidity = rawData[3];
                double wtemp = parseAndScale(rawData[4]);
                String co2 = rawData[5];

                // CSV 형식: 시간, 온도, 습도, 수온, Co2
                String converted = String.join(",", time,
                        String.valueOf(temp),
                        humidity,
                        String.valueOf(wtemp),
                        co2);

                writer.write(converted);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[LOG] 변환 중 오류: " + logPath);
            e.printStackTrace();
        }
    }

    private double parseAndScale(String val) {
        try {
            return Double.parseDouble(val) / 10.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
