package com.hzy.manager.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author wzh
 * @since 2020-10-14
 */
@Data
@Accessors(chain = true)
@TableName("p_dict")
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典主键id
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    /**
     * 键
     */
    private Integer dicKey;

    /**
     * 值
     */
    private String dicValue;

    /**
     * 字段名字
     */
    private String fieldName;

    /**
     * 表名
     */
    private String tableName;
}
