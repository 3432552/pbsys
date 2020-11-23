package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.Constant;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.common.exception.LoginException;
import com.hzy.manager.dao.LoginUserMapper;
import com.hzy.manager.dao.UserMapper;
import com.hzy.manager.dao.UserRoleMapper;
import com.hzy.manager.domain.User;
import com.hzy.manager.domain.UserRole;
import com.hzy.manager.dto.UserDto;
import com.hzy.manager.util.HttpServletUtil;
import com.hzy.manager.vo.BroadcastUserVo;
import com.hzy.manager.vo.LoginUser;
import com.hzy.manager.service.UserService;
import com.hzy.manager.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private LoginUserMapper loginUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public LoginUser findByName(String username, String password) throws LoginException {
        if (StringUtils.isEmpty(username)) {
            throw new LoginException("用户名不能为空");
        }
        LoginUser user = loginUserMapper.findByUserName(username);
        if (user == null) {
            throw new LoginException("用户或密码错误");
        }
        if (user.getStatus().equals(Constant.LOCK)) {
            throw new LoginException("账户已冻结");
        }
        if (!StringUtils.equals(user.getPassword(), MD5Util.encrypt(username, password))) {
            throw new LoginException("密码输入错误");
        }
        //更新用户登录时间
        User u = new User();
        u.setId(user.getId());
        u.setLastLoginTime(new Date());
        userMapper.updateLoginTime(u);
        return user;
    }

    @Override
    public List<Page<User>> getUserAndDeptPage(UserDto userDto, Page<User> userPage) {
        return userMapper.selectUserAndDeptPage(userDto, userPage);
    }

    public LoginUser findByUserToRegister(String userName) throws LoginException {
        LoginUser loginUser = loginUserMapper.findByUserName(userName);
        return loginUser == null ? null : loginUser;
    }

    @Override
    public List<BroadcastUserVo> selectAllBroadcastUser() {
        return userMapper.getAllBroadcastUser();
    }

    @Override
    public List<BroadcastUserVo> getBlurBroadcastUser(String realName) {
        return userMapper.selectBlurBroadcastUser(realName);
    }

    @Transactional(rollbackFor = Exception.class)
    public void register(User user) throws BusinessException {
        if (StringUtils.isEmpty(user.getUserName())) {
            throw new BusinessException("用户名不能为空");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new BusinessException("密码不能为空");
        }
        user.setPassword(MD5Util.encrypt(user.getUserName(), user.getPassword()));
        if (StringUtils.isEmpty(user.getRealName())) {
            throw new BusinessException("真实姓名不能为空");
        }
        if (user.getDeptId() == null) {
            throw new BusinessException("部门不能为空");
        }
        User u = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, user.getUserName()));
        if (u != null) {
            throw new BusinessException("用户名已被占用");
        }
        try {
            user.setAvatarUrl("default.jpg");
            user.setCreateTime(new Date());
            userMapper.insert(user);
            //如果部门是播控组,则给用户一个播控的角色,其他部门都是注册用户角色
            if (user.getDeptId() == 8) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(3L);
                userRoleMapper.insert(userRole);
            } else {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(6L);
                userRoleMapper.insert(userRole);
            }
        } catch (Exception e) {
            log.error("用户注册失败:", e);
            throw new BusinessException("用户注册失败");
        }
    }

    @Override
    public User getUserById(Long uid) {
        return userMapper.selectByUid(uid);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(User user) throws BusinessException {
        if (StringUtils.isEmpty(user.getUserName())) {
            throw new BusinessException("用户名不能为空");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new BusinessException("密码不能为空");
        }
        user.setPassword(MD5Util.encrypt(user.getUserName(), user.getPassword()));
        if (StringUtils.isEmpty(user.getRealName())) {
            throw new BusinessException("真实姓名不能为空");
        }
        if (StringUtils.isEmpty(user.getPhone())) {
            throw new BusinessException("手机号不能为空");
        }
        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
        if (pattern.matcher(user.getPhone()).matches() == false) {
            throw new BusinessException("你输入的手机号格式不正确!");
        }
        if (StringUtils.isEmpty(user.getEmail())) {
            throw new BusinessException("邮箱不能为空");
        }
        if (user.getSex() == null) {
            throw new BusinessException("性别不能为空");
        }
        if (user.getDeptId() == null) {
            throw new BusinessException("部门不能为空");
        }
        Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        if (p.matcher(user.getEmail()).matches() == false) {
            throw new BusinessException("你输入的邮箱格式不正确");
        }
        try {
            user.setCreateTime(new Date());
            userMapper.insert(user);
            //保存用户角色,可批量保存用户角色
            String[] roles = user.getRoleId().split(StringPool.COMMA);
            Arrays.stream(roles).forEach(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(Long.valueOf(roleId));
                userRoleMapper.insert(userRole);
            });
        } catch (Exception e) {
            log.error("新增用户失败:", e);
            throw new BusinessException("新增用户失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUser(User user) {
        user.setModifyTime(new Date());
        userMapper.updateById(user);
        //可批量修改用户角色(先删除后新增间接修改角色id,直接修改不可行)
        String[] roleIds = user.getRoleId().split(StringPool.COMMA);
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId()));
        Arrays.asList(roleIds).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(Long.valueOf(roleId));
            userRoleMapper.insert(userRole);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUserByIds(String[] ids) {
        //删除用户
        userMapper.deleteBatchIds(Arrays.asList(ids));
        //删除用户角色,在for循环里一个个删的
        Arrays.asList(ids).forEach(uid -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(Long.valueOf(uid));
            userRoleMapper.deleteRoleIdByUid(userRole.getUserId().toString());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updatePwd(User user) throws BusinessException {
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(HttpServletUtil.getHeaderToken());
        String userName = loginUser.getUserName();
        LoginUser loginUser1 = loginUserMapper.findByUserName(userName);
        log.info("User对象:" + loginUser.toString());
        if (!StringUtils.equals(MD5Util.encrypt(userName, user.getPassword()), loginUser1.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        if (StringUtils.equals(user.getNewPwd(), user.getPassword())) {
            throw new BusinessException("原密码和新密码一致,请重新设置新密码");
        }
        if (!StringUtils.equals(user.getNewPwd(), user.getConfirmPwd())) {
            throw new BusinessException("新密码和确认密码不一致,请重新输入");
        }
        user.setId(loginUser1.getId());
        user.setPassword(MD5Util.encrypt(userName, user.getNewPwd()));
        int result = userMapper.updateById(user);
        return result;
    }
}
