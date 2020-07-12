package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.dao.RoleMapper;
import com.hzy.manager.dao.RoleMenuMapper;
import com.hzy.manager.dao.UserRoleMapper;
import com.hzy.manager.domain.Role;
import com.hzy.manager.domain.RoleMenu;
import com.hzy.manager.domain.UserRole;
import com.hzy.manager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> roleList() {
        return roleMapper.selectList(null);
    }

    @Override
    public List<Role> getMenuIdsByRoleId(Long roleId) {
        return roleMapper.selMenuIds(roleId);
    }

    @Override
    public Role getRoleById(Long roleId) {
        return roleMapper.selectById(roleId);
    }

    @Override
    public Role getRoleByuId(Long uId) {
        return roleMapper.getUserRole(uId);
    }

    @Transactional
    @Override
    public void addRole(Role role) {
        role.setCreateTime(new Date());
        roleMapper.insert(role);
        String[] menuId = role.getMenuId().split(StringPool.COMMA);
        //新增角色菜单中间表
        setRoleMenu(role, menuId);
    }

    private void setRoleMenu(Role role, String[] menuId) {
        Arrays.stream(menuId).forEach(menuIds -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(Long.valueOf(menuIds));
            roleMenuMapper.insert(roleMenu);
        });
    }

    @Override
    public void updateRole(Role role) {
        role.setModifyTime(new Date());
        //先把菜单表更新
        roleMapper.updateById(role);
        //根据角色id把角色菜单表删除
        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, role.getId()));
        String[] menuId = role.getMenuId().split(StringPool.COMMA);
        //再根据选中的menuId更新
        setRoleMenu(role, menuId);
    }

    @Override
    public void deleteRoleById(String[] rId) {
        //删除角色表
        roleMapper.deleteBatchIds(Arrays.asList(rId));
        //批量删除角色菜单表,根据角色id
        Arrays.stream(rId).forEach(rIds -> {
            roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, rIds));
        });
    }
}
