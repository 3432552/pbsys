package com.hzy.manager.controller;

import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Role;
import com.hzy.manager.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 查询角色列表接口
     *
     * @return
     */
    @GetMapping("/roleList")
    public Result roleList() {
        List<Role> roleList = null;
        try {
            roleList = roleService.roleList();
        } catch (Exception e) {
            log.error("查询角色列表失败:", e);
            return Result.error("查询角色列表失败!");
        }
        return Result.ok(roleList);
    }
}
