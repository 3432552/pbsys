package com.hzy.manager.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("p_worklog")
public class WorkLog implements Serializable {
    private static final long serialVersionUID = -2796438644466587729L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String workDate;
    private String week;
    private String gameTime;
    private Integer studio;
    private String league;
    private String game;
    private Integer post;
    private Double workHours;
    private BigDecimal postAllowance;
    //审批状态0:正常【默认】 1:待审批 2:审批成功 3:审批失败
    private Integer status;
    //审批反馈
    private String approvalOpinion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;
    //其他表字段
    private transient String realName;
}
