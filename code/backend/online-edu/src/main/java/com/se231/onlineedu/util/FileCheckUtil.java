package com.se231.onlineedu.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.excel.EasyExcelFactory;
import com.se231.onlineedu.exception.EmptyFileException;
import com.se231.onlineedu.exception.FileFormatNotSupportException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author liu
 *
 * @date 2019/07/11
 */
public class FileCheckUtil {
    private static String imageFileExtension = ".jpg,.jpeg,.png,.svg,.tif";

    private static String videoFileExtension = ".avi.mpg.mlv.mpe.mpeg.dat.mp4";

    static public Boolean checkImageSizeExceed(MultipartFile multipartFile, int limit){
        return multipartFile.getSize() > limit;
    }

    static public Boolean checkImageTypeWrong(String suffix){
        return !imageFileExtension.contains(suffix);
    }

    static public Boolean checkVideoTypeWrong(String suffix){
        return !videoFileExtension.contains(suffix);
    }

    static public void checkExcelValid(MultipartFile multipartFile) throws IOException {
            Workbook workbook = null;
            //获取文件名字
            String fileName = multipartFile.getOriginalFilename();
            //判断后缀
            if (fileName.endsWith("xls")) {
                workbook = new HSSFWorkbook(multipartFile.getInputStream());
            } else if (fileName.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(multipartFile.getInputStream());
            } else {
                throw new FileFormatNotSupportException("Import File Fail -> File Format Wrong,Only Support Xlsx And Xls");
            }
            //获取工作sheet
            Sheet sheet = workbook.getSheet("sheet1");
            int rows = sheet.getLastRowNum();
            if (rows == 0) {
                throw new EmptyFileException("File Error -> File Is Empty.");
            }
    }
}
