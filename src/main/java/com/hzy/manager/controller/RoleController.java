package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Role;
import com.hzy.manager.service.RoleService;
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

@RestController
@Slf4j
@Api(tags = "角色控制类")
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 查询角色列表接口
     *
     * @return
     */
    @ApiOperation(value = "查询所有角色")
    @GetMapping("/roleList")
    public Result roleList() {
        List<Role> roleList = null;
        try {
            roleList = roleService.roleList();
            return Result.ok(roleList);
        } catch (Exception e) {
            log.error("查询角色列表失败:", e);
            return Result.error("查询角色列表失败!");
        }
    }
    /**
     * 通过角色id得到这个角色的所有菜单ids和这个角色的信息(用于修改)
     *
     * @return
     */
    @ApiOperation(value = "根据角色id查到这个角色信息和角色所拥有的菜单权限menuIds(修改之前调用)")
    @ApiImplicitParam(name = "id", value = "角色id", required = true)
    @GetMapping("/getMenuIds/{roleId}")
    public Result getMenuIdsByRoleId(@PathVariable Long roleId) {
        Map<String, Object> map = null;
        try {
            List<Role> menuIdsList = roleService.getMenuIdsByRoleId(roleId);
            Role getRole = roleService.getRoleById(roleId);
            //创建集合把菜单ids加进去
            List<String> menuIdList = new ArrayList<>();
            menuIdsList.forEach(menuId -> {
                menuIdList.add(menuId.getMenuId().toString());
            });
            map = new HashMap<>();
            map.put("Role", getRole);
            map.put("menuIds", menuIdList);
        } catch (Exception e) {
            log.error("查询勾选的角色失败:", e);
            return Result.error("查询勾选的角色失败!");
        }
        return Result.ok(map);
    }

    /**
     * 新增角色
     *
     * @param role(roleName,remark,menuId=菜单id可多个)
     * @return
     */
    @ApiOperation(value = "新增角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true),
            @ApiImplicitParam(name = "remark", value = "角色描述", required = true),
            @ApiImplicitParam(name = "menuId", value = "把菜单树加载进来可选取多个菜单获得到菜单id拼接成字符串如：1,2,3,4", required = true)
    })
    @PostMapping("/addRole")
    public Result addRoleMes(@RequestBody Role role) {
        try {
            roleService.addRole(role);
            return Result.ok("新增角色成功!");
        } catch (Exception e) {
            log.error("新增角色失败:", e);
            return Result.error("新增角色失败!");
        }
    }
    /**
     * 修改角色
     *
     * @param role(roleName,remark,menuId=菜单id可多个)
     * @return
     */
    @ApiOperation(value = "修改角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true),
            @ApiImplicitParam(name = "remark", value = "角色描述", required = true),
            @ApiImplicitParam(name = "menuId", value = "可选取多个菜单获得到菜单id进行修改拼接成字符串如：1,2,3,4", required = true)
    })
    @PutMapping("/updateRole")
    public Result updateRoleMes(@RequestBody Role role) {
        try {
            roleService.updateRole(role);
            return Result.ok("修改角色成功!");
        } catch (Exception e) {
            log.error("修改角色失败:", e);
            return Result.error("修改角色失败!");
        }
    }

    /**
     * 可批量删除角色
     *
     * @param rId(角色id)
     * @return
     */
    @ApiOperation(value = "删除角色信息")
    @ApiImplicitParam(name = "id", value = "角色ids(可批量删除,拼接成字符串如：1,2,3,4)", required = true)
    @DeleteMapping("/deleteRole/{rId}")
    public Result deleteRoleById(@PathVariable String rId) {
        try {
            String[] id = rId.split(StringPool.COMMA);
            roleService.deleteRoleById(id);
            return Result.ok("删除成功!");
        } catch (Exception e) {
            log.error("删除角色失败:", e);
            return Result.error("删除角色失败!");
        }
    }
}
