package com.hzy.manager.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.Result;
import com.hzy.manager.domain.Dict;
import com.hzy.manager.service.DictService;
import com.hzy.manager.util.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import sun.security.krb5.internal.crypto.RsaMd5CksumType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wzh
 * @since 2020-10-14
 */
@Slf4j
@RestController
@Api(tags = "字典控制类")
@RequestMapping("/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    @ApiOperation(value = "查看所有字典信息", notes = "带分页,currentNo:当前页;pageSize:页面容量")
    @PostMapping("/getAllDicInfo/{currentNo}/{pageSize}")
    public Result selAllDicInfo(@PathVariable Integer currentNo, @PathVariable Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("currentNo", currentNo);
        map.put("pageSize", pageSize);
        List<Dict> dictList = null;
        PageUtils<Dict> dictPageUtils = null;
        try {
            int totalCount = dictService.count();
            dictList = dictService.getDictListByPage(map);
            dictPageUtils = new PageUtils<>(currentNo, pageSize, totalCount);
            dictPageUtils.setPageList(dictList);
        } catch (Exception e) {
            log.error("查询所有字典信息失败:", e);
            return Result.error("查询所有字典信息失败");
        }
        return Result.ok(dictPageUtils);
    }

    @ApiOperation(value = "新增字典信息")
    @PostMapping("/addDicInfo")
    public Result insertDicInfo(@RequestBody Dict dict) {
        int res = dictService.addDic(dict);
        if (res > 0) {
            return Result.ok("新增字典信息成功");
        } else {
            return Result.ok("新增字典信息失败");
        }
    }

    @ApiOperation(value = "查看一条字典信息【为了修改字典】")
    @PostMapping("/getOneDicInfo/{dictId}")
    public Result selOneDicInfo(@PathVariable Long dictId) {
        List<Dict> oneDictList = null;
        try {
            oneDictList = dictService.list(new LambdaQueryWrapper<Dict>().eq(Dict::getDictId, dictId));
        } catch (Exception e) {
            log.error("查询一条字典信息失败:", e);
            return Result.error("查询一条字典信息失败");
        }
        return Result.ok(oneDictList);
    }

    @ApiOperation(value = "修改字典信息")
    @PutMapping("/updateDicInfo")
    public Result upDicInfo(@RequestBody Dict dict) {
        int res = dictService.updateDic(dict);
        if (res > 0) {
            return Result.ok("修改字典信息成功");
        } else {
            return Result.ok("修改字典信息失败");
        }
    }

    @ApiOperation(value = "删除字典信息")
    @DeleteMapping("/deleteDicInfo/{dictIds}")
    public Result delDicInfo(@PathVariable String dictIds) {
        boolean a = dictService.removeByIds(Arrays.asList(dictIds.split(StringPool.COMMA)));
        if (a == true) {
            return Result.ok("删除字典信息成功");
        } else {
            return Result.ok("删除字典信息失败");
        }
    }
}
