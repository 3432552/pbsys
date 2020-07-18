package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.domain.WorkLog;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WorkLogMapper extends BaseMapper<WorkLog> {
    //查全部工作日志
    List<WorkLog> selectWorkLogList(Map<String, Object> map);

    //有多条件查询,查询总条数
    int selectWorkLogListCount(String realName);

    //播控人员只能看到自己的工作日志
    List<WorkLog> selectWorkLogListByBoKong(Map<String, Object> map);

    //根据用户id查找工作日志
    List<WorkLog> selWorkLogListByUid(@Param("uid") Long uid, @Param("workDate") String workDate);


}
