package com.hzy.manager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.common.Result;
import com.hzy.manager.dao.ApprovalMapper;
import com.hzy.manager.dao.MenuMapper;
import com.hzy.manager.domain.Approval;
import com.hzy.manager.domain.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private ApprovalMapper approvalMapper;

    @GetMapping("/selectPage")
    public Result indexMes() {
        //List<Approval> approvalList = approvalMapper.selectApprovalList("");
        return Result.ok();
    }
}
