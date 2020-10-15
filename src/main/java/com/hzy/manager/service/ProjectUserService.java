package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
public interface ProjectUserService extends IService<ProjectUser> {
    List<ProjectUser> getMemberInfo(Long projectId);
}
