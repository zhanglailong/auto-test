package org.jeecg.modules.uut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.uut.entity.RunningUutList;
import org.jeecg.modules.uut.mapper.AuToRunningUutListMapper;
import org.jeecg.modules.uut.service.IAuToRunningUutListService;
import org.jeecg.modules.uut.vo.RunningUutListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Description: 被测对象列表
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Service
public class AuToRunningUutListServiceImpl extends ServiceImpl<AuToRunningUutListMapper, RunningUutList> implements IAuToRunningUutListService {
    @Autowired
    private AuToRunningUutListMapper runningUutListMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RunningUutList findUniqueBy(String fieldname, String value) {
        return this.getBaseMapper().findUniqueBy(fieldname, value);
    }

    @Override
    public RunningUutListVo findUniqueVoBy(String fieldname, String value) {
        return this.getBaseMapper().findUniqueVoBy(fieldname, value);
    }

    /**
     * 通过id查询被测对象最高版本
     * @param id
     * @return String
     */
    @Override
    public String queryUutVersionById(String id){
        return runningUutListMapper.queryUutVersionById(id);
    };
}
