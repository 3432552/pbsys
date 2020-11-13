package com.hzy.manager.dto;

import lombok.Data;

@Data
public class ProjectDto {
    private String projectName;
    private Integer currentNo;
    private Integer pageSize;
}
