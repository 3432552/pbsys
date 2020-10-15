package com.hzy.manager.dto;

import lombok.Data;

@Data
public class ScheduleDto {
    private Long userId;
    private String startTime;
    private String endTime;
}
