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
    public int addSchedule(Schedule schedule) throws BusinessException {
        if (StringUtils.isBlank(schedule.getWeek())) {
            throw new BusinessException("星期不能为空");
        }
        if (StringUtils.isBlank(schedule.getGameTime())) {
            throw new BusinessException("比赛时间不能为空");
        }
        if (StringUtils.isBlank(schedule.getLeague())) {
            throw new BusinessException("联赛不能为空");
        }
        if (schedule.getPost() == 0) {
            throw new BusinessException("岗位不能为空");
        }
        if (schedule.getStudio() == 0) {
            throw new BusinessException("演播室不能为空");
        }
        if (StringUtils.isBlank(schedule.getGame())) {
            throw new BusinessException("比赛不能为空");
        }
        schedule.setCreateTime(new Date());
        return scheduleMapper.insert(schedule);
    }

    @Override
    public List<Schedule> getScheduleListByPage(Map<String, Object> map) {
        return scheduleMapper.getScheduleListByCondition(map);
    }

    @Override
    public int getScheduleListCountMes(Map<String, Object> map) {
        return scheduleMapper.getScheduleListByConditionCount(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteSchedule(String[] ids) {
        return scheduleMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public Schedule selScheduleOne(Long sId) {
        Schedule schedule = scheduleMapper.selectOne(new LambdaQueryWrapper<Schedule>().eq(Schedule::getId, sId));
        return schedule;
    }

    @Override
    public List<Schedule> getNewSchedule(ScheduleDto scheduleDto) {
        return scheduleMapper.getScheduleInfo(scheduleDto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateScheduleSer(Schedule schedule) {
        return scheduleMapper.updateById(schedule);
    }
}
