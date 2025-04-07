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
            return ftpClient.retrieveFile(remotePath, os);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public FTPClient getClient() {
        return ftpClient;
    }
}
