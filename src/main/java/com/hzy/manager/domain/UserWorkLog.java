package com.hzy.manager.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("p_user_worklog")
public class UserWorkLog implements Serializable {
    private static final long serialVersionUID = 6372744855513323415L;
    private Long userId;
    private Long id;
}
