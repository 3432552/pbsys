package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.Menu;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {

    //根据用户名查找拥有的权限
    List<Menu> getUserPermissions(String userName);

    //根据用户id查找拥有的菜单(左侧树使用只查找菜单)
    List<Menu> getMenuByUserId(Long uid);

    //查找菜单列表(根据用户名和菜单名和类型)
    List<Menu> getMenuByCondition(@Param("menuName") String menuName, @Param("type") String type, @Param("uid") Long uid);
}
