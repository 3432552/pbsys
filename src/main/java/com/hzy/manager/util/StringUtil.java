package com.hzy.manager.util;

import java.util.Arrays;
import java.util.List;

/**
 * 字符串工具类
 */
public class StringUtil {
    /**
     * 判断一个以","分割的字符串是否包含某值
     *
     * @param segmentationString
     * @param uid
     * @return
     */
    public static boolean isContainerUid(String segmentationString, Long uid) {
        String[] values = segmentationString.split(",");
        List<String> list = Arrays.asList(values);
        if (list.contains(String.valueOf(uid))) {
            return true;
        } else {
            return false;
        }
    }
}
