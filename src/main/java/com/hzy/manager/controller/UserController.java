package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Result;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.common.exception.LoginException;
import com.hzy.manager.domain.User;
import com.hzy.manager.vo.LoginUser;
import com.hzy.manager.service.UserService;
import com.hzy.manager.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 检查用户名是否存在
     *
     * @param userName
     * @return
     * @throws LoginException
     */
    @GetMapping("/checkUserName/{userName}")
    public boolean checkUserName(@PathVariable String userName) throws LoginException {
        LoginUser loginUser = userService.findByUserToRegister(userName);
        if (loginUser == null) {
            return true;
        } else {
            throw new LoginException("该用户名已被占用!");
        }
    }

    /**
     * 用户列表并实现分页
     *
     * @param user
     * @param currentNo
     * @param pageSize
     * @return
     */
    @GetMapping("/selectUserList/{currentNo}/{pageSize}")
    public Result userList(User user, @PathVariable Integer currentNo, @PathVariable Integer pageSize) {
        int totalNum = userService.count();
        PageUtils<User> pageUtils = new PageUtils<>(currentNo, pageSize, totalNum);
        Map<String, Object> map = new HashMap<>();
        map.put("realName", user.getRealName());
        map.put("deptId", user.getDeptId());
        map.put("status", user.getStatus());
        map.put("offSet", pageUtils.getOffset());
        map.put("pageSize", pageUtils.getPageSize());
        List<User> list = userService.getUserList(map);
        pageUtils.setPageList(list);
        return Result.ok(pageUtils);
    }

    /**
     * 新增用户
     *
     * @param user 可批量新增角色信息(一个用户对应一个角色)
     * @return deptId, userName, roleId
     * @throws BusinessException
     */
    @PostMapping("/addUser")
    public Result insertUser(User user) throws BusinessException {
        userService.addUser(user);
        return Result.ok("新增用户成功!");
    }

    /**
     * 根据用户ID查询一条用户信息用于修改
     *
     * @param uid
     * @return
     */
    @GetMapping("/selectUserById/{uid}")
    public Result selUserById(@PathVariable Long uid) {
        try {
            User user = userService.getUserById(uid);
            return Result.ok(user);
        } catch (Exception e) {
            log.error("查询用户失败:", e);
            return Result.error("查询用户失败!");
        }
    }

    /**
     * 修改用户信息，可批量修改角色信息
     * 必传的,id,deptId,roleId
     *
     * @param user
     * @return
     */
    @PutMapping("/updateUser")
    public Result updateDeptById(User user) {
        try {
            userService.updateUser(user);
            return Result.ok("修改用户成功!");
        } catch (Exception e) {
            log.error("修改用户失败:", e);
            return Result.error("修改用户失败!");
        }
    }

    /**
     * 可批量删除用户(也把用户角色信息一起删除了)
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteUser/{ids}")
    public Result delUserByIds(@PathVariable String ids) {
        try {
            String[] arrIds = ids.split(StringPool.COMMA);
            userService.deleteUserByIds(arrIds);
            return Result.ok("删除用户成功!");
        } catch (Exception e) {
            log.error("删除用户失败:", e);
            return Result.error("删除用户失败!");
        }
    }
}
