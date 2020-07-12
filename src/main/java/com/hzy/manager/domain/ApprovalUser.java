package com.hzy.manager.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("p_approval_user")
public class ApprovalUser implements Serializable {
    private static final long serialVersionUID = -5081517246898928810L;
    private Long aId;
    private Long uId;
}
