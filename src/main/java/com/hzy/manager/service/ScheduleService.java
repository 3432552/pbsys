package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.domain.Schedule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.dto.ScheduleDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wzh
 * @since 2020-10-14
 */
public interface ScheduleService extends IService<Schedule> {
    List<Schedule> getScheduleListByPage(Map<String, Object> map);

    int getScheduleListCountMes(Map<String, Object> map);

    int deleteSchedule(String[] ids);

    Schedule selScheduleOne(Schedule schedule);

    int updateScheduleSer(Schedule schedule);

    //查询某一天的排班【为了获取到这一天播控人员上班和不上班是谁和人数】
    Map<String, Object> getWorkingCondition(Schedule schedule);

    //查询一个播控人员的工作日志多条件
    Map<String, Object> getWorkLogListByCondition(ScheduleDto scheduleDto);
}
