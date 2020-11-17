package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.dao.ScheduleMapper;
import com.hzy.manager.domain.Schedule;
import com.hzy.manager.dto.ScheduleDto;
import com.hzy.manager.service.ScheduleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wzh
 * @since 2020-10-14
 */
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteSchedule(String[] ids) {
        return scheduleMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<Schedule> getScheduleListByPage(Map<String, Object> map) {
        return scheduleMapper.getScheduleListByCondition(map);
    }

    @Override
    public int getScheduleListCountMes(Map<String, Object> map) {
        return scheduleMapper.getScheduleListByConditionCount(map);
    }

    @Override
    public Schedule selScheduleOne(Schedule schedule1) {
        Schedule schedule = scheduleMapper.selectOne(new LambdaQueryWrapper<Schedule>().eq(Schedule::getId, schedule1.getId()));
        return schedule;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateScheduleSer(Schedule schedule) {
        return scheduleMapper.updateById(schedule);
    }
}
