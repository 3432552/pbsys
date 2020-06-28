package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.domain.Dept;
import com.hzy.manager.dto.Tree;

import java.util.List;

public interface DeptService extends IService<Dept> {
    List<Dept> getDeptList();

    Tree<Dept> getDeptTree();

    void addDept(Dept dept);

    Dept selectListById(Long deptId);

    void updateDept(Dept dept);

    void deleteDeptByIds(String[] ids);
}
