package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.Constant;
import com.hzy.manager.dao.MenuMapper;
import com.hzy.manager.dao.RoleMapper;
import com.hzy.manager.dao.RoleMenuMapper;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.domain.Role;
import com.hzy.manager.domain.RoleMenu;
import com.hzy.manager.dto.LoginUser;
import com.hzy.manager.dto.Tree;
import com.hzy.manager.dto.router.RouterMeta;
import com.hzy.manager.dto.router.VueRouter;
import com.hzy.manager.service.MenuService;
import com.hzy.manager.util.DateUtil;
import com.hzy.manager.util.MD5Util;
import com.hzy.manager.util.TreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Transactional
    @Override
    public void addMenu(Menu menu) {
        Long parentId = menu.getParentId();
        if (parentId == null) {
            menu.setParentId(0L);
        }
        if (menu.getType().equals(Constant.BUTTON)) {
            menu.setIcon(null);
            menu.setPath(null);
        }
        menu.setCreateTime(new Date());
        menuMapper.insert(menu);
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.USER_CACHE));
        Role role = roleMapper.getUserRole(loginUser.getId());
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(role.getId());
        roleMenu.setMenuId(menu.getId());
        roleMenuMapper.insert(roleMenu);
    }

    @Override
    public void updateMenu(Menu menu) {
        if (menu.getType().equals(Constant.BUTTON)) {
            menu.setIcon(null);
            menu.setPath(null);
        }
        menu.setModifyTime(new Date());
        menuMapper.updateById(menu);
    }

    @Override
    public List<VueRouter<Menu>> getUserRouters() {
        List<VueRouter<Menu>> routes = new ArrayList<>();
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.USER_CACHE));
        List<Menu> menus = menuMapper.getMenuByUserId(loginUser.getId());
        menus.forEach(menu -> {
            VueRouter<Menu> route = new VueRouter<>();
            route.setId(menu.getId().toString());
            route.setParentId(menu.getParentId().toString());
            route.setIcon(menu.getIcon());
            route.setPath(menu.getPath());
            route.setComponent(menu.getComponent());
            route.setName(menu.getMenuName());
            route.setMeta(new RouterMeta(true, null));
            routes.add(route);
        });
        return TreeUtils.buildVueRouter(routes);
    }

    @Override
    public Tree<Menu> getMenuTree(String userName, String type) {
        List<Tree<Menu>> menuTree = new ArrayList<>();
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.USER_CACHE));
        List<Menu> menuList = menuMapper.getMenuByCondition(userName, type, loginUser.getId());
        menuList.forEach(menu -> {
            Tree<Menu> menuTree1 = new Tree<>();
            menuTree1.setId(menu.getId().toString());
            menuTree1.setText(menu.getMenuName());
            menuTree1.setIcon(menu.getIcon());
            menuTree1.setType(menu.getType());
            menuTree1.setPerms(menu.getPerms());
            menuTree1.setPath(menu.getPath());
            menuTree1.setCreteTime(DateUtil.getDateTime(menu.getCreateTime()));
            menuTree1.setParentId(menu.getParentId().toString());
            menuTree.add(menuTree1);
        });
        return TreeUtils.build(menuTree);
    }

    @Override
    public Menu findMenuById(Long mid) {
        return menuMapper.selectById(mid);
    }

    @Override
    public void deleteMenu(Long mid) {
        //先删除菜单
        menuMapper.deleteById(mid);
        //再删除关联表角色和菜单
        roleMenuMapper.deleteRoleIdByMenuId(mid);
    }
}
