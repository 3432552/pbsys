package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.domain.Dict;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wzh
 * @since 2020-10-14
 */
public interface DictService extends IService<Dict> {
    List<Dict> getDictListByPage(Map<String,Object> map);
    int addDic(Dict dict);

    int updateDic(Dict dict);
}
