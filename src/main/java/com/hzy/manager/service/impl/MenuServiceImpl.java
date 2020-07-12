package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.Constant;
import com.hzy.manager.common.authentication.JWTUtil;
import com.hzy.manager.dao.LoginUserMapper;
import com.hzy.manager.dao.MenuMapper;
import com.hzy.manager.dao.RoleMapper;
import com.hzy.manager.dao.RoleMenuMapper;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.domain.Role;
import com.hzy.manager.domain.RoleMenu;
import com.hzy.manager.vo.LoginUser;
import com.hzy.manager.vo.Tree;
import com.hzy.manager.vo.router.RouterMeta;
import com.hzy.manager.vo.router.VueRouter;
import com.hzy.manager.service.MenuService;
import com.hzy.manager.util.DateUtil;
import com.hzy.manager.util.MD5Util;
import com.hzy.manager.util.TreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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
    @Autowired
    private LoginUserMapper loginUserMapper;

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
        String token = redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY)).toString();
        log.info("当前操作用户的token:" + token);
        LoginUser loginUser = loginUserMapper.findByUserName(JWTUtil.getUsername(token));
        log.info("User对象:" + loginUser.toString());
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
        String token = redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY)).toString();
        log.info("当前操作用户的token:" + token);
        LoginUser loginUser = loginUserMapper.findByUserName(JWTUtil.getUsername(token));
        log.info("User对象:" + loginUser.toString());
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
        String token = redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY)).toString();
        log.info("当前操作用户的token:" + token);
        LoginUser loginUser = loginUserMapper.findByUserName(JWTUtil.getUsername(token));
        log.info("User对象:" + loginUser.toString());
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

    @Transactional
    @Override
    public void deleteMenuByIds(String[] mids) {
        //先删除菜单
        menuMapper.deleteBatchIds(Arrays.asList(mids));
        //再删除关联表角色和菜单
        roleMenuMapper.deleteRoleIdByMenuId(mids);
    }
}
