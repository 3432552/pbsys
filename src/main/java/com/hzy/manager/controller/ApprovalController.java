package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Approval;
import com.hzy.manager.service.ApprovalService;
import com.hzy.manager.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 多条件查询参数:realName(根据用户真实名字),approvalStatus(审核状态)
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
            map.put("approvalStatus", approval.getApprovalStatus());
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

    /**
     * 根据审批id进行查询一条审批申请信息，为了修改审批申请信息
     *
     * @return
     */
    @GetMapping("/selectApprovalById/{aId}")
    public Result selectApprovalByIdMes(@PathVariable Long aId) {
        try {
            Approval approval = approvalService.selectById(aId);
            return Result.ok(approval);
        } catch (Exception e) {
            log.error("查询一条审批申请信息失败:", e);
            return Result.error("查询一条审批申请信息失败!");
        }
    }

    /**
     * 修改审批申请信息,根据id
     *
     * @param approval
     * @return
     */
    @PutMapping("/updateApproval")
    public Result updateApproval(Approval approval) {
        try {
            approvalService.updateApproval(approval);
            return Result.ok("修改审批申请信息成功");
        } catch (Exception e) {
            log.error("修改审批申请信息失败:", e);
            return Result.error("修改审批申请信息失败");
        }

    }

    /**
     * 批量删除审批申请信息
     *
     * @return
     */
    @DeleteMapping("/deleteApprovalByIds/{aIds}")
    public Result deleteApprovalByIdsMes(@PathVariable String aIds) {
        try {
            String[] aIdArr = aIds.split(StringPool.COMMA);
            approvalService.deleteApprovalByIds(aIdArr);
            return Result.ok("删除审批申请信息成功");
        } catch (Exception e) {
            log.error("删除审批申请信息失败:", e);
            return Result.error("删除审批申请信息失败");
        }
    }

    /**
     * 审批申请通过
     * id(审批id)
     *
     * @return
     */
    @PostMapping("/approvalPass")
    public Result approvalPass(Approval approval) {
        try {
            approvalService.updateApprovalStatusOk(approval);
            return Result.ok("审批通过提交成功");
        } catch (Exception e) {
            log.error("审批通过提交失败:", e);
            return Result.error("审批通过提交失败");
        }
    }

    /**
     * 审批申请不通过
     * id(审批id)
     *
     * @return
     */
    @PostMapping("/approvalNoPass")
    public Result approvalNoPass(Approval approval) {
        try {
            approvalService.updateApprovalStatusNoOk(approval);
            return Result.ok("审批不通过提交成功");
        } catch (Exception e) {
            log.error("审批不通过提交失败:", e);
            return Result.error("审批不通过提交失败");
        }
    }
}
