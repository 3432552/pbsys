package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Approval;
import com.hzy.manager.service.ApprovalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "播控人员申请审批控制类")
@RequestMapping("/approval")
public class ApprovalController {
    @Autowired
    private ApprovalService approvalService;

    /**
     * 查询全部的审批申请记录(mybatisPlus实现物理分页)
     * 多条件查询参数:realName(根据用户真实名字),approvalStatus(审核状态)
     *
     * @param approval
     * @param currentNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "申请修改工作日志申请信息列表", notes = "带分页,currentNo:当前页;pageSize:页面容量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "realName", value = "真实名字", required = true),
            @ApiImplicitParam(name = "approvalStatus", value = "申请状态 0：待审批 1：审批通过 2：审批不通过", required = true)
    })
    @GetMapping("/selectApprovalList/{currentNo}/{pageSize}")
    public Result selApprovalList(@RequestBody Approval approval, @PathVariable Integer currentNo, @PathVariable Integer pageSize) {
        try {
            Page<Approval> page = new Page<>(currentNo, pageSize);
            List<Page<Approval>> pageList = approvalService.getApprovalList(approval, page);
            Map<String, Object> map = new HashMap<>();
            map.put("pageList", pageList);
            map.put("page", page);
            return Result.ok(map);
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
    @ApiOperation(value = "根据审批id查询一条审批申请信息")
    @ApiImplicitParam(name = "id", value = "审批id", required = true)
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
    @ApiOperation(value = "修改审批申请信息(审批是播控人员发起的管理员也不应该修改它,【不用调用这个接口】)")
    @PutMapping("/updateApproval")
    public Result updateApproval(@RequestBody Approval approval) {
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
    @ApiOperation(value = "根据审批id删除审批申请信息")
    @ApiImplicitParam(name = "id", value = "审批申请ids(可批量删除,拼接成字符串如：1,2,3,4)", required = true)
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
    @ApiOperation(value = "审批申请通过", notes = "不用传参数")
    @PostMapping("/approvalPass")
    public Result approvalPass(@RequestBody Approval approval) {
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
    @ApiOperation(value = "审批申请不通过", notes = "不用传参数")
    @PostMapping("/approvalNoPass")
    public Result approvalNoPass(@RequestBody Approval approval) {
        try {
            approvalService.updateApprovalStatusNoOk(approval);
            return Result.ok("审批不通过提交成功");
        } catch (Exception e) {
            log.error("审批不通过提交失败:", e);
            return Result.error("审批不通过提交失败");
        }
    }
}
