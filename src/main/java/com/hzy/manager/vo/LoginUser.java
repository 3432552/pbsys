package com.hzy.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1844673109232208059L;
    private Long id;
    private String userName;
    private String password;
    private Long deptId;
    private String realName;
    private String phone;
    private String status;
    private String sex;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;
    private String avatarUrl;
    private String describle;
    private transient String code;
}
