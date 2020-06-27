package com.hzy.manager.controller;

import com.hzy.manager.common.Result;
import com.hzy.manager.common.exception.LoginException;
import com.hzy.manager.domain.User;
import com.hzy.manager.dto.LoginUser;
import com.hzy.manager.service.UserService;
import com.hzy.manager.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
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
    public Result userList(@RequestBody User user, @PathVariable Integer currentNo, @PathVariable Integer pageSize) {
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

}
