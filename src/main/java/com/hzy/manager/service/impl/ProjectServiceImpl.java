package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.Result;
import com.hzy.manager.dao.ProjectMapper;
import com.hzy.manager.dao.ProjectUserMapper;
import com.hzy.manager.domain.Project;
import com.hzy.manager.domain.ProjectUser;
import com.hzy.manager.dto.PageDto;
import com.hzy.manager.dto.ProjectDto;
import com.hzy.manager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wzh
 * @since 2020-09-08
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectUserMapper projectUserMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addProject(Project project) {
        project.setCreateTime(new Date());
        projectMapper.insert(project);
        project.getProjectUserList().forEach(projectUser -> {
            ProjectUser projectUser1 = new ProjectUser();
            projectUser1.setProjectId(project.getId());
            projectUser1.setUserId(projectUser.getUserId());
            projectUser1.setWorkingTime(projectUser.getWorkingTime());
            projectUserMapper.insert(projectUser1);
        });
    }

    @Override
    public List<Page<Project>> getProjectList(ProjectDto projectDto, Page<Project> projectPage) {
        return projectMapper.selProjectList(projectDto, projectPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteProject(String[] projectIds) {
        //先删除项目表
        projectMapper.deleteBatchIds(Arrays.asList(projectIds));
        //再删除关联表
        projectUserMapper.deleteProjectUser(projectIds);
    }
}
