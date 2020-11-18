package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Constant;
import com.hzy.manager.common.Result;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.dao.LoginUserMapper;
import com.hzy.manager.dao.WorkLogMapper;
import com.hzy.manager.domain.Dict;
import com.hzy.manager.domain.Role;
import com.hzy.manager.domain.UserWorkLog;
import com.hzy.manager.domain.WorkLog;
import com.hzy.manager.dto.PageDto;
import com.hzy.manager.dto.WorkLogDto;
import com.hzy.manager.service.DictService;
import com.hzy.manager.service.RoleService;
import com.hzy.manager.service.UserWorkLogService;
import com.hzy.manager.service.WorkLogService;
import com.hzy.manager.util.HttpServletUtil;
import com.hzy.manager.util.MD5Util;
import com.hzy.manager.util.PageUtils;
import com.hzy.manager.vo.LoginUser;
import com.hzy.manager.vo.ScheduleVo;
import com.hzy.manager.vo.WorkLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
    private RoleService roleService;
    @Autowired
    private UserWorkLogService userWorkLogService;
    @Autowired
    private WorkLogMapper workLogMapper;
    @Autowired
    private DictService dictService;

    /**
     * 这个用的是自己封装的分页工具
     * 查询工作日志列表(播控人员只能看到自己的日志，有看工作日志权限的能看到全部日志)
     *
     * @return
     */
    @ApiOperation(value = "播控工作日志列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "realName", value = "真实名字"),
            @ApiImplicitParam(name = "currentNo", value = "当前页",required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面容量",required = true)
    })
    @PostMapping("/selectLogList")
    public Result selWorkLogList(@RequestBody WorkLogDto workLogDto) {
        List<Dict> dictList = dictService.list();
        Map<Integer, String> dictMap = new HashMap<>();
        for (Dict d : dictList) {
            dictMap.put(d.getDicKey(), d.getDicValue());
        }
        List<WorkLogVo> workLogVoList=new ArrayList<>();
        try {
            LoginUser loginUser1 = (LoginUser) redisTemplate.opsForValue().get(HttpServletUtil.getHeaderToken());
            Long uid = loginUser1.getId();
            Role role = roleService.getRoleByuId(uid);
            //如果是播控人员,只能看到自己的工作日志,其他有权限的能看到全部工作日志
            if (role.getId() == 3) {
                int totalNum = userWorkLogService.count(new LambdaQueryWrapper<UserWorkLog>().eq(UserWorkLog::getUserId, uid));
                PageUtils<WorkLog> pageUtils = new PageUtils<>(workLogDto.getCurrentNo(),workLogDto.getPageSize(),totalNum);
                Map<String, Object> map = new HashMap<>();
                map.put("realName", workLogDto.getRealName());
                map.put("offSet", pageUtils.getOffset());
                map.put("pageSize", pageUtils.getPageSize());
                map.put("uid", uid);
                List<WorkLog> list = workLogService.getWorkLogListByBoKong(map);
                list.forEach(schedule -> {
                    WorkLogVo workLogVo=new WorkLogVo();
                    workLogVo.setId(schedule.getId()).setWorkDate(schedule.getWorkDate()).setWeek(schedule.getWeek());
                    workLogVo.setGameTime(schedule.getGameTime()).setStudioKey(schedule.getStudio()).setStatus(schedule.getStatus()).setApprovalOpinion(schedule.getApprovalOpinion());
                    workLogVo.setStudioValue(dictMap.get(schedule.getStudio())).setLeague(schedule.getLeague()).setGame(schedule.getGame()).setApprovalOpinion(schedule.getApprovalOpinion());
                    workLogVo.setWorkHours(schedule.getWorkHours()).setPostAllowance(schedule.getPostAllowance());
                    workLogVo.setPostKey(schedule.getPost()).setRealName(schedule.getRealName()).setPostValue(dictMap.get(schedule.getPost())).setCreateTime(schedule.getCreateTime()).setModifyTime(schedule.getModifyTime());
                    workLogVoList.add(workLogVo);
                });
                Map<String, Object> dataList = new HashMap<>();
                dataList.put("page",pageUtils);
                dataList.put("workLogList",workLogVoList);
                return Result.ok(dataList);
            } else {
                int totalNum = workLogService.getWorkLogListCount(workLogDto.getRealName());
                PageUtils<WorkLog> pageUtils = new PageUtils<>(workLogDto.getCurrentNo(),workLogDto.getPageSize(), totalNum);
                Map<String, Object> map = new HashMap<>();
                map.put("realName", workLogDto.getRealName());
                map.put("offeSet", pageUtils.getOffset());
                map.put("pageSize", pageUtils.getPageSize());
                List<WorkLog> list = workLogService.getWorkLogList(map);
                list.forEach(schedule -> {
                    WorkLogVo workLogVo=new WorkLogVo();
                    workLogVo.setId(schedule.getId()).setWorkDate(schedule.getWorkDate()).setWeek(schedule.getWeek());
                    workLogVo.setGameTime(schedule.getGameTime()).setStudioKey(schedule.getStudio()).setStatus(schedule.getStatus()).setApprovalOpinion(schedule.getApprovalOpinion());
                    workLogVo.setStudioValue(dictMap.get(schedule.getStudio())).setLeague(schedule.getLeague()).setGame(schedule.getGame()).setApprovalOpinion(schedule.getApprovalOpinion());
                    workLogVo.setWorkHours(schedule.getWorkHours()).setPostAllowance(schedule.getPostAllowance());
                    workLogVo.setPostKey(schedule.getPost()).setRealName(schedule.getRealName()).setPostValue(dictMap.get(schedule.getPost())).setCreateTime(schedule.getCreateTime()).setModifyTime(schedule.getModifyTime());
                    workLogVoList.add(workLogVo);
                });
                Map<String, Object> dataList = new HashMap<>();
                dataList.put("page",pageUtils);
                dataList.put("workLogList",workLogVoList);
                return Result.ok(dataList);
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
     *
     * @param workLog
     * @return
     */
    @ApiOperation(value = "修改工作日志信息播控人员修改工作日志后,工作日志就是待审核状态】【前台接口】")
    @PutMapping("/updateWorkLog")
    public Result updateWorkLogMes(@RequestBody WorkLog workLog) {
        int res = workLogService.updateWorkLog(workLog);
        if (res > 0) {
            return Result.ok("修改工作日志成功");
        } else {
            return Result.error("修改工作日志失败");
        }
    }

    @ApiOperation(value = "修改日志信息审批通过【支持批量通过审批：如：1,4,7,8】")
    @PutMapping("/approvalOk/{ids}")
    public Result workLogPass(@PathVariable String ids) {
        List<WorkLog> workLogList = new ArrayList<>();
        String[] nums = ids.split(StringPool.COMMA);
        for (int i = 0; i < nums.length; i++) {
            WorkLog w = new WorkLog();
            w.setId(Long.valueOf(nums[i]));
            w.setStatus(Constant.APPROVALSUCCESS);
            workLogList.add(w);
        }
        boolean res = workLogService.updateBatchById(workLogList);
        if (res == true) {
            return Result.ok("修改成功");
        } else {
            return Result.error("修改失败");
        }
    }

    @ApiOperation(value = "修改日志信息审批失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "工作日志ids", required = true),
            @ApiImplicitParam(name = "approvalOpinion", value = "不通过原因", required = true)
    })
    @PutMapping("/approvalNo")
    public Result workLogNo(@RequestBody WorkLog workLog) {
        int res = workLogService.updateWorkLogNo(workLog);
        if (res > 0) {
            return Result.ok("修改成功");
        } else {
            return Result.ok("修改失败");
        }
    }
}
