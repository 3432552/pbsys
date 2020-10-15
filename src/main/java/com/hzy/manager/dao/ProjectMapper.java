package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.domain.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wzh
 * @since 2020-09-08
 */
public interface ProjectMapper extends BaseMapper<Project> {
    List<Page<Project>> selProjectList(@Param("project") Project project, Page<Project> projectPage);

}
