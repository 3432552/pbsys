package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.Dept;

import java.util.List;

public interface DeptMapper extends BaseMapper<Dept> {
    List<Dept> selectDeptList();
}
