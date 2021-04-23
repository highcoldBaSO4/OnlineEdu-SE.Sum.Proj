package com.se231.onlineedu.util;

import com.se231.onlineedu.exception.BulkImportDataException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @author YuxuanLiu
 *
 * @date 2019/07/12
 */
public class SaveFileUtil {
    public static String saveFile(MultipartFile multipartFile, String suffix) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + suffix;


        FtpClientUtil ftpClientUtil = new FtpClientUtil();
        ftpClientUtil.open();

        ftpClientUtil.putFileToPath(multipartFile.getInputStream(), fileName);
        ftpClientUtil.close();

        return fileName;
    }

    public static String saveFile(InputStream inputStream, String suffix) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + suffix;


        FtpClientUtil ftpClientUtil = new FtpClientUtil();
        ftpClientUtil.open();

        ftpClientUtil.putFileToPath(inputStream, fileName);
        ftpClientUtil.close();

        return fileName;
    }


    public static List<String> saveImages(MultipartFile[] multipartFiles, int limit) throws IOException {
        int num = 1;
        List<String> strings = new ArrayList<>();
        StringBuilder errorString = new StringBuilder();
        boolean hasError = false;
        for (MultipartFile multipartFile : multipartFiles) {
            if (FileCheckUtil.checkImageSizeExceed(multipartFile, limit)) {
                hasError = true;
                errorString.append("image " + num + " size Exceed");
            }
            String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
            if (FileCheckUtil.checkImageTypeWrong(suffix)) {
                hasError = true;
                errorString.append("image " + num + " format error");
            }
            strings.add(SaveFileUtil.saveFile(multipartFile, suffix));
        }
        if(!hasError) {
            return strings;
        }
        else {
            throw new BulkImportDataException(errorString.toString());
        }
    }

    public static boolean deleteImage(String fileName) throws IOException {
        FtpClientUtil ftpClientUtil = new FtpClientUtil();
        ftpClientUtil.open();
        ftpClientUtil.deleteByPath(fileName);
        ftpClientUtil.close();
        return true;
    }
}
