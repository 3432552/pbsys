package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.dao.DeptMapper;
import com.hzy.manager.domain.Dept;
import com.hzy.manager.vo.Tree;
import com.hzy.manager.service.DeptService;
import com.hzy.manager.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> getDeptList() {
        return deptMapper.selectDeptList();
    }

    @Override
    public Dept selectListById(Long deptId) {
        Dept dept = deptMapper.selectById(deptId);
        return dept == null ? null : dept;
    }

    @Transactional
    @Override
    public void updateDept(Dept dept) {
        dept.setModifyTime(new Date());
        deptMapper.updateById(dept);
    }

    @Override
    public Tree<Dept> getDeptTree() {
        List<Tree<Dept>> trees = new ArrayList<>();
        List<Dept> deptTreeList = deptMapper.selectList(null);
        deptTreeList.forEach(dept -> {
            Tree<Dept> tree = new Tree<>();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getDeptName());
            trees.add(tree);
        });
        return TreeUtils.build(trees);
    }

    @Transactional
    public void addDept(Dept dept) {
        Long parentId = dept.getParentId();
        if (parentId == null) {
            dept.setParentId(0L);
        }
        dept.setCreateTime(new Date());
        deptMapper.insert(dept);
    }

    @Transactional
    @Override
    public void deleteDeptByIds(String[] ids) {
        deptMapper.deleteBatchIds(Arrays.asList(ids));
    }
}
