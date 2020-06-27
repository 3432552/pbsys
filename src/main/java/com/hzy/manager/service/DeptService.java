package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.domain.Dept;

import java.util.List;

public interface DeptService extends IService<Dept> {
    List<Dept> getDeptList();
}
