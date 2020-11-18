package com.hzy.manager.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long deptId;
    private String realName;
    private Integer status;
    private Integer currentNo;
    private Integer pageSize;
}
