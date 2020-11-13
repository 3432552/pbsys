package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.vo.Tree;
import com.hzy.manager.vo.router.VueRouter;
import com.hzy.manager.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "菜单控制类")
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
    @ApiOperation(value = "根据当前登录用户查询这个用户所拥有的菜单,只有菜单用于左侧展示")
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
    @ApiOperation(value = "根据当前登录用户查询这个用户所拥有的菜单,菜单和按钮,用于菜单列表的展示(可多条件查询)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuName", value = "菜单名称", required = true),
            @ApiImplicitParam(name = "type", value = "菜单类型", required = true)
    })
    @PostMapping("/menuTree")
    public Result getMenu(@RequestBody Menu menu) {
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
    @ApiOperation(value = "新增菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuName", value = "菜单名称", required = true),
            @ApiImplicitParam(name = "type", value = "菜单类型", required = true),
            @ApiImplicitParam(name = "path", value = "菜单url", required = true),
            @ApiImplicitParam(name = "perms", value = "权限标识", required = true),
            @ApiImplicitParam(name = "icon", value = "菜单图标", required = true),
            @ApiImplicitParam(name = "parentId", value = "菜单父id,这个是传菜单的id", required = true)
    })
    @PostMapping("/addMenu")
    public Result addDept(@RequestBody Menu menu) {
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
     * @return
     */
    @ApiOperation(value = "根据菜单id获取一条菜单信息")
    @ApiImplicitParam(name = "id", value = "菜单id", required = true)
    @PostMapping("/selectMenuById")
    public Result selMenuById(@RequestBody Menu menu1) {
        try {
            Menu menu = menuService.findMenuById(menu1.getId());
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
    @ApiOperation(value = "修改菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuName", value = "菜单名称", required = true),
            @ApiImplicitParam(name = "type", value = "菜单类型", required = true),
            @ApiImplicitParam(name = "path", value = "菜单url", required = true),
            @ApiImplicitParam(name = "perms", value = "权限标识", required = true),
            @ApiImplicitParam(name = "icon", value = "菜单图标", required = true),
            @ApiImplicitParam(name = "parentId", value = "菜单父id,这个是传菜单的id", required = true)
    })
    @PutMapping("/updateMenu")
    public Result updateDeptById(@RequestBody Menu menu) {
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
     *
     * @param
     * @return
     */
    @ApiOperation(value = "删除菜单信息")
    @ApiImplicitParam(name = "id", value = "菜单ids(可批量删除,拼接成字符串如：1,2,3,4)", required = true)
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
