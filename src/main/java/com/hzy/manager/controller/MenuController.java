package com.hzy.manager.controller;

import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.dto.Tree;
import com.hzy.manager.dto.router.VueRouter;
import com.hzy.manager.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 根据用户ID生成左侧菜单,只生成菜单(增删改属于按钮,左侧菜单树不显示)
     *
     * @param
     * @return
     */
    @GetMapping("/onlyMenuTree")
    public Result getMenuRoutes() {
        List<VueRouter<Menu>> vueRouters = null;
        try {
            vueRouters = menuService.getUserRouters();
            return Result.ok(vueRouters);
        } catch (Exception e) {
            log.error("生成菜单树失败:", e);
            return Result.error("生成菜单树失败!");
        }
    }

    /**
     * 生成菜单列表,生成菜单和按钮
     *
     * @param
     * @return
     */
    @GetMapping("/menuTree")
    public Result getMenu(Menu menu) {
        Tree<Menu> menuTree = null;
        try {
            menuTree = menuService.getMenuTree(menu.getMenuName(), menu.getType());
            return Result.ok(menuTree);
        } catch (Exception e) {
            log.error("生成菜单列表树失败:", e);
            return Result.error("生成列表树失败!");
        }
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
