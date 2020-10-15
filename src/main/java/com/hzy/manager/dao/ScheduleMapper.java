package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.domain.Project;
import com.hzy.manager.domain.Schedule;
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
    //多条件查询排版信息【后台接口】
    List<Page<Schedule>> getScheduleListByCondition(@Param("scheduleDto") ScheduleDto scheduleDto, Page<Schedule> schedulePage);

    //查询从今天往后的排版信息
    List<Schedule> getScheduleInfo(@Param("scheduleDto") ScheduleDto scheduleDto);

    List<Schedule> getScheduleList();
}
