package com.farmin.farminserver.domain.ftp.catm1ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FtpService {
    private final FTPClient ftpClient = new FTPClient();

    public boolean connect(String server, int port, String user, String password) {
        try {
            ftpClient.connect(server, port);
            return ftpClient.login(user, password);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void configure() throws IOException {
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.setConnectTimeout(10000);  // 10초 연결 타임아웃
        ftpClient.setDefaultTimeout(15000);  // 15초 기본 타임아웃
        ftpClient.setDataTimeout(60000);     // 60초 데이터 타임아웃

        // FTP 클라이언트 버퍼 크기 설정
        ftpClient.setBufferSize(1024 * 1024); // 1MB 버퍼

        // FTP 연결 상태 확인 로그
        System.out.println("[FTP] 연결 상태: " + (ftpClient.isConnected() ? "연결됨" : "연결 안됨"));
        System.out.println("[FTP] 현재 작업 디렉토리: " + ftpClient.printWorkingDirectory());
        int replyCode = ftpClient.getReplyCode();
        System.out.println("[FTP] 서버 응답 코드: " + replyCode + ", " + ftpClient.getReplyString());
    }

    public void disconnect() {
        try {
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FTPFile[] listFiles(String path) throws IOException {
        return ftpClient.listFiles(path);
    }

    public boolean downloadFile(String remotePath, String localPath) {
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(localPath))) {
            boolean success = ftpClient.retrieveFile(remotePath, os);
            if (!success) {
                System.err.println("[FTP] 다운로드 실패 - 응답 코드: " + ftpClient.getReplyCode()
                        + ", 메시지: " + ftpClient.getReplyString());
            }
            return success;
        } catch (IOException e) {
            System.err.println("[FTP] 다운로드 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public FTPClient getClient() {
        return ftpClient;
    }

    public String[] listNames(String path) throws IOException {
        return ftpClient.listNames(path);
    }

    public String[] listNames() throws IOException {
        return ftpClient.listNames();
    }

    public String currentDirectory() throws IOException {
        return ftpClient.printWorkingDirectory();
    }

    public String getLastServerResponse() {
        return ftpClient.getReplyString();
    }

    public int getServerReplyCode() {
        return ftpClient.getReplyCode();
    }
}