package com.farmin.farminserver.utill;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 파일 권한 문제를 해결하고 직접 CSV 파일을 생성하는 유틸리티
 */
public class DirectCsvGenerator {

    public static void main(String[] args) {
        // 기본 경로 설정
        String baseDir = "/home/farmin/바탕화면/Catm1/log";
        String outputDir = "/home/farmin/바탕화면/Catm1/fixed_csv";

        if (args.length > 0) {
            baseDir = args[0];
        }
        if (args.length > 1) {
            outputDir = args[1];
        }

        System.out.println("직접 CSV 생성 시작");
        System.out.println("입력 디렉토리: " + baseDir);
        System.out.println("출력 디렉토리: " + outputDir);

        // 출력 디렉토리 생성 (권한 설정)
        try {
            createDirectoryWithPermissions(outputDir);
            System.out.println("출력 디렉토리 생성 성공 (권한: rwxrwxrwx)");
        } catch (Exception e) {
            System.err.println("출력 디렉토리 생성 실패: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 로그 파일 처리
        try {
            processLogFiles(baseDir, outputDir);
        } catch (Exception e) {
            System.err.println("처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 전체 권한으로 디렉토리 생성
     */
    private static void createDirectoryWithPermissions(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);

        if (!Files.exists(path)) {
            // 디렉토리 생성
            Files.createDirectories(path);

            try {
                // 모든 사용자에게 모든 권한 부여 (chmod 777)
                Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
                Files.setPosixFilePermissions(path, perms);
            } catch (UnsupportedOperationException e) {
                // Windows 시스템이나 POSIX를 지원하지 않는 환경에서는 건너뜀
                System.out.println("POSIX 권한 설정이 지원되지 않는 환경입니다.");
            }
        }
    }

    /**
     * 로그 디렉토리의 모든 .log 파일을 처리
     */
    private static void processLogFiles(String baseDir, String outputDir) throws IOException {
        // 결과 요약 카운터
        int totalLogFiles = 0;
        int successCount = 0;
        int failCount = 0;

        // 모든 .log 파일 검색
        Path basePath = Paths.get(baseDir);
        List<Path> logFiles = new ArrayList<>();

        if (Files.exists(basePath)) {
            Files.walkFileTree(basePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".log")) {
                        logFiles.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.println("파일 접근 실패: " + file + " - " + exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            throw new IOException("입력 디렉토리가 존재하지 않습니다: " + baseDir);
        }

        totalLogFiles = logFiles.size();
        System.out.println("총 " + totalLogFiles + "개의 로그 파일을 발견했습니다.");

        // 각 로그 파일 처리
        for (Path logFile : logFiles) {
            try {
                boolean success = convertLogToCSV(logFile, outputDir);
                if (success) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
                System.err.println("파일 변환 실패: " + logFile + " - " + e.getMessage());
            }
        }

        System.out.println("\n===== 변환 결과 요약 =====");
        System.out.println("총 로그 파일: " + totalLogFiles);
        System.out.println("변환 성공: " + successCount);
        System.out.println("변환 실패: " + failCount);

        // 새로 생성된 CSV 파일 목록 출력
        System.out.println("\n===== 생성된 CSV 파일 =====");
        Path outputPath = Paths.get(outputDir);
        if (Files.exists(outputPath)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(outputPath, "*.csv")) {
                int fileCount = 0;
                for (Path csvFile : stream) {
                    fileCount++;
                    System.out.println(csvFile.getFileName() + " (" + Files.size(csvFile) + " 바이트)");
                }
                System.out.println("총 " + fileCount + "개의 CSV 파일이 생성되었습니다.");
            }
        }
    }

    /**
     * 로그 파일을 CSV로 변환 (출력 디렉토리에 직접 생성)
     */
    private static boolean convertLogToCSV(Path logFile, String outputDir) throws IOException {
        // 입력 파일 확인
        if (!Files.exists(logFile) || Files.size(logFile) == 0) {
            System.err.println("빈 파일 또는 존재하지 않는 파일: " + logFile);
            return false;
        }

        // 원본 파일 경로에서 필요한 정보 추출
        String fileName = logFile.getFileName().toString();
        String csvFileName = fileName.replace(".log", ".csv");

        // 출력 경로 준비
        Path outputPath = Paths.get(outputDir, csvFileName);

        System.out.println("\n처리 중: " + logFile);
        System.out.println("출력: " + outputPath);

        // 파일 내용 샘플 출력
        printFileSample(logFile);

        // 먼저 메모리에 내용 처리
        List<String> outputLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(logFile.toFile()), StandardCharsets.UTF_8))) {

            String line;
            int lineCount = 0;
            int successCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;

                // 라인 처리
                String processedLine = processLogLine(line);

                if (processedLine != null) {
                    outputLines.add(processedLine);
                    successCount++;
                }
            }

            System.out.println("처리된 라인: " + lineCount + ", 성공: " + successCount);

            // 성공한 라인이 없으면 실패로 간주
            if (successCount == 0) {
                System.err.println("변환할 데이터가 없습니다.");
                return false;
            }
        }

        // 결과 파일 작성 (여러 방법으로 시도)
        boolean writeSuccess = false;

        // 방법 1: Files API 사용
        try {
            // 모든 권한으로 디렉토리 생성
            createDirectoryWithPermissions(outputPath.getParent().toString());

            // 파일 작성
            Files.write(outputPath, outputLines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            // 파일에 모든 권한 부여
            try {
                Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-rw-rw-");
                Files.setPosixFilePermissions(outputPath, perms);
            } catch (UnsupportedOperationException e) {
                // Windows 등 POSIX를 지원하지 않는 환경
                System.out.println("POSIX 권한 설정이 지원되지 않는 환경입니다.");
            }

            writeSuccess = true;
            System.out.println("방법 1로 파일 작성 성공: " + outputPath);
        } catch (IOException e) {
            System.err.println("방법 1 실패: " + e.getMessage());
            // 계속 다음 방법 시도
        }

        // 방법 2: FileWriter 사용 (실패 시)
        if (!writeSuccess) {
            try (FileWriter writer = new FileWriter(outputPath.toFile())) {
                for (String line : outputLines) {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
                writeSuccess = true;
                System.out.println("방법 2로 파일 작성 성공: " + outputPath);
            } catch (IOException e) {
                System.err.println("방법 2 실패: " + e.getMessage());
            }
        }

        // 방법 3: PrintWriter 사용 (실패 시)
        if (!writeSuccess) {
            try (PrintWriter writer = new PrintWriter(outputPath.toFile(), StandardCharsets.UTF_8.name())) {
                for (String line : outputLines) {
                    writer.println(line);
                }
                writeSuccess = true;
                System.out.println("방법 3으로 파일 작성 성공: " + outputPath);
            } catch (IOException e) {
                System.err.println("방법 3 실패: " + e.getMessage());
            }
        }

        // 파일 작성 결과 확인
        if (writeSuccess && Files.exists(outputPath)) {
            long fileSize = Files.size(outputPath);
            if (fileSize > 0) {
                System.out.println("CSV 파일 생성 성공: " + outputPath + " (" + fileSize + " 바이트)");
                return true;
            } else {
                System.err.println("CSV 파일이 비어 있습니다: " + outputPath);
                return false;
            }
        } else {
            System.err.println("CSV 파일 생성 실패: " + outputPath);
            return false;
        }
    }

    /**
     * 로그 파일의 샘플 내용 출력
     */
    private static void printFileSample(Path file) throws IOException {
        System.out.println("파일 샘플 내용:");
        System.out.println("파일 크기: " + Files.size(file) + " 바이트");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file.toFile()), StandardCharsets.UTF_8))) {

            int lineCount = 0;
            String line;

            while ((line = reader.readLine()) != null && lineCount < 5) {
                System.out.println("라인 " + (lineCount + 1) + ": '" + line + "'");
                lineCount++;
            }
        }
    }

    /**
     * 로그 파일의 한 라인을 CSV 형식으로 처리
     */
    private static String processLogLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        try {
            // 쉼표 구분 형식 (순번,시간,온도,습도,수온,CO2)
            if (line.contains(",")) {
                String[] parts = line.trim().split(",");
                if (parts.length >= 6) {
                    // 순번 제외, 나머지 필드 사용
                    String time = parts[1].trim();
                    String temp = formatValue(parts[2].trim());
                    String humidity = parts[3].trim();
                    String wtemp = formatValue(parts[4].trim());
                    String co2 = parts[5].trim();

                    return String.format("%s,%s,%s,%s,%s", time, temp, humidity, wtemp, co2);
                } else {
                    System.err.println("잘못된 형식 (쉼표 구분): " + line);
                    return null;
                }
            }

            // 공백 구분 형식 (시간 온도 습도 수온 CO2)
            String[] parts = line.trim().split("\\s+");
            if (parts.length >= 5) {
                String time = parts[0].trim();
                String temp = formatValue(parts[1].trim());
                String humidity = parts[2].trim();
                String wtemp = formatValue(parts[3].trim());
                String co2 = parts[4].trim();

                return String.format("%s,%s,%s,%s,%s", time, temp, humidity, wtemp, co2);
            }

            System.err.println("지원되지 않는 형식: " + line);
            return null;
        } catch (Exception e) {
            System.err.println("라인 처리 오류: " + line + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * 값을 적절히 포맷팅 (온도/수온 스케일링)
     */
    private static String formatValue(String value) {
        try {
            float num = Float.parseFloat(value);
            // 온도나 수온이 100 이상이면 10으로 나누기
            if (num > 100) {
                num /= 10.0f;
            }
            return String.format("%.1f", num);
        } catch (NumberFormatException e) {
            return "0.0";
        }
    }
}
