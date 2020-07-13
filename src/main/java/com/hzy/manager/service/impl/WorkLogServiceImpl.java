package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.Constant;
import com.hzy.manager.common.authentication.JWTUtil;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.dao.*;
import com.hzy.manager.domain.UserWorkLog;
import com.hzy.manager.domain.WorkLog;
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
    @Autowired
    private LoginUserMapper loginUserMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<WorkLog> getWorkLogList(Map<String, Object> map) {
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
    public int addWorkLog(WorkLog workLog) throws BusinessException {
        String token = redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY)).toString();
        log.info("当前操作用户的token:" + token);
        LoginUser loginUser = loginUserMapper.findByUserName(JWTUtil.getUsername(token));
        log.info("loginUser:" + loginUser.toString());
        int res = 0;
        if (!StringUtils.isEmpty(workLog.getWorkDate())) {
            List<WorkLog> workLogMes = workLogMapper.selWorkLogListByUid(loginUser.getId(), workLog.getWorkDate());
            if (workLogMes.size() == 0) {
                workLog.setCreateTime(new Date());
                res = workLogMapper.insert(workLog);
                UserWorkLog userWorkLog = new UserWorkLog();
                userWorkLog.setUserId(loginUser.getId());
                userWorkLog.setId(workLog.getId());
                userWorkLogMapper.insert(userWorkLog);
            } else {
                throw new BusinessException("一天只能新增一条工作日志");
            }
        }
        return res;
    }

    @Transactional
    @Override
    public void updateWorkLog(WorkLog workLog) {
        String token = redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY)).toString();
        log.info("当前操作用户的token:" + token);
        LoginUser loginUser = loginUserMapper.findByUserName(JWTUtil.getUsername(token));
        log.info("User对象:" + loginUser.toString());
        workLog.setModifyTime(new Date());
        workLogMapper.updateById(workLog);
        //用户角色中间表删除当前播控用户一个修改播控日志的角色
        userRoleMapper.deleteUserRoleById(loginUser.getId(), 4L);
        //角色菜单中间表删除当前播控用户角色对应的修改工作日志的菜单
        roleMenuMapper.deleteRoleMenuById(4L, 11L);
    }
}
