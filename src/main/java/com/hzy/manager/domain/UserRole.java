package com.hzy.manager.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("p_user_role")
public class UserRole implements Serializable {
    private static final long serialVersionUID = 3795058844654523823L;
    private Long userId;
    private Long roleId;
}
