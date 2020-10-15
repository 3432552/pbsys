package com.hzy.manager.common.properties;

public class ShiroProperties {

    private String anonUrl;

    /**
     * token默认有效时间 30分钟
     */
    private Long tokenTimeOut;

    public String getAnonUrl() {
        return anonUrl;
    }

    public void setAnonUrl(String anonUrl) {
        this.anonUrl = anonUrl;
    }

    public Long getTokenTimeOut() {
        return tokenTimeOut;
    }

    public void setTokenTimeOut(Long tokenTimeOut) {
        this.tokenTimeOut = tokenTimeOut;
    }
}
