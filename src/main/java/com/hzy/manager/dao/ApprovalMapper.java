package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.Approval;

import java.util.List;
import java.util.Map;

public interface ApprovalMapper extends BaseMapper<Approval> {
    List<Approval> selectApprovalList(Map<String,Object> map);
}
