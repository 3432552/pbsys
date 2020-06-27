package com.hzy.manager.util;

import com.fasterxml.jackson.databind.ser.std.StdArraySerializers;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页通用工具类
 *
 * @param <T>
 */
@Data
public class PageUtils<T> {
    //偏移量
    private Integer offset;
    //页面容量
    private Integer pageSize;
    //当前页码
    private Integer currentNo;
    //总条数
    private Integer totalNums;
    //总页码数(也可当尾页)
    private Integer totalPages;
    //首页
    private Integer firstPage = 1;
    //上一页
    private Integer prevPage;
    //下一页
    private Integer nextPage;
    //当前页的起始行数
    private Integer currentStartNum;
    //当前页的结束行数
    private Integer currentEndNum;

    //数据集合
    List<T> pageList = new ArrayList<>();

    public PageUtils(Integer currentNo, Integer pageSize, Integer totalNums) {
        this.offset = (currentNo - 1) * pageSize;
        this.pageSize = pageSize;
        this.currentNo = currentNo;
        this.totalNums = totalNums;
        this.totalPages = (int) Math.ceil((double) totalNums / pageSize);
        this.prevPage = currentNo - 1;
        this.nextPage = currentNo + 1;
        this.currentStartNum = (currentNo - 1) * pageSize + 1;
        this.currentEndNum = totalNums / pageSize >= currentNo ? (currentNo - 1) * pageSize + pageSize : (currentNo - 1) * pageSize + (totalNums % pageSize);
    }

}
