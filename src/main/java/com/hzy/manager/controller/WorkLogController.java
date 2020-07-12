package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Constant;
import com.hzy.manager.common.Result;
import com.hzy.manager.common.authentication.JWTUtil;
import com.hzy.manager.dao.LoginUserMapper;
import com.hzy.manager.domain.Role;
import com.hzy.manager.domain.User;
import com.hzy.manager.domain.UserWorkLog;
import com.hzy.manager.domain.WorkLog;
import com.hzy.manager.service.RoleService;
import com.hzy.manager.service.UserWorkLogService;
import com.hzy.manager.service.WorkLogService;
import com.hzy.manager.util.MD5Util;
import com.hzy.manager.util.PageUtils;
import com.hzy.manager.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/worklog")
public class WorkLogController {
    @Autowired
    private WorkLogService workLogService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private LoginUserMapper loginUserMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserWorkLogService userWorkLogService;

    /**
     * 查询工作日志列表(播控人员只能看到自己的日志，有看工作日志权限的能看到全部日志)
     *
     * @param (currentNo,pageSize,realName)
     * @return
     */
    @GetMapping("/selectLogList/{currentNo}/{pageSize}")
    public Result selWorkLogList(WorkLog workLog, @PathVariable Integer currentNo, @PathVariable Integer pageSize) {
        try {
            String token = redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY)).toString();
            LoginUser loginUser = loginUserMapper.findByUserName(JWTUtil.getUsername(token));
            Long uid = loginUser.getId();
            Role role = roleService.getRoleByuId(uid);
            //如果是播控人员,只能看到自己的工作日志,其他有权限的能看到全部工作日志
            if (role.getId() == 3) {
                int totalNum = userWorkLogService.count(new LambdaQueryWrapper<UserWorkLog>().eq(UserWorkLog::getUserId, uid));
                PageUtils<WorkLog> pageUtils = new PageUtils<>(currentNo, pageSize, totalNum);
                Map<String, Object> map = new HashMap<>();
                map.put("realName", workLog.getRealName());
                map.put("offeSet", pageUtils.getOffset());
                map.put("pageSize", pageUtils.getPageSize());
                map.put("uid", uid);
                List<WorkLog> list = workLogService.getWorkLogListByBoKong(map);
                pageUtils.setPageList(list);
                return Result.ok(pageUtils);
            } else {
                int totalNum = workLogService.count();
                PageUtils<WorkLog> pageUtils = new PageUtils<>(currentNo, pageSize, totalNum);
                Map<String, Object> map = new HashMap<>();
                map.put("realName", workLog.getRealName());
                map.put("offeSet", pageUtils.getOffset());
                map.put("pageSize", pageUtils.getPageSize());
                List<WorkLog> list = workLogService.getWorkLogList(map);
                pageUtils.setPageList(list);
                return Result.ok(pageUtils);
            }
        } catch (Exception e) {
            log.error("查询工作日志失败:", e);
            return Result.error("查询工作日志失败!");
        }
    }

    /**
     * 根据工作日志表id获取一条日志信息为了修改
     *
     * @return
     */
    @GetMapping("/selectById/{logId}")
    public Result selWorkLogById(@PathVariable Long logId) {
        try {
            WorkLog workLog = workLogService.getById(logId);
            return Result.ok(workLog);
        } catch (Exception e) {
            log.error("查询一条工作日志失败:", e);
            return Result.error("查询一条工作日志失败!");
        }
    }

    /**
     * 可以批量删除工作日志
     *
     * @param logIds,根据日志表id
     * @return
     */
    @DeleteMapping("/deleteById/{logIds}")
    public Result delById(@PathVariable String logIds) {
        try {
            String[] logId = logIds.split(StringPool.COMMA);
            workLogService.deleteWorkLogById(logId);
            return Result.ok("删除工作日志成功!");
        } catch (Exception e) {
            log.error("删除工作日志失败:", e);
            return Result.error("删除工作日志失败!");
        }
    }

    /**
     * 新增工作日志
     *
     * @param workLog
     * @return
     */
    @PostMapping("/addWorkLog")
    public Result addWorkLog(WorkLog workLog) {
        try {
            workLogService.addWorkLog(workLog);
            return Result.ok("新增工作日志成功");
        } catch (Exception e) {
            log.error("新增工作日志失败:", e);
            return Result.error("新增工作日志失败");
        }
    }

    /**
     * 修改工作日志根据工作日志id
     *
     * @param workLog
     * @return
     */
    @PutMapping("/updateWorkLog")
    public Result updateWorkLogMes(WorkLog workLog) {
        try {
            workLogService.updateWorkLog(workLog);
            return Result.ok("修改工作日志成功");
        } catch (Exception e) {
            log.error("修改工作日志失败:", e);
            return Result.error("修改工作日志失败");
        }
    }
}
