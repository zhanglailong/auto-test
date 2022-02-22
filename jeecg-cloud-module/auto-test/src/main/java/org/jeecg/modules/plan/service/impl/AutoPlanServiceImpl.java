package org.jeecg.modules.plan.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.plan.entity.AutoPlan;
import org.jeecg.modules.plan.mapper.AutoPlanMapper;
import org.jeecg.modules.plan.service.IAutoPlanService;
import org.jeecg.modules.scriptandplan.entity.AutoScriptPlan;
import org.jeecg.modules.scriptandplan.mapper.AutoScriptPlanMapper;
import org.jeecg.modules.scriptandplan.service.IAutoScriptPlanService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 方案管理
 * @Author: zll
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Service
public class AutoPlanServiceImpl extends ServiceImpl<AutoPlanMapper, AutoPlan> implements IAutoPlanService {

    @Resource
    AutoPlanMapper autoPlanMapper;
    @Resource
    IAutoScriptPlanService autoScriptPlanService;
    @Resource
    AutoScriptPlanMapper autoScriptPlanMapper;
    @Override
    public void delete(String id) {
        //删除方案
        AutoPlan autoPlan = autoPlanMapper.selectById(id);
        autoPlan.setIdel(CommonConstant.DATA_INT_IDEL_1);
        int flag = autoPlanMapper.updateById(autoPlan);
        if (flag == CommonConstant.DATA_INT_0) {
            throw new UnsupportedOperationException("方案删除失败!");
        }
        //删除方案下的脚本
        QueryWrapper<AutoScriptPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CommonConstant.DATA_STRING_PLANID,id);
        List<AutoScriptPlan> list = autoScriptPlanService.list(queryWrapper);
        if(CollectionUtils.isNotEmpty(list)){
            for (AutoScriptPlan autoScriptPlan : list) {
                autoScriptPlan.setIdel(CommonConstant.DATA_INT_IDEL_1);
                autoScriptPlanMapper.updateById(autoScriptPlan);
            }
        }
    }

    @Override
    public void edit(AutoPlan autoPlan) {
        int flag = autoPlanMapper.updateById(autoPlan);
        if (flag == CommonConstant.DATA_INT_0) {
            throw new UnsupportedOperationException("修改失败!");
        }
    }
}
