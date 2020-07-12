package com.hzy.manager.controller;

import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Approval;
import com.hzy.manager.service.ApprovalService;
import com.hzy.manager.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/approval")
public class ApprovalController {
    @Autowired
    private ApprovalService approvalService;

    /**
     * 查询全部的审批申请记录
     *
     * @param approval
     * @param currentNo
     * @param pageSize
     * @return
     */
    @GetMapping("/selectApprovalList/{currentNo}/{pageSize}")
    public Result selApprovalList(Approval approval, @PathVariable Integer currentNo, @PathVariable Integer pageSize) {
        try {
            int totalNum = approvalService.count();
            PageUtils<Approval> pageUtils = new PageUtils<>(currentNo, pageSize, totalNum);
            Map<String, Object> map = new HashMap<>();
            map.put("realName", approval.getRealName());
            map.put("offeSet", pageUtils.getOffset());
            map.put("pageSize", pageUtils.getPageSize());
            List<Approval> approvalList = approvalService.getWorkLogList(map);
            pageUtils.setPageList(approvalList);
            return Result.ok(pageUtils);
        } catch (Exception e) {
            log.error("查询审批申请信息失败:", e);
            return Result.error("查询审批申请信息失败!");
        }
    }
}
