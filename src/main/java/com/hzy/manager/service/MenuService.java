package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.vo.Tree;
import com.hzy.manager.vo.router.VueRouter;

import java.util.List;

public interface MenuService extends IService<Menu> {
    void addMenu(Menu menu);

    Tree<Menu> getMenuTree(String userName, String type);

    List<VueRouter<Menu>> getUserRouters();

    Menu findMenuById(Long mid);

    void updateMenu(Menu menu);

    void deleteMenuByIds(String[] mids);
}
