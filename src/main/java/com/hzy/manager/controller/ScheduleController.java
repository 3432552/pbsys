package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.common.Result;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.domain.Schedule;
import com.hzy.manager.dto.ScheduleDto;
import com.hzy.manager.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "排版控制器")
@RequestMapping("/schedule")
@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "查看排版信息【后台接口】", notes = "{\"startTime\":\"2020-9-21\",\"endTime\":\"2020-9-24\",\"userId\":\"4\"}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true),
            @ApiImplicitParam(name = "userId", value = "用户Id[可选,用来查询某个用户的排版信息]", required = false),
    })
    @PostMapping("/selectScheduleByCondition/{currentNo}/{pageSize}")
    public Result selScheduleInfo(@RequestBody ScheduleDto scheduleDto, @PathVariable Integer currentNo, @PathVariable Integer pageSize) {
        try {
            Page<Schedule> page = new Page<>(currentNo, pageSize);
            List<Page<Schedule>> pageList = scheduleService.getScheduleListBy(scheduleDto, page);
            Map<String, Object> map = new HashMap<>();
            map.put("pageList", pageList);
            map.put("page", page);
            return Result.ok(map);
        } catch (Exception e) {
            log.error("查询排班信息失败:", e);
            return Result.error("查询排班信息失败");
        }
    }

    @ApiOperation(value = "查看排版系统【前端接口】")
    @ApiImplicitParam(name = "startTime", value = "开始时间【为了查看包括今天的以后排班信息】", required = true)
    @PostMapping("/selectScheduleFront")
    public Result selNewScheduleInfo(@RequestBody ScheduleDto scheduleDto) {
        try {
            List<Schedule> scheduleList = scheduleService.getNewSchedule(scheduleDto);
            return Result.ok(scheduleList);
        } catch (Exception e) {
            log.error("查询排班信息失败:", e);
            return Result.error("查询排班信息失败");
        }
    }

    @ApiOperation(value = "新增排版信息")
    @PostMapping("/addSchedule")
    public Result insertSchedule(@RequestBody Schedule schedule) throws BusinessException {
        int res = scheduleService.addSchedule(schedule);
        if (res > 0) {
            return Result.ok("新增排版信息成功");
        } else {
            return Result.ok("新增排版信息失败");
        }
    }

    @ApiOperation(value = "修改排班信息")
    @PutMapping("/updateSchedule")
    public Result updateScheduleMes(@RequestBody Schedule schedule) {
        int res = scheduleService.updateScheduleSer(schedule);
        if (res > 0) {
            return Result.ok("修改排班信息成功");
        } else {
            return Result.ok("修改排班信息失败");
        }
    }

    @ApiOperation(value = "查询一条排班信息")
    @PostMapping("/selectOneSchedule/{sId}")
    @ApiImplicitParam(name = "sId", value = "排版id", required = true)
    public Result seScheduleOne(@PathVariable Long sId) {
        try {
            Schedule s = scheduleService.selScheduleOne(sId);
            return Result.ok(s);
        } catch (Exception e) {
            log.error("查询一条排班信息失败:", e);
            return Result.error("查询一条排班信息失败");
        }
    }

    @ApiOperation(value = "删除排班信息")
    @DeleteMapping("/deleteSchedule/{ids}")
    public Result delScheduleMes(@PathVariable String ids) {
        int res = scheduleService.deleteSchedule(ids.split(StringPool.COMMA));
        if (res > 0) {
            return Result.ok("删除排班信息成功");
        } else {
            return Result.ok("删除排班信息失败");
        }
    }
}
