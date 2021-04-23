package com.se231.onlineedu.util;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;


/**
 * @author yuxuanLiu
 * @date 2019/07/28
 */
public class FtpClientUtil {
    @Value("${app.ftp.server}")
    private String server = "10.0.0.129";

    @Value("${app.ftp.port}")
    private int port = 21;

    @Value("${app.ftp.user}")
    private String user = "ubuntu";

    @Value("${app.ftp.password}")
    private String password = "abcd1234";

    private FTPClient ftp;

    public FtpClientUtil() {
    }

    public void open() throws IOException {
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(server, port);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftp.login(user, password);
        ftp.enterLocalPassiveMode(); // important!
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
    }

    public void close() throws IOException {
        ftp.disconnect();
    }

    public void putFileToPath(InputStream inputStream, String path) throws IOException {
        ftp.storeFile(path, inputStream);
        ftp.sendSiteCommand("chmod " + "755" + " " + path);
    }

    public void deleteByPath(String path) throws IOException {
        ftp.deleteFile(path);
    }
}
