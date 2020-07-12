package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Role;
import com.hzy.manager.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @PostMapping("/addRole")
    public Result addRoleMes(Role role) {
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
    @PutMapping("/updateRole")
    public Result updateRoleMes(Role role) {
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
