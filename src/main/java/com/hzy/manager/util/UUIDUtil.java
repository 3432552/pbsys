package com.hzy.manager.util;


import java.util.UUID;

/**
 * @Description TODO
 * @Author cwd
 * @Date 2020/4/28 10:14
 */
public class UUIDUtil {

    /**
     * 获得一个去掉"-"符号的UUID
     *
     * @return
     */
    public static String uuid() {
        String s = UUID.randomUUID().toString();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);


    }

    public static void main(String[] args) {
        String uuid = uuid();
        System.out.println(uuid);
    }
}
