package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.dao.DictMapper;
import com.hzy.manager.domain.Dict;
import com.hzy.manager.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wzh
 * @since 2020-10-14
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<Dict> getDictListByPage(Map<String, Object> map) {
        return dictMapper.getDictInfoByCondition(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addDic(Dict dict) {
        return dictMapper.insert(dict);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateDic(Dict dict) {
        return dictMapper.updateById(dict);
    }
}
