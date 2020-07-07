package com.hzy.manager.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("p_user_role")
public class UserRole implements Serializable {
    private Long userId;
    private Long roleId;
}
