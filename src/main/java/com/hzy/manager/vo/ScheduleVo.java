package com.hzy.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 排版信息展示vo
 */
@Data
@Accessors(chain = true)
public class ScheduleVo {
    private Long id;
    /**
     * 工作时间
     */
    private String workDate;

    /**
     * 星期几
     */
    private String week;

    /**
     * 比赛时间
     */
    private String gameTime;
    /**
     * 外键,关联用户id
     */
    private Long userId;
    /**
     * 用户真实姓名
     */
    private String realName;
    /**
     * 演播室key
     */
    private Integer studioKey;
    /**
     * 演播室值
     */
    private String studioValue;

    /**
     * 联赛
     */
    private String league;

    /**
     * 比赛
     */
    private String game;
    /**
     * 岗位key
     */
    private Integer postKey;
    /**
     * 岗位值
     */
    private String postValue;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;
}
