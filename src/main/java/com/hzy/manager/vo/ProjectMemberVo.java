package com.hzy.manager.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 项目下面的成员信息和工时vo
 */
@Data
public class ProjectMemberVo implements Serializable {
    //项目用户关联表主键id
    private Long id;
    //项目id
    private Long projectId;
    //工作时长/时
    private String workingTime;
    //用户真实姓名
    private String realName;
    //部门名称
    private String deptName;
}
