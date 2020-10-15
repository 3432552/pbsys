package com.hzy.manager.util;

import java.text.DecimalFormat;

/**
 * @Author: wzh
 * @ClassName: FileSizeBack
 * @Description:
 * @Date: 2020/5/6 11:41
 */
public class FileSizeBack {
    /**
     * 文件大小，返回kb.mb
     *
     * @return
     */
    public static String getSize(long fileLength) {
        String size = "";
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileLength < 1024) {
            size = df.format((double) fileLength) + "BT";
        } else if (fileLength < 1048576) {
            size = df.format((double) fileLength / 1024) + "KB";
        } else if (fileLength < 1073741824) {
            size = df.format((double) fileLength / 1048576) + "MB";
        } else {
            size = df.format((double) fileLength / 1073741824) + "GB";
        }
        return size;
    }
}
