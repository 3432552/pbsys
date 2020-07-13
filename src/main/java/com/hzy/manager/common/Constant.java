package com.hzy.manager.common;

/**
 * 常量
 */
public class Constant {
    /**
     * token 缓存前缀(只用来存放当前操作用户的token)
     */
    public static final String TOKEN_CACHE_KEY = "cacheToken**@#$%#@Q$%";

    /**
     * 登录用户信息缓存前缀
     */
    public static final String USER_CACHE = "smooth";
    /**
     * 验证码前缀
     */
    public static final String CODE_KEY = "code";
    /**
     * 账户 0正常 1锁定
     */
    public static final String NORMAL = "0";
    public static final String LOCK = "1";
    /**
     * 性别 0男 1女
     */
    public static final String MAN = "0";
    public static final String WOMAN = "1";
    /**
     * 验证码图形 长,宽,高
     */
    public static final Integer WIDTH = 146;
    public static final Integer HEIGHT = 33;
    public static final Integer LEN = 4;
    /**
     * 菜单0,按钮1
     */
    public static final String MENU = "0";
    public static final String BUTTON = "1";
    /**
     * 审批状态 0:待审批(数据库默认值就是0) 1:审批通过 2:审批不通过
     */
    public static final String APPROVALPASS = "1";
    public static final String APPROVALNOPASS = "2";
}
