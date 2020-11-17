package com.hzy.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.dao.*;
import com.hzy.manager.domain.*;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.service.RoleService;
import com.hzy.manager.service.UserService;
import com.hzy.manager.util.EncryptUtil;
import com.hzy.manager.vo.BroadcastUserVo;
import com.hzy.manager.vo.LoginUser;
import org.apache.logging.log4j.util.Base64Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private ScheduleMapper scheduleMapper;

    //验证Base64编码和解码
    @Test
    void contextLoads23() {
        String name = "654363";
        String encodeString = Base64Utils.encodeToString(name.getBytes());
        System.out.println("加密的参数:" + encodeString);
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
        String token = "aee10d889b6f4de9937455e5e5124591";
        boolean a = redisTemplate.hasKey(token);
        System.out.println("是否有这个用户:" + a);
    }

    /**
     * 生成图片base64
     */
    @Test
    void contentLoad4() {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPEG", byteArrayOutputStream);
            String imageBase64 = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            System.out.println("图片base64:" + imageBase64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
