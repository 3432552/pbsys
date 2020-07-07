package com.hzy.manager.controller;

import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Dept;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.dto.Tree;
import com.hzy.manager.dto.router.VueRouter;
import com.hzy.manager.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 根据用户名生成左侧菜单,只生成菜单(增删改属于按钮,左侧菜单树不显示)
     *
     * @param userName
     * @return
     */
    @GetMapping("/{userName}")
    public Result getMenuRoutes(@PathVariable String userName) {
        List<VueRouter<Menu>> vueRouters = menuService.getUserRouters(userName);
        return Result.ok(vueRouters);
    }

    /**
     * 生成菜单列表,生成菜单和按钮
     *
     * @param map
     * @return
     */
    @GetMapping("/menuTree")
    public Result getMenu(Map<String, Object> map) {
        map.put("userName", "admin");
        Tree<Menu> menuTree = null;
        try {
            menuTree = menuService.getMenuTree(map);
        } catch (Exception e) {
            log.error("生成菜单树失败:", e);
            return Result.error("生成菜单树失败!");
        }
        return Result.ok(menuTree);
    }

    /**
     * 新增菜单 必要参数menuName,type,parentId(要获得点击菜单的那个父Id)
     *
     * @param menu
     * @return
     */
    @PostMapping("/addMenu")
    public Result addDept(Menu menu) {
        try {
            menuService.addMenu(menu);
            return Result.ok("新增菜单成功");
        } catch (Exception e) {
            log.error("新增菜单失败:", e);
            return Result.error("新增菜单失败!");
        }
    }
}
