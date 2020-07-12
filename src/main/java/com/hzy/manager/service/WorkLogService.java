package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.domain.WorkLog;

import java.util.List;
import java.util.Map;

public interface WorkLogService extends IService<WorkLog> {
    List<WorkLog> getWorkLogList(Map<String,Object> map);
    List<WorkLog> getWorkLogListByBoKong(Map<String,Object> map);
    WorkLog worklog(Long logId);

    void deleteWorkLogById(String[] logIds);

    void addWorkLog(WorkLog workLog);

    void updateWorkLog(WorkLog workLog);
}
