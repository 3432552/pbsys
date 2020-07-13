package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.domain.WorkLog;

import java.util.List;
import java.util.Map;

public interface WorkLogService extends IService<WorkLog> {
    List<WorkLog> getWorkLogList(Map<String, Object> map);

    List<WorkLog> getWorkLogListByBoKong(Map<String, Object> map);

    WorkLog worklog(Long logId);

    void deleteWorkLogById(String[] logIds);

    int addWorkLog(WorkLog workLog) throws BusinessException;

    void updateWorkLog(WorkLog workLog);
}
