package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.dto.Tree;
import com.hzy.manager.dto.router.VueRouter;

import java.util.List;
import java.util.Map;

public interface MenuService extends IService<Menu> {
    void addMenu(Menu menu);

    Tree<Menu> getMenuTree(Map<String, Object> map);

    List<VueRouter<Menu>> getUserRouters(String userName);
}
