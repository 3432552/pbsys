package com.hzy.manager.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Approval;
import com.hzy.manager.domain.Project;
import com.hzy.manager.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目前端控制器
 * </p>
 *
 * @author wzh
 * @since 2020-09-08
 */
@Slf4j
@Api(tags = "项目控制器")
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    /**
     * 查询全部的项目信息(mybatisPlus实现物理分页)
     * 多条件查询参数:productName(项目名称)
     *
     * @param project
     * @param currentNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "项目信息列表", notes = "带分页,currentNo:当前页;pageSize:页面容量")
    @ApiImplicitParam(name = "productName", value = "项目名称", required = true)
    @GetMapping("/selectProjectList/{currentNo}/{pageSize}")
    public Result selProductListMes(@RequestBody Project project, @PathVariable Integer currentNo, @PathVariable Integer pageSize) {
        try {
            Page<Project> page = new Page<>(currentNo, pageSize);
            List<Page<Project>> pageList = projectService.getProjectList(project, page);
            Map<String, Object> map = new HashMap<>();
            map.put("pageList", pageList);
            map.put("page", page);
            return Result.ok(map);
        } catch (Exception e) {
            log.error("查询项目信息失败:", e);
            return Result.error("查询项目信息失败!");
        }
    }

    @ApiOperation(value = "新增项目信息", notes = "Project对象带上ProjectUser集合即可【拼成一个对象里带集合的json字符串即可】")
    @PostMapping("/addProject")
    public Result addProductMes(@RequestBody Project project) {
        try {
            projectService.addProject(project);
        } catch (Exception e) {
            log.error("新增项目信息失败:", e);
            return Result.error("新增项目信息失败!");
        }
        return Result.ok("新增项目信息成功");
    }

    @ApiOperation(value = "删除项目信息", notes = "形参传项目主键id字符串【1,2,3,4,5】")
    @ApiImplicitParam(name = "id", value = "项目主键id", required = true)
    @DeleteMapping("/deleteProject/{projectIds}")
    public Result delProductMes(@PathVariable String projectIds) {
        try {
            projectService.deleteProject(projectIds.split(StringPool.COMMA));
        } catch (Exception e) {
            log.error("新增项目信息失败:", e);
            return Result.error("新增项目信息失败!");
        }
        return Result.ok("删除项目信息成功");
    }
}
