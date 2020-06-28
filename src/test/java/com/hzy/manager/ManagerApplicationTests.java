package com.hzy.manager;

import com.hzy.manager.common.Constant;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.dao.DeptMapper;
import com.hzy.manager.dao.LoginUserMapper;
import com.hzy.manager.dao.MenuMapper;
import com.hzy.manager.dao.UserMapper;
import com.hzy.manager.domain.Dept;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.domain.User;
import com.hzy.manager.dto.LoginUser;
import com.hzy.manager.service.UserService;
import com.hzy.manager.util.DateUtil;
import com.hzy.manager.util.MD5Util;
import net.bytebuddy.asm.Advice;
import org.assertj.core.api.IntArrayAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
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
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private LoginUserMapper loginUserMapper;

    @Test
    void contextLoads() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
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
        Dept dept = deptMapper.selectById(4L);
        System.out.println(dept.toString());
    }

    @Test
    void contextLoads3() {
        Map<String, Object> map = new HashMap<>();
        map.put("realName", "张三");
        map.put("deptId", "2");
        map.put("status", "0");
        map.put("pageNo", 1);
        map.put("pageSize", 3);
        List<User> userList = userMapper.selectUserAndDeptPage(map);
        System.out.println(userList.toString());
    }
}
