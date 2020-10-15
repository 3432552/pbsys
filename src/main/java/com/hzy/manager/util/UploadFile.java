package com.hzy.manager.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Author: wzh
 * @ClassName: UploadFile
 * @Description: 文件上传工具类结合MVC使用
 * @Date: 2020/5/12 20:33
 */
public class UploadFile {
    private static final Logger log = LoggerFactory.getLogger(UploadFile.class);

    /**
     * 上传单文件
     *
     * @param file             文件
     * @param saveFilePath     保存文件的相对路径
     * @param originalFileName 上传文件的文件名
     */
    public static void uploadFileOne(MultipartFile file, String saveFilePath, String originalFileName) {
        try {
            File file2 = new File(saveFilePath, originalFileName);
            log.info("保存到服务器上面的绝对路径:" + file2.getPath());
            //保存的路径如果不存在会自动创建文件夹
            if (!file2.exists()) {
                file2.mkdirs();
                file2.createNewFile();
            }
            //保存到服务器
            file.transferTo(file2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
