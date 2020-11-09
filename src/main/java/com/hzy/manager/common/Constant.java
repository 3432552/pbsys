package com.hzy.manager.common;

/**
 * 常量
 */
public class Constant {
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
     * 菜单0,按钮1
     */
    public static final String MENU = "0";
    public static final String BUTTON = "1";
    /**
     * 审批状态0:正常【默认】 1:待审批 2:审批成功 3:审批失败
     */
    public static final Integer APPROVALDEFAULT = 0;
    public static final Integer APPROVALPENDING = 1;
    public static final Integer APPROVALSUCCESS = 2;
    public static final Integer APPROVALFAIL = 3;

}
