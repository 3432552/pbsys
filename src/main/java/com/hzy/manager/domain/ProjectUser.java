package com.hzy.manager.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("p_project_user")
public class ProjectUser implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 项目用户关联表主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 工作时长/时
     */
    private String workingTime;

}
