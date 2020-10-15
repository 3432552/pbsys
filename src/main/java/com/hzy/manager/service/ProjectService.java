package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Project;
import com.hzy.manager.domain.ProjectUser;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wzh
 * @since 2020-09-08
 */
public interface ProjectService extends IService<Project> {
    //多条件查询项目信息并分页
    List<Page<Project>> getProjectList(Project project, Page<Project> projectPage);

    void addProject(Project project);

    void deleteProject(String[] projectIds);
}
