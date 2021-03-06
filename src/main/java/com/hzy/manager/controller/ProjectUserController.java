package com.hzy.manager.controller;


import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Project;
import com.hzy.manager.domain.ProjectUser;
import com.hzy.manager.service.ProjectUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 项目用户关联前端控制器
 * </p>
 *
 * @author wzh
 * @since 2020-09-08
 */
@Slf4j
@Api(tags = "项目用户控制类")
@RestController
@RequestMapping("/projectuser")
public class ProjectUserController {
    @Autowired
    private ProjectUserService projectUserService;

    @ApiOperation(value = "查询参与项目下面的成员和工作时长")
    @ApiImplicitParam(name = "projectId", value = "项目id", required = true, dataType = "Long")
    @PostMapping("/selectMember")
    public Result selectMemberMes(@RequestBody ProjectUser projectUser) {
        List<ProjectUser> projectUserList = null;
        try {
            projectUserList = projectUserService.getMemberInfo(projectUser.getProjectId());
        } catch (Exception e) {
            log.info("查询项目下面人员失败");
            e.printStackTrace();
            return Result.error("查询项目下面人员失败");
        }
        return Result.ok(projectUserList);
    }
}
