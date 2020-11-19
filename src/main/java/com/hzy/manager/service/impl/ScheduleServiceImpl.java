package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.dao.DictMapper;
import com.hzy.manager.dao.ScheduleMapper;
import com.hzy.manager.dao.UserMapper;
import com.hzy.manager.domain.Dict;
import com.hzy.manager.domain.Schedule;
import com.hzy.manager.domain.User;
import com.hzy.manager.dto.ScheduleDto;
import com.hzy.manager.service.ScheduleService;
import com.hzy.manager.util.PageUtils;
import com.hzy.manager.util.StringUtil;
import com.hzy.manager.vo.BroadcastUserVo;
import com.hzy.manager.vo.ScheduleVo;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DictMapper dictMapper;

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

    @Override
    public Map<String, Object> getWorkingCondition(Schedule schedule) {
        Map<String, Object> returnDataMap = new HashMap<>();
        //所有播控人员id
        List<String> broadUserAllString = new ArrayList<>();
        Map<Integer, String> userMap = new HashMap<>();
        List<BroadcastUserVo> userList1 = userMapper.getAllBroadcastUser();
        for (BroadcastUserVo u : userList1) {
            userMap.put(u.getId().intValue(), u.getRealName());
            broadUserAllString.add(String.valueOf(u.getId()));
        }
        List<Schedule> workUserList = scheduleMapper.selectList(new LambdaQueryWrapper<Schedule>().eq(Schedule::getWorkDate, schedule.getWorkDate()));
        String workString = "";
        for (Schedule w : workUserList) {
            workString += w.getTrioUserid() + "," + w.getVcpMpUserid() + "," + w.getLvUserid() + "," + w.getTrtcUserid() + "," + w.getStudyOtherUserid() + ",";
        }
        String[] s = workString.split(StringPool.COMMA);
        Stream<String> stream = Arrays.stream(s);
        List<String> workUserIdList = stream.distinct().collect(toList());
        //取差集得到休息人员
        List<String> restUserIdList = broadUserAllString.stream().filter(item -> !workUserIdList.contains(item)).collect(toList());
        String workUserName = "";
        String restUserName = "";
        for (int i = 0; i < workUserIdList.size(); i++) {
            if (i == workUserIdList.size() - 1) {
                workUserName += userMap.get(Integer.valueOf(workUserIdList.get(i)));
                break;
            }
            workUserName += userMap.get(Integer.valueOf(workUserIdList.get(i))) + ",";
        }
        for (int i = 0; i < restUserIdList.size(); i++) {
            if (i == restUserIdList.size() - 1) {
                restUserName += userMap.get(Integer.valueOf(restUserIdList.get(i)));
                break;
            }
            restUserName += userMap.get(Integer.valueOf(restUserIdList.get(i))) + ",";
        }
        returnDataMap.put("workUserSize", workUserIdList.size());
        returnDataMap.put("workUser", workUserName);
        returnDataMap.put("restUserSize", restUserIdList.size());
        returnDataMap.put("restUser", restUserName);
        return returnDataMap;
    }

    @Override
    public Map<String, Object> getWorkLogListByCondition(ScheduleDto scheduleDto) {
        Map<String, Object> mapPage = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        Map<Integer, String> userMap = new HashMap<>();
        mapPage.put("currentNo", (scheduleDto.getCurrentNo() - 1) * scheduleDto.getPageSize());
        mapPage.put("pageSize", scheduleDto.getPageSize());
        mapPage.put("startTime", scheduleDto.getStartTime());
        mapPage.put("endTime", scheduleDto.getEndTime());
        mapPage.put("userId", scheduleDto.getUserId());
        map.put("startTime", scheduleDto.getStartTime());
        map.put("endTime", scheduleDto.getEndTime());
        map.put("userId", scheduleDto.getUserId());
        List<Dict> dictList = dictMapper.selectList(null);
        Map<Integer, String> dictMap = new HashMap<>();
        for (Dict d : dictList) {
            dictMap.put(d.getDicKey(), d.getDicValue());
        }
        List<ScheduleVo> scheduleVoList = new ArrayList<>();
        List<User> userList = userMapper.selectList(null);
        for (User u : userList) {
            userMap.put(u.getId().intValue(), u.getRealName());
        }
        List<Schedule> scheduleList = scheduleMapper.getScheduleListByCondition(mapPage);
        int totalCount = scheduleMapper.getScheduleListByConditionCount(map);
        PageUtils<Schedule> schedulePageUtils = new PageUtils<>(scheduleDto.getCurrentNo(), scheduleDto.getPageSize(), totalCount);
        scheduleList.forEach(schedule -> {
            ScheduleVo scheduleVo = new ScheduleVo();
            scheduleVo.setId(schedule.getId()).setWorkDate(schedule.getWorkDate()).setWeek(schedule.getWeek());
            scheduleVo.setGameTime(schedule.getGameTime()).setStudioKey(schedule.getStudio()).setStudioValue(dictMap.get(schedule.getStudio()));
            if (schedule.getTrioUserid() != null && schedule.getTrioUserid() == scheduleDto.getUserId().intValue()) {
                scheduleVo.setTrioUseridName(userMap.get(schedule.getTrioUserid()));
            } else {
                scheduleVo.setTrioUseridName(null);
            }
            if (schedule.getLvUserid() != null && schedule.getLvUserid() == scheduleDto.getUserId().intValue()) {
                scheduleVo.setLvUseridName(userMap.get(schedule.getLvUserid()));
            } else {
                scheduleVo.setLvUseridName(null);
            }
            if (schedule.getVcpMpUserid() != null && schedule.getVcpMpUserid() == scheduleDto.getUserId().intValue()) {
                scheduleVo.setVcpMpUseridName(userMap.get(schedule.getVcpMpUserid()));
            } else {
                scheduleVo.setVcpMpUseridName(null);
            }
            if (schedule.getTrtcUserid() != null && schedule.getTrtcUserid() == scheduleDto.getUserId().intValue()) {
                scheduleVo.setTrtcUseridName(userMap.get(schedule.getTrtcUserid()));
            } else {
                scheduleVo.setTrtcUseridName(null);
            }
            if (schedule.getStudyOtherUserid() != null && StringUtil.isContainerUid(schedule.getStudyOtherUserid(), scheduleDto.getUserId()) == true) {
                //包含这个用户
                scheduleVo.setStudyOtherUseridName(userMap.get(scheduleDto.getUserId().intValue()));
                scheduleVo.setRemark(schedule.getRemark());
            } else {
                scheduleVo.setStudyOtherUseridName(null);
            }
            scheduleVo.setLeague(schedule.getLeague()).setGame(schedule.getGame());
            scheduleVo.setCreateTime(schedule.getCreateTime()).setModifyTime(schedule.getModifyTime());
            scheduleVoList.add(scheduleVo);
        });
        Map<String, Object> dataList = new HashMap<>();
        dataList.put("page", schedulePageUtils);
        dataList.put("scheduleList", scheduleVoList);
        return dataList;
    }
}
