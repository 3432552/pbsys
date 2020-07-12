package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.domain.Role;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<Role> roleList();

    List<Role> getMenuIdsByRoleId(Long roleId);

    //根据角色Id查找一个角色对象
    Role getRoleById(Long roleId);
    //根据用户id查找一个角色对象
    Role getRoleByuId(Long uId);

    void addRole(Role role);

    void updateRole(Role role);

    void deleteRoleById(String[] rId);
}
