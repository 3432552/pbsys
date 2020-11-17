package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.common.Result;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.domain.Dict;
import com.hzy.manager.domain.Project;
import com.hzy.manager.domain.Schedule;
import com.hzy.manager.domain.User;
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

    @ApiOperation(value = "查看排版信息【后台接口】")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "currentNo", value = "当前页"),
            @ApiImplicitParam(name = "pageSize", value = "页面容量"),
    })
    @PostMapping("selectScheduleList")
    public Result selScheduleList(@RequestBody ScheduleDto scheduleDto) {
        try {
            Map<String, Object> mapPage = new HashMap<>();
            Map<String, Object> map = new HashMap<>();
            Map<Integer, String> userMap = new HashMap<>();
            mapPage.put("currentNo", (scheduleDto.getCurrentNo() - 1) * scheduleDto.getPageSize());
            mapPage.put("pageSize", scheduleDto.getPageSize());
            mapPage.put("startTime", scheduleDto.getStartTime());
            mapPage.put("endTime", scheduleDto.getEndTime());
            map.put("startTime", scheduleDto.getStartTime());
            map.put("endTime", scheduleDto.getEndTime());
            List<Dict> dictList = dictService.list();
            Map<Integer, String> dictMap = new HashMap<>();
            for (Dict d : dictList) {
                dictMap.put(d.getDicKey(), d.getDicValue());
            }
            List<User> userList = userService.list();
            for (User u : userList) {
                userMap.put(u.getId().intValue(), u.getRealName());
            }
            List<ScheduleVo> scheduleVoList = new ArrayList<>();
            List<Schedule> scheduleList = scheduleService.getScheduleListByPage(mapPage);
            int totalCount = scheduleService.getScheduleListCountMes(map);
            PageUtils<Schedule> schedulePageUtils = new PageUtils<>(scheduleDto.getCurrentNo(), scheduleDto.getPageSize(), totalCount);
            scheduleList.forEach(schedule -> {
                ScheduleVo scheduleVo = new ScheduleVo();
                scheduleVo.setId(schedule.getId()).setWorkDate(schedule.getWorkDate()).setWeek(schedule.getWeek());
                scheduleVo.setGameTime(schedule.getGameTime()).setStudioKey(schedule.getStudio()).setStudioValue(dictMap.get(schedule.getStudio()));
                scheduleVo.setTrioUseridName(userMap.get(schedule.getTrioUserid())).setVcpMpUseridName(userMap.get(schedule.getVcpMpUserid()));
                scheduleVo.setLvUseridName(userMap.get(schedule.getLvUserid())).setTrtcUseridName(userMap.get(schedule.getTrtcUserid()));
                String studyString = "";
                for (int i = 0; i < schedule.getStudyOtherUserid().split(StringPool.COMMA).length; i++) {
                    if (i == schedule.getStudyOtherUserid().split(StringPool.COMMA).length - 1) {
                        studyString += userMap.get(Integer.valueOf(schedule.getStudyOtherUserid().split(StringPool.COMMA)[i]));
                        break;
                    }
                    studyString += userMap.get(Integer.valueOf(schedule.getStudyOtherUserid().split(StringPool.COMMA)[i])) + ",";
                }
                scheduleVo.setStudyOtherUseridName(studyString);
                scheduleVo.setLeague(schedule.getLeague()).setGame(schedule.getGame());
                scheduleVo.setRemark(schedule.getRemark()).setCreateTime(schedule.getCreateTime()).setModifyTime(schedule.getModifyTime());
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

    @ApiOperation(value = "新增排班信息【支持批量新增】")
    @PostMapping("/addSchedule")
    public Result insertSchedule(@RequestBody List<Schedule> scheduleList) {
        boolean flag = scheduleService.saveBatch(scheduleList);
        if (flag == true) {
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
    @ApiImplicitParam(name = "id", value = "排版id", required = true, dataType = "Long")
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
