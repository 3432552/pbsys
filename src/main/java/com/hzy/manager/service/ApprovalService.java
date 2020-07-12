package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.domain.Approval;

import java.util.List;
import java.util.Map;

public interface ApprovalService extends IService<Approval> {
    List<Approval> getWorkLogList(Map<String, Object> map);
}
