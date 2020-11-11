package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.common.Result;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.common.exception.LoginException;
import com.hzy.manager.dao.UserMapper;
import com.hzy.manager.domain.User;
import com.hzy.manager.vo.BroadcastUserVo;
import com.hzy.manager.vo.LoginUser;
import com.hzy.manager.service.UserService;
import com.hzy.manager.util.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.build.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "用户控制类")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    /**
     * 检查用户名是否存在
     *
     * @param userName
     * @return
     * @throws LoginException
     */
    @ApiOperation(value = "检查用户名是否存在")
    @ApiImplicitParam(name = "userName", value = "用户名")
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
     * 用户列表并实现分页(mybatisPlus实现物理分页)
     *
     * @param user
     * @param currentNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "realName", value = "真实名字", required = true),
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true),
            @ApiImplicitParam(name = "status", value = "用户账号状态", required = true)
    })
    @GetMapping("/selectUserList/{currentNo}/{pageSize}")
    public Result userList(@RequestBody User user, @PathVariable Integer currentNo, @PathVariable Integer pageSize) {
        try {
            Page<User> page = new Page<>(currentNo, pageSize);
            List<Page<User>> pageList = userService.getUserAndDeptPage(user, page);
            Map<String, Object> map = new HashMap<>();
            map.put("pageList", pageList);
            map.put("page", page);
            return Result.ok(map);
        } catch (Exception e) {
            log.error("查询用户列表失败:", e);
            return Result.error("查询用户列表失败");
        }
    }

    /**
     * 新增用户
     *
     * @param user 可批量新增角色信息(一个用户对应一个角色)
     * @return deptId, userName, roleId
     * @throws BusinessException
     */
    @ApiOperation(value = "新增用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true),
            @ApiImplicitParam(name = "realName", value = "真实名字", required = true),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true),
            @ApiImplicitParam(name = "sex", value = "性别", required = true)
    })
    @PostMapping("/addUser")
    public Result insertUser(@RequestBody User user) throws BusinessException {
        userService.addUser(user);
        return Result.ok("新增用户成功!");
    }

    /**
     * 根据用户ID查询一条用户信息用于修改
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "根据用户id查询一条用户信息")
    @ApiImplicitParam(name = "id", value = "用户id", required = true)
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
    @ApiOperation(value = "修改用户信息")
    @ApiImplicitParam(name = "User", value = "User实体", required = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true),
            @ApiImplicitParam(name = "id(Long)", value = "用户id", required = true),
            @ApiImplicitParam(name = "roleId(String)", value = "角色Ids[字符串如：1,2,3]", required = true),
            @ApiImplicitParam(name = "userName", value = "用户名[这个框禁用,只能看]", required = true),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true),
            @ApiImplicitParam(name = "realName", value = "真实名字", required = true),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true),
            @ApiImplicitParam(name = "status", value = "用户账号状态", required = true),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true),
            @ApiImplicitParam(name = "sex", value = "性别", required = true)
    })
    @PutMapping("/updateUser")
    public Result updateDeptById(@RequestBody User user) {
        try {
            userService.updateUser(user);
            return Result.ok("修改用户成功!");
        } catch (Exception e) {
            log.error("修改用户失败:", e);
            return Result.error("修改用户失败!");
        }
    }

    @ApiOperation(value = "查询所有播控人员姓名", notes = "无参数")
    @GetMapping("/selectBroadcastUser")
    public Result selBroadcastUser() {
        try {
            List<BroadcastUserVo> b = userService.selectAllBroadcastUser();
            return Result.ok(b);
        } catch (Exception e) {
            log.error("加载所有播控人员失败:", e);
            return Result.error("加载所有播控人员失败:");
        }
    }

    /**
     * 可批量删除用户(也把用户角色信息一起删除了)
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除用户信息")
    @ApiImplicitParam(name = "id", value = "可批量删除,用户id字符串例如:1,2,3,4", required = true)
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

    /**
     * 修改用户头像
     *
     * @return
     */
    @ApiOperation(value = "修改用户头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "avatarUrl", value = "用户头像url", required = true),
            @ApiImplicitParam(name = "id", value = "用户id", required = true)
    })
    @PutMapping("/updateAvatar/{userAvatar}/{uId}")
    public Result editAvatar(@PathVariable String userAvatar, @PathVariable Long uId) {
        try {
            User user = new User();
            user.setId(uId);
            user.setAvatarUrl(userAvatar);
            userService.updateById(user);
            return Result.ok("修改用户头像成功");
        } catch (Exception e) {
            log.error("修改用户头像失败:", e);
            return Result.error("修改用户头像失败");
        }
    }

    /**
     * 修改用户登录密码
     * password,newPwd,confirmPwd
     *
     * @return
     */
    @ApiOperation(value = "修改用户登录密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "用户登录密码", required = true),
            @ApiImplicitParam(name = "newPwd", value = "新密码", required = true),
            @ApiImplicitParam(name = "confirmPwd", value = "确认密码", required = true)
    })
    @PutMapping("/updateUserPwd")
    public Result updateUserPwd(@RequestBody User user) throws BusinessException {
        int res = userService.updatePwd(user);
        if (res > 0) {
            return Result.ok("修改用户登录密码成功");
        } else {
            return Result.ok("修改用户登录密码失败");
        }
    }
}
