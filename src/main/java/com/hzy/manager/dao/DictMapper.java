package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.Dict;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wzh
 * @since 2020-10-14
 */
public interface DictMapper extends BaseMapper<Dict> {
    List<Dict> getDictInfoByCondition(Map<String, Object> map);
}
