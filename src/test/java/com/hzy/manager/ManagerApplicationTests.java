package com.hzy.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.dao.*;
import com.hzy.manager.domain.*;
import com.hzy.manager.service.RoleService;
import com.hzy.manager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
class ManagerApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private WorkLogMapper workLogMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private LoginUserMapper loginUserMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ApprovalMapper approvalMapper;

    //获取用户角色
    @Test
    void contextLoads() {
        String name = "wzh";
        List<Role> roleList = roleMapper.getUserRoles(name);
        roleList.forEach(System.out::println);
    }

    @Test
    void contextLoads23() {
        List<WorkLog> workLogMes = workLogMapper.selWorkLogListByUid(2L, "2020-7-11");
        workLogMes.forEach(System.out::println);
    }

    //获取用户权限
    @Test
    void contextLoads12() {
        String name = "wzh";
        List<Menu> roleList = menuMapper.getUserPermissions(name);
        roleList.forEach(System.out::println);
    }

    @Test
    void contextLoads1() {
        String email = "13544352@163.com";
        Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        if (p.matcher(email).matches() == false) {
            System.out.println("邮箱格式不正确!");
        } else {
            System.out.println("邮箱格式正确!");
        }
    }

    @Test
    void contextLoads4() {
        List<Menu> menuList = menuMapper.getMenuByUserId(1L);
        menuList.forEach(System.out::println);
    }

    @Test
    void contextLoads3() {

        //System.out.println("分页结果:"+page.toString());

    }
}
