package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Constant;
import com.hzy.manager.common.Result;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.dao.LoginUserMapper;
import com.hzy.manager.dao.WorkLogMapper;
import com.hzy.manager.domain.Role;
import com.hzy.manager.domain.UserWorkLog;
import com.hzy.manager.domain.WorkLog;
import com.hzy.manager.service.RoleService;
import com.hzy.manager.service.UserWorkLogService;
import com.hzy.manager.service.WorkLogService;
import com.hzy.manager.util.HttpServletUtil;
import com.hzy.manager.util.MD5Util;
import com.hzy.manager.util.PageUtils;
import com.hzy.manager.vo.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "播控工作日志控制类")
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
    @Autowired
    private WorkLogMapper workLogMapper;

    /**
     * 这个用的是自己封装的分页工具
     * 查询工作日志列表(播控人员只能看到自己的日志，有看工作日志权限的能看到全部日志)
     *
     * @param (currentNo,pageSize,realName)
     * @return
     */
    @ApiOperation(value = "播控工作日志列表信息", notes = "带分页,currentNo:当前页;pageSize:页面容量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "realName", value = "真实名字", required = true)
    })
    @GetMapping("/selectLogList/{currentNo}/{pageSize}")
    public Result selWorkLogList(WorkLog workLog,@PathVariable Integer currentNo, @PathVariable Integer pageSize) {
        try {
            LoginUser loginUser1 = (LoginUser) redisTemplate.opsForValue().get(HttpServletUtil.getHeaderToken());
            LoginUser loginUser = loginUserMapper.findByUserName(loginUser1.getUserName());
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
                int totalNum = workLogService.getWorkLogListCount(workLog.getRealName());
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
    @ApiOperation(value = "根据工作日志id查询一条工作日志信息")
    @ApiImplicitParam(name = "id", value = "工作日志id", required = true)
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
    @ApiOperation(value = "删除工作日志信息")
    @ApiImplicitParam(name = "id", value = "工作日志ids(可批量删除,拼接成字符串如：1,2,3,4)", required = true)
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
     * 新增工作日志,播控小伙伴每天只能新增一条工作日志
     *
     * @param workLog
     * @return
     */
    @ApiOperation(value = "新增工作日志信息", notes = "岗位(在线包,对战,大屏+ar,其实就这几个岗位，整个下拉框固定下，做到这再确认下吧)，演播室也可整个下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workDate", value = "工作日期如:2020-7-22", required = true),
            @ApiImplicitParam(name = "week", value = "星期几", required = true),
            @ApiImplicitParam(name = "gameTime", value = "比赛直播时间如:12:56", required = true),
            @ApiImplicitParam(name = "studio", value = "演播室", required = true),
            @ApiImplicitParam(name = "league", value = "联赛", required = true),
            @ApiImplicitParam(name = "game", value = "比赛", required = true),
            @ApiImplicitParam(name = "post", value = "岗位", required = true),
            @ApiImplicitParam(name = "jobContent", value = "工作内容描述", required = true),
            @ApiImplicitParam(name = "workHours", value = "工作时长", required = true),
            @ApiImplicitParam(name = "postAllowance", value = "岗位补贴", required = true)
    })
    @PostMapping("/addWorkLog")
    public Result addWorkLog(@RequestBody WorkLog workLog) throws BusinessException {
        int result = workLogService.addWorkLog(workLog);
        if (result > 0) {
            return Result.ok("新增工作日志成功");
        } else {
            return Result.error("新增工作日志失败");
        }
    }

    /**
     * 修改工作日志根据工作日志id
     * 说明:播控人员得到管理人员审批成功后只能修改一次播控工作日志,
     * 修改后播控人员又没有了修改工作日志这个菜单了需要再向管理员申请后才可进行下次工作日志的修改
     *
     * @param workLog
     * @return
     */
    @ApiOperation(value = "修改工作日志信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workDate", value = "工作日期如:2020-7-22", required = true),
            @ApiImplicitParam(name = "week", value = "星期几", required = true),
            @ApiImplicitParam(name = "gameTime", value = "比赛直播时间如:12:56", required = true),
            @ApiImplicitParam(name = "studio", value = "演播室", required = true),
            @ApiImplicitParam(name = "league", value = "联赛", required = true),
            @ApiImplicitParam(name = "game", value = "比赛", required = true),
            @ApiImplicitParam(name = "post", value = "岗位", required = true),
            @ApiImplicitParam(name = "jobContent", value = "工作内容描述", required = true),
            @ApiImplicitParam(name = "workHours", value = "工作时长", required = true),
            @ApiImplicitParam(name = "postAllowance", value = "岗位补贴", required = true)
    })
    @PutMapping("/updateWorkLog")
    public Result updateWorkLogMes(@RequestBody WorkLog workLog) {
        try {
            workLogService.updateWorkLog(workLog);
            return Result.ok("修改工作日志成功");
        } catch (Exception e) {
            log.error("修改工作日志失败:", e);
            return Result.error("修改工作日志失败");
        }
    }

    /**
     * excel快速实现导出
     *
     * @param response
     * @return
     */
    @GetMapping("/exportExcel")
    public Result excelExport(HttpServletResponse response) {
        try {
            List<WorkLog> list = workLogMapper.selectList(null);
            System.out.println("==========================");
            list.forEach(System.out::print);
            System.out.println("==========================");
            //ExcelKit.$Export(WorkLog.class, response).downXlsx(list, false);
            return Result.ok("导出工作日志Excel成功");
        } catch (Exception e) {
            log.error("导出工作日志Excel失败:", e);
            return Result.error("导出工作日志Excel失败");
        }
    }
}
