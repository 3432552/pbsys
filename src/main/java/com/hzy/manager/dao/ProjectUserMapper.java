package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.ProjectUser;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wzh
 * @since 2020-09-08
 */
public interface ProjectUserMapper extends BaseMapper<ProjectUser> {
    void deleteProjectUser(String[] puId);
    //根据项目id查找项目下面的成员和成员的工时
    List<ProjectUser> selectMemberListByProjectId(Long projectId);
}
