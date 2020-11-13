package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Result;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.domain.Dict;
import com.hzy.manager.domain.Schedule;
import com.hzy.manager.dto.PageDto;
import com.hzy.manager.dto.ScheduleDto;
import com.hzy.manager.service.DictService;
import com.hzy.manager.service.ScheduleService;
import com.hzy.manager.service.UserService;
import com.hzy.manager.util.PageUtils;
import com.hzy.manager.vo.BroadcastUserVo;
import com.hzy.manager.vo.ScheduleVo;
import com.sun.org.apache.xerces.internal.xs.StringList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "排班控制类")
@RequestMapping("/schedule")
@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private DictService dictService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "查看排版信息【后台接口】", notes = "例子:{\"startTime\":\"2020-9-21\",\"endTime\":\"2020-9-24\",\"userId\":\"4\"}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "userId", value = "用户Id[可选,用来查询某个用户的排版信息]"),
    })
    @PostMapping("/selectScheduleByCondition")
    public Result selScheduleInfo(@RequestBody ScheduleDto scheduleDto) {
        try {
            Map<String, Object> mapPage = new HashMap<>();
            Map<String, Object> map = new HashMap<>();
            mapPage.put("currentNo", (scheduleDto.getCurrentNo() - 1) * scheduleDto.getPageSize());
            mapPage.put("pageSize", scheduleDto.getPageSize());
            mapPage.put("startTime", scheduleDto.getStartTime());
            mapPage.put("endTime", scheduleDto.getEndTime());
            mapPage.put("userId", scheduleDto.getUserId());
            map.put("startTime", scheduleDto.getStartTime());
            map.put("endTime", scheduleDto.getEndTime());
            map.put("userId", scheduleDto.getUserId());
            List<Dict> dictList = dictService.list();
            Map<Integer, String> dictMap = new HashMap<>();
            for (Dict d : dictList) {
                dictMap.put(d.getDicKey(), d.getDicValue());
            }
            List<ScheduleVo> scheduleVoList = new ArrayList<>();
            List<Schedule> scheduleList = scheduleService.getScheduleListByPage(mapPage);
            if (scheduleList.size() == 0) {
                return Result.wan("没有排版信息哦");
            }
            int totalCount = scheduleService.getScheduleListCountMes(map);
            PageUtils<Schedule> schedulePageUtils = new PageUtils<>(scheduleDto.getCurrentNo(),scheduleDto.getPageSize(), totalCount);
            scheduleList.forEach(schedule -> {
                ScheduleVo scheduleVo = new ScheduleVo();
                scheduleVo.setId(schedule.getId()).setWorkDate(schedule.getWorkDate()).setWeek(schedule.getWeek()).setUserId(schedule.getUserId());
                scheduleVo.setRealName(schedule.getUser().getRealName()).setGameTime(schedule.getGameTime()).setStudioKey(schedule.getStudio());
                scheduleVo.setStudioValue(dictMap.get(schedule.getStudio())).setLeague(schedule.getLeague()).setGame(schedule.getGame());
                scheduleVo.setPostKey(schedule.getPost()).setPostValue(dictMap.get(schedule.getPost())).setCreateTime(schedule.getCreateTime()).setModifyTime(schedule.getModifyTime());
                scheduleVoList.add(scheduleVo);
            });
            Map<String, Object> dataList = new HashMap<>();
            dataList.put("page", schedulePageUtils);
            dataList.put("scheduleList", scheduleVoList);
            return Result.ok(dataList);
        } catch (Exception e) {
            log.error("查询排班信息失败:", e);
            return Result.error("查询排班信息失败");
        }
    }

    @ApiOperation(value = "查看排班信息【前台接口】", notes = "以下参数都是条件查询参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【为了查看包括今天的以后排班信息】"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "userId", value = "用户Id[只看自己的排班,传入当前登录用户的id]")
    })
    @PostMapping("/selectScheduleFront")
    public Result selNewScheduleInfo(@RequestBody ScheduleDto scheduleDto) {
        try {
            List<Dict> dictList = dictService.list();
            Map<Integer, String> dictMap = new HashMap<>();
            for (Dict d : dictList) {
                dictMap.put(d.getDicKey(), d.getDicValue());
            }
            List<ScheduleVo> scheduleVoList = new ArrayList<>();
            List<Schedule> scheduleList = scheduleService.getNewSchedule(scheduleDto);
            List<BroadcastUserVo> broadcastUserVoList = userService.selectAllBroadcastUser();
            //没有排版信息
            if (scheduleList.size() == 0) {
                return Result.wan("今天没有你的排版哦");
            }
            scheduleList.forEach(schedule -> {
                ScheduleVo scheduleVo = new ScheduleVo();
                scheduleVo.setId(schedule.getId()).setWorkDate(schedule.getWorkDate()).setWeek(schedule.getWeek()).setUserId(schedule.getUserId());
                scheduleVo.setRealName(schedule.getUser().getRealName()).setGameTime(schedule.getGameTime()).setStudioKey(schedule.getStudio());
                scheduleVo.setStudioValue(dictMap.get(schedule.getStudio())).setLeague(schedule.getLeague()).setGame(schedule.getGame());
                scheduleVo.setPostKey(schedule.getPost()).setPostValue(dictMap.get(schedule.getPost())).setCreateTime(schedule.getCreateTime()).setModifyTime(schedule.getModifyTime());
                scheduleVoList.add(scheduleVo);
            });
            broadcastUserVoList = broadcastUserVoList.stream().
                    filter(b -> !scheduleList.stream()
                            .map(s -> s.getUserId()).collect(Collectors.toList()).
                                    contains(b.getId())).collect(Collectors.toList());
            Map<String, Object> map = new HashMap<>();
            map.put("workUser", scheduleVoList);
            map.put("restUser", broadcastUserVoList);
            return Result.ok(map);
        } catch (Exception e) {
            log.error("查询排班信息失败:", e);
            return Result.error("查询排班信息失败");
        }
    }

    @ApiOperation(value = "新增排班信息")
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
    @PostMapping("/selectOneSchedule")
    @ApiImplicitParam(name = "id", value = "排版id", required = true,dataType = "Long")
    public Result seScheduleOne(@RequestBody Schedule schedule) {
        try {
            Schedule s = scheduleService.selScheduleOne(schedule);
            return Result.ok(s);
        } catch (Exception e) {
            log.error("查询一条排班信息失败:", e);
            return Result.error("查询一条排班信息失败");
        }
    }

    @ApiOperation(value = "删除排班信息")
    @ApiImplicitParam(name = "ids", value = "排版ids【1,2,3,5,这样拼接】", required = true)
    @DeleteMapping("/deleteSchedule/{ids}")
    public Result delScheduleMes(@PathVariable String ids) {
        int res = scheduleService.deleteSchedule(ids.split(StringPool.COMMA));
        if (res > 0) {
            return Result.ok("删除排班信息成功");
        } else {
            return Result.ok("删除排班信息失败");
        }
    }

    @ApiOperation(value = "查看所有演播室", notes = "无参数")
    @PostMapping("/selectStudioInfo")
    public Result selStudioInfo() {
        List<Dict> studioList = null;
        try {
            studioList = dictService.list(new LambdaQueryWrapper<Dict>().eq(Dict::getFieldName, "studio"));
        } catch (Exception e) {
            log.error("查询所有演播室失败:", e);
            return Result.error("查询所有演播室失败");
        }
        return Result.ok(studioList);
    }

    @ApiOperation(value = "查看所有岗位", notes = "无参数")
    @PostMapping("/selectPostInfo")
    public Result selPostInfo() {
        List<Dict> postList = null;
        try {
            postList = dictService.list(new LambdaQueryWrapper<Dict>().eq(Dict::getFieldName, "post"));
        } catch (Exception e) {
            log.error("查询所有岗位失败:", e);
            return Result.error("查询所有岗位失败");
        }
        return Result.ok(postList);
    }
}
