package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.vo.Tree;
import com.hzy.manager.vo.router.VueRouter;
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
     * 根据用户ID生成菜单列表,生成菜单和按钮
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
     * 新增菜单 必要参数menuName,type,parentId(要获得点击菜单的那个父菜单Id【即菜单主键id】)
     * 新增菜单后会给当前角色在菜单角色关联表中把把角色对应的菜单也会新增进关联表
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

    /**
     * 根据菜单Id获取一条菜单信息，为了修改
     *
     * @param menuId
     * @return
     */
    @GetMapping("/selectMenuById/{menuId}")
    public Result selMenuById(@PathVariable Long menuId) {
        try {
            Menu menu = menuService.findMenuById(menuId);
            return Result.ok(menu);
        } catch (Exception e) {
            log.error("查询菜单失败:", e);
            return Result.error("查询菜单失败!");
        }
    }

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    @PutMapping("/updateMenu")
    public Result updateDeptById(Menu menu) {
        try {
            menuService.updateMenu(menu);
            return Result.ok("修改菜单成功!");
        } catch (Exception e) {
            log.error("修改菜单失败:", e);
            return Result.error("修改菜单失败!");
        }
    }

    /**
     * 批量删除菜单或按钮
     * 菜单id
     * @param
     * @return
     */
    @DeleteMapping("/deleteMenu/{mids}")
    public Result deleteMenuById(@PathVariable String mids) {
        try {
            String[] midArr = mids.split(StringPool.COMMA);
            menuService.deleteMenuByIds(midArr);
            return Result.ok("删除菜单成功!");
        } catch (Exception e) {
            log.error("删除菜单失败:", e);
            return Result.error("删除菜单失败!");
        }
    }
}
