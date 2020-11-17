package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.domain.Project;
import com.hzy.manager.domain.Schedule;
import com.hzy.manager.domain.User;
import com.hzy.manager.dto.ProjectDto;
import com.hzy.manager.dto.ScheduleDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wzh
 * @since 2020-10-14
 */
public interface ScheduleMapper extends BaseMapper<Schedule> {
    List<Schedule> getScheduleListByCondition(Map<String, Object> map);
    int getScheduleListByConditionCount(Map<String, Object> map);
}