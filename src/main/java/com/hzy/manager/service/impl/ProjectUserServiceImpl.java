package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.dao.ProjectUserMapper;
import com.hzy.manager.domain.ProjectUser;
import com.hzy.manager.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class ProjectUserServiceImpl extends ServiceImpl<ProjectUserMapper, ProjectUser> implements ProjectUserService {
    @Autowired
    private ProjectUserMapper projectUserMapper;

    @Override
    public List<ProjectUser> getMemberInfo(Long projectId) {
        return projectUserMapper.selectMemberListByProjectId(projectId);
    }
}
