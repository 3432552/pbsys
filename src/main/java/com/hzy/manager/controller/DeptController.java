package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Dept;
import com.hzy.manager.vo.Tree;
import com.hzy.manager.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "部门控制类")
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 查询所有部门
     *
     * @return
     */
    @ApiOperation(value = "查询所有部门信息")
    @GetMapping("/deptList")
    public Result selectDeptList() {
        List<Dept> deptList = null;
        try {
            deptList = deptService.getDeptList();
            return Result.ok(deptList);
        } catch (Exception e) {
            log.error("查询所有部门失败:", e);
            return Result.error("查询所有部门失败");
        }
    }

    /**
     * 生成部门树
     *
     * @return
     */
    @ApiOperation(value = "生成部门树")
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
    @ApiOperation(value = "新增部门信息")
    @ApiImplicitParam(name = "Dept", value = "Dept实体")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptName(String)", value = "部门名字", required = true),
            @ApiImplicitParam(name = "parentId(Long)", value = "部门父Id(就是父部门主键deptId)", required = true),
    })
    @PostMapping("/addDept")
    public Result addDept(@RequestBody Dept dept) {
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
     * @return
     */
    @ApiOperation(value = "根据部门id查找一条部门信息")
    @ApiImplicitParam(name = "deptId(Long)", value = "部门id", required = true)
    @PostMapping("/selectDeptById")
    public Result selDeptById(@RequestBody Dept dept1) {
        try {
            Dept dept = deptService.selectListById(dept1.getDeptId());
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
    @ApiOperation(value = "修改部门信息")
    @ApiImplicitParam(name = "Dept", value = "Dept实体", required = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true),
            @ApiImplicitParam(name = "deptName", value = "部门名字", required = true),
            @ApiImplicitParam(name = "parentId(Long)", value = "部门父Id", required = true),
    })
    @PutMapping("/updateDept")
    public Result updateDeptById(@RequestBody Dept dept) {
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
    @ApiOperation(value = "根据部门id删除部门信息(支持批量删除)")
    @ApiImplicitParam(name = "ids", value = "部门ids(String)[部门id字符串,例如:1,2,3]", required = true)
    @DeleteMapping("/deleteDeptById/{ids}")
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
