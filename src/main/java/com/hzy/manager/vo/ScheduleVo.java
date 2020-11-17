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
     * trio岗位人员姓名
     */
    private String trioUseridName;

    /**
     * 大屏和AR岗位人员姓名
     */
    private String vcpMpUseridName;

    /**
     * lv岗位人员姓名
     */
    private String lvUseridName;

    /**
     * trtc岗位人员姓名
     */
    private String trtcUseridName;

    /**
     * 学习和其他
     */
    private String studyOtherUseridName;

    /**
     * 排班备注
     */
    private String remark;
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
