package com.hzy.manager.controller;

import com.hzy.manager.common.Result;
import com.hzy.manager.dao.DictMapper;
import com.hzy.manager.dao.ScheduleMapper;
import com.hzy.manager.dao.UserMapper;
import com.hzy.manager.service.DictService;
import com.hzy.manager.service.ScheduleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试控制类(测试使用)")
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private DictService dictService;

    @GetMapping("/get")
    public Result getMes() {
      /*  List<Dict> dictList = dictService.list();
        Map<Integer, String> map = new HashMap<>();
        for (Dict d : dictList) {
            map.put(d.getDicKey(),d.getDicValue());
        }
        List<Schedule> scheduleList = scheduleService.list();
        List<ScheduleVo> dataVo = new ArrayList<>();
        scheduleList.forEach(s -> {
            ScheduleVo vo = new ScheduleVo();
            vo.setPost(map.get(s.getPost()));
            vo.setStudio(map.get(s.getStudio()));
            dataVo.add(vo);
        });*/
        return Result.ok();
    }
}
