package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.Constant;
import com.hzy.manager.common.authentication.JWTUtil;
import com.hzy.manager.dao.ApprovalMapper;
import com.hzy.manager.dao.LoginUserMapper;
import com.hzy.manager.dao.RoleMenuMapper;
import com.hzy.manager.dao.UserRoleMapper;
import com.hzy.manager.domain.Approval;
import com.hzy.manager.domain.RoleMenu;
import com.hzy.manager.domain.UserRole;
import com.hzy.manager.service.ApprovalService;
import com.hzy.manager.util.MD5Util;
import com.hzy.manager.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ApprovalServiceImpl extends ServiceImpl<ApprovalMapper, Approval> implements ApprovalService {
    @Autowired
    private ApprovalMapper approvalMapper;
    @Autowired
    private LoginUserMapper loginUserMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Approval> getWorkLogList(Map<String, Object> map) {
        return approvalMapper.selectApprovalList(map);
    }

    @Override
    public void updateApproval(Approval approval) {
        approval.setModifyTime(new Date());
        approvalMapper.updateById(approval);
    }

    @Override
    public void deleteApprovalByIds(String[] aIds) {
        approvalMapper.deleteBatchIds(Arrays.asList(aIds));
    }

    @Override
    public Approval selectById(Long aId) {
        return approvalMapper.selectById(aId);
    }

    @Transactional
    @Override
    public void updateApprovalStatusOk(Approval approval) {
        String token = redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY)).toString();
        log.info("当前操作用户的token:" + token);
        LoginUser loginUser = loginUserMapper.findByUserName(JWTUtil.getUsername(token));
        log.info("User对象:" + loginUser.toString());
        //修改申请表状态approvalStatus为1
        approval.setModifyTime(new Date());
        approval.setApprovalStatus(Constant.APPROVALPASS);
        approvalMapper.updateById(approval);
        UserRole userRole = new UserRole();
        RoleMenu roleMenu = new RoleMenu();
        //用户角色中间表给当前播控用户新增一个修改播控日志的角色
        userRole.setUserId(loginUser.getId());
        userRole.setRoleId(4L);
        userRoleMapper.insert(userRole);
        //角色菜单中间表给当前播控用户新增一个修改播控日志的菜单
        roleMenu.setMenuId(11L);
        roleMenu.setRoleId(4L);
        roleMenuMapper.insert(roleMenu);
    }

    @Transactional
    @Override
    public void updateApprovalStatusNoOk(Approval approval) {
        approval.setModifyTime(new Date());
        approval.setApprovalStatus(Constant.APPROVALNOPASS);
        approvalMapper.updateById(approval);
    }
}
