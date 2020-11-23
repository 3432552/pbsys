package com.hzy.manager.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 排班管理员排班
 * </p>
 *
 * @author wzh
 * @since 2020-10-14
 */
@Data
@Accessors(chain = true)
@TableName("p_schedule")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
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
     * 演播室
     */
    private Integer studio;

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
    private Integer trioUserid;

    /**
     * 大屏和AR岗位人员姓名
     */
    private Integer vcpMpUserid;

    /**
     * lv岗位人员姓名
     */
    private Integer lvUserid;

    /**
     * trtc岗位人员姓名
     */
    private Integer trtcUserid;

    /**
     * 学习和其他
     */
    private String studyOtherUserid;

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
