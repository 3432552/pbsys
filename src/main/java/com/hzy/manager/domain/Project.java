package com.hzy.manager.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wzh
 * @since 2020-09-08
 */
@Data
@Accessors(chain = true)
@TableName("p_project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 项目id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目地点
     */
    private String projectLocation;

    /**
     * 项目开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**
     * 项目结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 项目描述
     */
    private String describle;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    //一对多关系
    @TableField(exist = false)
    private List<ProjectUser> projectUserList;
}
