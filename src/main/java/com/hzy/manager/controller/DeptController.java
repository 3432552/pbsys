package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Dept;
import com.hzy.manager.dto.Tree;
import com.hzy.manager.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 查询所有部门
     *
     * @return
     */
    @GetMapping("/deptList")
    public Result selectDeptList() {
        List<Dept> deptList = null;
        try {
            deptList = deptService.getDeptList();
            return Result.ok(deptList);
        } catch (Exception e) {
            log.error("查询所有部门失败:", e);
            return Result.error("查询所有部门失败!");
        }
    }

    /**
     * 生成部门树
     *
     * @return
     */
    @GetMapping("/deptTree")
    public Result buildDeptTree() {
        Tree<Dept> deptTree = null;
        try {
            deptTree = deptService.getDeptTree();
        } catch (Exception e) {
            log.error("生成部门树失败:", e);
            return Result.error("生成部门树失败!");
        }
        return Result.ok(deptTree);
    }

    /**
     * 新增部门
     * 新增之前可以调用全部部门信息接口，部门名字不能重复
     * 必须参数:deptName,parentId
     *
     * @param dept
     * @return
     * @throws Exception
     */
    @PostMapping("/addDept")
    public Result addDept(Dept dept) {
        try {
            deptService.addDept(dept);
            return Result.ok("新增部门成功");
        } catch (Exception e) {
            log.error("新增部门失败:", e);
            return Result.error("新增部门失败!");
        }
    }

    /**
     * 通过部门Id查找一条部门信息为了修改
     *
     * @param deptId
     * @return
     */
    @GetMapping("/selectDeptById")
    public Result selDeptById(Long deptId) {
        try {
            Dept dept = deptService.selectListById(deptId);
            return Result.ok(dept);
        } catch (Exception e) {
            log.error("查询部门失败:", e);
            return Result.error("查询部门失败!");
        }
    }

    /**
     * 修改部门信息
     * 必传的,id
     *
     * @param dept
     * @return
     */
    @PutMapping("/updateDept")
    public Result updateDeptById(Dept dept) {
        try {
            deptService.updateDept(dept);
            return Result.ok("修改部门成功!");
        } catch (Exception e) {
            log.error("修改部门失败:", e);
            return Result.error("修改部门失败!");
        }
    }

    /**
     * 删除部门根据部门id,支持批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteDept")
    public Result delDeptById(@PathVariable String ids) {
        try {
            String[] idArray = ids.split(StringPool.COMMA);
            deptService.deleteDeptByIds(idArray);
            return Result.ok("删除部门成功!");
        } catch (Exception e) {
            log.error("删除部门失败:", e);
            return Result.error("删除部门失败!");
        }
    }
}
