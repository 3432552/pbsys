package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.Constant;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.dao.*;
import com.hzy.manager.domain.UserWorkLog;
import com.hzy.manager.domain.WorkLog;
import com.hzy.manager.util.HttpServletUtil;
import com.hzy.manager.vo.LoginUser;
import com.hzy.manager.service.WorkLogService;
import com.hzy.manager.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WorkLogServiceImpl extends ServiceImpl<WorkLogMapper, WorkLog> implements WorkLogService {
    @Autowired
    private WorkLogMapper workLogMapper;
    @Autowired
    private UserWorkLogMapper userWorkLogMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<WorkLog> getWorkLogList(Map<String, Object> map) {
        return workLogMapper.selectWorkLogList(map);
    }

    @Override
    public int getWorkLogListCount(String realName) {
        return workLogMapper.selectWorkLogListCount(realName);
    }

    @Override
    public List<WorkLog> getWorkLogListByBoKong(Map<String, Object> map) {
        return workLogMapper.selectWorkLogListByBoKong(map);
    }

    @Override
    public WorkLog worklog(Long logId) {
        return workLogMapper.selectById(logId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteWorkLogById(String[] logIds) {
        workLogMapper.deleteBatchIds(Arrays.asList(logIds));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addWorkLog(WorkLog workLog) throws BusinessException {
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(HttpServletUtil.getHeaderToken());
        workLog.setCreateTime(new Date());
        int res;
        res = workLogMapper.insert(workLog);
        UserWorkLog userWorkLog = new UserWorkLog();
        userWorkLog.setUserId(loginUser.getId());
        userWorkLog.setId(workLog.getId());
        res = userWorkLogMapper.insert(userWorkLog);
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateWorkLog(WorkLog workLog) {
        workLog.setStatus(Constant.APPROVALPENDING);
        return workLogMapper.updateById(workLog);
    }

    @Override
    public int updateWorkLogNo(WorkLog workLog) {
        workLog.setStatus(Constant.APPROVALFAIL);
        return workLogMapper.updateById(workLog);
    }
}

