package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.Constant;
import com.hzy.manager.dao.MenuMapper;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.dto.Tree;
import com.hzy.manager.dto.router.RouterMeta;
import com.hzy.manager.dto.router.VueRouter;
import com.hzy.manager.service.MenuService;
import com.hzy.manager.util.DateUtil;
import com.hzy.manager.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

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
    }

    @Override
    public List<VueRouter<Menu>> getUserRouters(String userName) {
        List<VueRouter<Menu>> routes = new ArrayList<>();
        List<Menu> menus = menuMapper.getMenuByUserName(userName);
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
    public Tree<Menu> getMenuTree(Map<String, Object> map) {
        List<Tree<Menu>> menuTree = new ArrayList<>();
        List<Menu> menuList = menuMapper.getMenuByCondition(map);
        menuList.forEach(menu -> {
            Tree<Menu> menuTree1 = new Tree<>();
            menuTree1.setId(menu.getId().toString());
            menuTree1.setText(menu.getMenuName());
            menuTree1.setType(menu.getType());
            menuTree1.setPath(menu.getPath());
            menuTree1.setText(menu.getMenuName());
            menuTree1.setCreteTime(DateUtil.getDateTime(menu.getCreateTime()));
            menuTree1.setParentId(menu.getParentId().toString());
            menuTree.add(menuTree1);
        });
        return TreeUtils.build(menuTree);
    }
}
