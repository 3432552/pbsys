package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.domain.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<Role> roleList();
}
