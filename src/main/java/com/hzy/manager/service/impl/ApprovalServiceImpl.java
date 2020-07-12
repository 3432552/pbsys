package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.dao.ApprovalMapper;
import com.hzy.manager.domain.Approval;
import com.hzy.manager.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ApprovalServiceImpl extends ServiceImpl<ApprovalMapper,Approval> implements ApprovalService {
    @Autowired
    private ApprovalMapper approvalMapper;
    @Override
    public List<Approval> getWorkLogList(Map<String, Object> map) {
        return approvalMapper.selectApprovalList(map);
    }
}
