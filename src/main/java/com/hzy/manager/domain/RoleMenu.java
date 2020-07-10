package com.hzy.manager.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("p_role_menu")
public class RoleMenu implements Serializable {
    private static final long serialVersionUID = -6728459094805228010L;
    private Long roleId;
    private Long menuId;
}
