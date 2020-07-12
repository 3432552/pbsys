package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.dao.UserWorkLogMapper;
import com.hzy.manager.domain.UserWorkLog;
import com.hzy.manager.service.UserWorkLogService;
import org.springframework.stereotype.Service;

@Service
public class UserWorkLogServiceImpl extends ServiceImpl<UserWorkLogMapper, UserWorkLog> implements UserWorkLogService {
}
