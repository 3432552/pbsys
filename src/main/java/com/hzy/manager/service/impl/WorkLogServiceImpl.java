package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.Constant;
import com.hzy.manager.common.authentication.JWTUtil;
import com.hzy.manager.dao.LoginUserMapper;
import com.hzy.manager.dao.UserWorkLogMapper;
import com.hzy.manager.dao.WorkLogMapper;
import com.hzy.manager.domain.UserWorkLog;
import com.hzy.manager.domain.WorkLog;
import com.hzy.manager.vo.LoginUser;
import com.hzy.manager.service.WorkLogService;
import com.hzy.manager.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
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
    @Autowired
    private LoginUserMapper loginUserMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<WorkLog> getWorkLogList(Map<String,Object> map) {
        return workLogMapper.selectWorkLogList(map);
    }

    @Override
    public List<WorkLog> getWorkLogListByBoKong(Map<String, Object> map) {
        return workLogMapper.selectWorkLogListByBoKong(map);
    }

    @Override
    public WorkLog worklog(Long logId) {
        return workLogMapper.selectById(logId);
    }

    @Transactional
    @Override
    public void deleteWorkLogById(String[] logIds) {
        workLogMapper.deleteBatchIds(Arrays.asList(logIds));
    }

    @Transactional
    @Override
    public void addWorkLog(WorkLog workLog) {
        workLog.setCreateTime(new Date());
        workLogMapper.insert(workLog);
        String token = redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY)).toString();
        log.info("当前操作用户的token:" + token);
        LoginUser loginUser = loginUserMapper.findByUserName(JWTUtil.getUsername(token));
        log.info("loginUser:" + loginUser.toString());
        UserWorkLog userWorkLog = new UserWorkLog();
        userWorkLog.setUserId(loginUser.getId());
        userWorkLog.setId(workLog.getId());
        userWorkLogMapper.insert(userWorkLog);
    }

    @Transactional
    @Override
    public void updateWorkLog(WorkLog workLog) {
        workLog.setModifyTime(new Date());
        workLogMapper.updateById(workLog);
    }
}
