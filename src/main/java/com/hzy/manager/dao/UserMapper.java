package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.domain.User;
import com.hzy.manager.dto.UserDto;
import com.hzy.manager.vo.BroadcastUserVo;
import org.apache.ibatis.annotations.Param;

import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {
    //多条件查询并分页
    List<Page<User>> selectUserAndDeptPage(@Param("user")UserDto userDto, Page<User> userPage);

    User selectByUid(Long uid);

    //更新登陆时间
    void updateLoginTime(User user);

    //根据播控角色查询所有播控人员
    List<BroadcastUserVo> getAllBroadcastUser();
    //模糊查询播控人员
    List<BroadcastUserVo> selectBlurBroadcastUser(String realName);
}
