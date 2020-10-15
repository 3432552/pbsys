package com.hzy.manager.controller;

import com.hzy.manager.common.Result;
import com.hzy.manager.dao.DictMapper;
import com.hzy.manager.dao.ScheduleMapper;
import com.hzy.manager.dao.UserMapper;
import com.hzy.manager.domain.Dict;
import com.hzy.manager.domain.Schedule;
import com.hzy.manager.dto.ScheduleDto;
import com.hzy.manager.service.DictService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "测试控制器(测试使用)")
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private DictService dictService;

    @GetMapping("/get")
    public Result getMes() {
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", "2020-9-21");
        map.put("endTime", "2020-9-24");
        map.put("userId", "4");
       // List<Schedule> sch = scheduleMapper.getScheduleListByCondition(map);
        return Result.ok();
    }
}
