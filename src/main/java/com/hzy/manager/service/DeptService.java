package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.domain.Dept;
import com.hzy.manager.vo.Tree;

import java.util.List;

public interface DeptService extends IService<Dept> {
    List<Dept> getDeptList();

    Tree<Dept> getDeptTree();

    void addDept(Dept dept);

    Dept selectListById(Long deptId);

    void updateDept(Dept dept);

    void deleteDeptByIds(String[] ids);
}
