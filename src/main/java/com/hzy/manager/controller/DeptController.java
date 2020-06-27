package com.hzy.manager.controller;

import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Dept;
import com.hzy.manager.domain.User;
import com.hzy.manager.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 查询所有部门
     *
     * @return
     */
    @GetMapping("/deptList")
    public Result selectDeptList() {
        List<Dept> deptList = deptService.getDeptList();
        return Result.ok(deptList);
    }
}
