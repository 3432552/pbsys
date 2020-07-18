package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.domain.Approval;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ApprovalMapper extends BaseMapper<Approval> {
    //多条件查询并分页
    List<Page<Approval>> selectApprovalList(@Param("approval") Approval approval, Page<Approval> approvalPage);
}
