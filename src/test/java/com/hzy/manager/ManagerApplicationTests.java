package com.hzy.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
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
import javax.print.attribute.HashDocAttributeSet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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

    @Test
    void contextLoads23() {
        Map<Integer, String> userMap = new HashMap<>();
        List<User> userList = userService.list();
        for (User u : userList) {
            userMap.put(u.getId().intValue(), u.getRealName());
        }
        String workDate = "2020-11-16";
        List<Schedule> workUserList = scheduleMapper.selectList(new LambdaQueryWrapper<Schedule>().eq(Schedule::getWorkDate, workDate));
        String workString = "";
        for (Schedule w : workUserList) {
            workString += w.getTrioUserid() + "," + w.getVcpMpUserid() + "," + w.getLvUserid() + "," + w.getTrtcUserid() + "," + w.getStudyOtherUserid() + ",";
        }
        System.out.println("字符串======》" + workString.toString());
        String[] s = workString.split(StringPool.COMMA);
        for (int i = 0; i < s.length; i++) {
            System.out.println("所有用户id:" + s[i]);
            System.out.println("所有播控人员:" + userMap.get(Integer.valueOf(s[i])));
        }
        Stream<String> stream = Arrays.stream(s);
        List<String> workList = stream.distinct().collect(toList());
        System.out.println("去重后的======也就是工作的人员====》" + workList+"\n");
        List<BroadcastUserVo> userList1 = userService.selectAllBroadcastUser();
        userList1.forEach(System.out::print);
        List<String> broadUserAllString = new ArrayList<>();
        userList1.forEach(m -> {
            broadUserAllString.add(String.valueOf(m.getId()));
        });
        System.out.println("播控所有人员id====》" + broadUserAllString+"\n");
        //取差集
        List<String> reduce = broadUserAllString.stream().filter(item -> !workList.contains(item)).collect(toList());
        System.out.println("取差集后的======也就是休息的人员====》" + reduce);
    }

    //获取用户权限
    @Test
    void contextLoads12() {
        List<String> stringList = new ArrayList<>();
        stringList.add("a");
        stringList.add("d");
        stringList.add("k");
        System.out.println(String.join(",", stringList));
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
