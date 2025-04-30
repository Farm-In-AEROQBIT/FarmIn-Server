package com.farmin.farminserver.domain.ftp.catm1ftp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogConverter {
    public void convertLogToCSV(Path logPath) {
        String csvPath = logPath.toString().replace(".log", ".csv");
        // 파일 확장자가 .log가 아닌 경우를 처리
        if (!csvPath.endsWith(".csv")) {
            csvPath = logPath.toString() + ".csv";
        }

        System.out.println("[LOG] 변환 시작: " + logPath + " -> " + csvPath);

        try {
            // 먼저 파일 내용 출력 (디버깅)
            byte[] fileBytes = Files.readAllBytes(logPath);
            String fileContent = new String(fileBytes, StandardCharsets.UTF_8);

            if (fileContent.isEmpty()) {
                System.err.println("[LOG] 로그 파일이 비어 있습니다: " + logPath);
                return;
            }

            System.out.println("[LOG] 로그 파일 첫 200바이트 샘플: " +
                    fileContent.substring(0, Math.min(200, fileContent.length())));

            // 다양한 구분자 검출
            String[] possibleSeparators = {",", " ", "\t", ";"};
            String detectedSeparator = null;

            // 첫 번째 줄로 구분자 검출
            String firstLine = fileContent.split("\n")[0].trim();
            System.out.println("[LOG] 첫 번째 줄: " + firstLine);

            // 각 구분자 시도
            for (String separator : possibleSeparators) {
                String[] parts = firstLine.split(separator);
                if (parts.length >= 5) {  // 최소 5개 항목 (시간,온도,습도,수온,CO2)
                    detectedSeparator = separator;
                    System.out.println("[LOG] 검출된 구분자: '" + detectedSeparator + "', 항목 수: " + parts.length);
                    break;
                }
            }

            // 구분자를 찾지 못한 경우 기본 구분자 사용
            if (detectedSeparator == null) {
                detectedSeparator = " ";  // 공백을 기본 구분자로 사용
                System.out.println("[LOG] 구분자를 검출하지 못했습니다. 기본 구분자 ' ' 사용");
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(logPath.toFile()));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath, false))) {

                String line;
                int lineCount = 0;
                int processedCount = 0;

                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    // 디버깅: 원시 데이터 출력
                    if (lineCount <= 3) {
                        System.out.println("[LOG] 라인 " + lineCount + ": " + line);
                    }

                    // 여러 구분자 시도
                    String[] rawData = line.split(detectedSeparator + "+");

                    // 항목이 부족한 경우 다른 구분자 시도
                    if (rawData.length < 5) {
                        // 구분자를 공백으로 다시 시도
                        rawData = line.split("\\s+");

                        if (lineCount <= 3) {
                            System.out.println("[LOG] 공백으로 분할 후 항목 수: " + rawData.length);
                        }
                    }

                    // 여전히 항목이 부족한 경우 - 특수한 형식 처리
                    if (rawData.length < 5) {
                        // 시간만 있는 경우 (나머지는 기본값)
                        if (rawData.length >= 1 && rawData[0].matches("\\d{2}:\\d{2}:\\d{2}")) {
                            String[] expandedData = new String[5];
                            expandedData[0] = rawData[0];  // 시간
                            expandedData[1] = "0";         // 온도 (기본값)
                            expandedData[2] = "0";         // 습도 (기본값)
                            expandedData[3] = "0";         // 수온 (기본값)
                            expandedData[4] = "0";         // CO2 (기본값)
                            rawData = expandedData;
                        } else {
                            System.out.println("[LOG] 라인 형식 오류, 건너뜀: " + line);
                            continue;
                        }
                    }

                    // 데이터 추출 및 처리
                    String time;
                    double temp;
                    String humidity;
                    double wtemp;
                    String co2;

                    try {
                        // 형식에 따라 데이터 추출
                        if (rawData.length >= 6 && rawData[0].matches("\\d+")) {
                            // 순번, 시간, 온도, 습도, 수온, CO2 형식
                            time = rawData[1];
                            temp = parseAndScale(rawData[2]);
                            humidity = rawData[3];
                            wtemp = parseAndScale(rawData[4]);
                            co2 = rawData[5];
                        } else {
                            // 시간, 온도, 습도, 수온, CO2 형식
                            time = rawData[0];
                            temp = parseAndScale(rawData[1]);
                            humidity = rawData[2];
                            wtemp = parseAndScale(rawData[3]);
                            co2 = rawData[4];
                        }

                        // 시간 형식 확인 및 수정
                        if (!time.matches("\\d{2}:\\d{2}:\\d{2}")) {
                            // 숫자만 있는 경우 시:분:초 형식으로 변환
                            if (time.matches("\\d+")) {
                                int timeValue = Integer.parseInt(time);
                                int hours = timeValue / 10000;
                                int minutes = (timeValue % 10000) / 100;
                                int seconds = timeValue % 100;
                                time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                            }
                        }

                        // CSV 형식: 시간, 온도, 습도, 수온, Co2
                        String converted = String.join(",", time,
                                String.format("%.1f", temp),
                                humidity,
                                String.format("%.1f", wtemp),
                                co2);

                        writer.write(converted);
                        writer.newLine();
                        processedCount++;
                    } catch (Exception e) {
                        System.err.println("[LOG] 라인 " + lineCount + " 처리 중 오류: " + e.getMessage());
                    }
                }

                System.out.println("[LOG] 변환 완료: " + logPath + " -> " + csvPath);
                System.out.println("[LOG] 처리된 라인 수: " + processedCount + " / " + lineCount);
            }
        } catch (IOException e) {
            System.err.println("[LOG] 변환 중 오류: " + logPath);
            e.printStackTrace();
        }
    }

    private double parseAndScale(String val) {
        try {
            double value = Double.parseDouble(val.trim());
            // 값이 100보다 크면 10으로 나누어 스케일링 (온도/수온 값 보정)
            if (value > 100) {
                return value / 10.0;
            }
            return value;
        } catch (NumberFormatException e) {
            System.err.println("[LOG] 숫자 변환 오류: '" + val + "'");
            return 0.0;
        }
    }
}