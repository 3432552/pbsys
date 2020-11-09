package com.hzy.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 工作日志展示Vo
 */
@Data
@Accessors(chain = true)
public class WorkLogVo {
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
     * 工作时长
     */
    private Integer workHours;
    /**
     * 岗位补贴
     */
    private BigDecimal postAllowance;
    //审批状态0:正常【默认】 1:待审批 2:审批成功 3:审批失败
    private Integer status;
    //审批反馈
    private String approvalOpinion;
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
