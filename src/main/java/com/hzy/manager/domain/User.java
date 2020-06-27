package com.hzy.manager.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("p_user")
public class User implements Serializable {
    private static final long serialVersionUID = -8219816649414959406L;
    @TableId(value = "id", type = IdType.AUTO)
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
    private Dept dept;
}
