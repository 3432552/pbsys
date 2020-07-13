package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.UserRole;
import org.apache.ibatis.annotations.Param;

public interface UserRoleMapper extends BaseMapper<UserRole> {
    //根据用户id删除角色Id
    void deleteRoleIdByUid(String uid);
    //删除用户角色中间表删除当前用户的修改工作日志的角色
    void deleteUserRoleById(@Param("uId") Long uId,@Param("rId") Long rId);
}
