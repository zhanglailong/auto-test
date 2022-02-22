package org.jeecg.modules.scriptandplan.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.mapper.AutoScriptMapper;
import org.jeecg.modules.scriptandplan.entity.AutoScriptPlan;
import org.jeecg.modules.scriptandplan.mapper.AutoScriptPlanMapper;
import org.jeecg.modules.scriptandplan.service.IAutoScriptPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
import java.util.*;

/**
 * @Description: 方案脚本对应表
 * @Author: jeecg-boot
 * @Date: 2021-08-18
 * @Version: V1.0
 */
@Service
public class AutoScriptPlanServiceImpl extends ServiceImpl<AutoScriptPlanMapper, AutoScriptPlan> implements IAutoScriptPlanService {

    @Resource
    private AutoScriptPlanMapper autoScriptPlanMapper;
    @Autowired
    private IAutoScriptPlanService autoScriptPlanService;
    @Resource
    private AutoScriptMapper autoScriptMapper;

    @Override
    public boolean bindingScript(String testCaseId, String scriptIds) {
        try {
            List<String> ids = Arrays.asList(scriptIds.split(","));
                for (String id : ids) {
                    QueryWrapper<AutoScriptPlan> queryWrapper = new QueryWrapper<AutoScriptPlan>();
                    queryWrapper.eq(CommonConstant.DATA_STRING_OLDSCRIPTID,id);
                    queryWrapper.eq(CommonConstant.DATA_STRING_TEST_CASE_ID,testCaseId);
                    queryWrapper.eq(CommonConstant.DATA_STRING_IDEL,CommonConstant.DATA_INT_IDEL_0);
                    AutoScriptPlan autoScriptPlan = autoScriptPlanMapper.selectOne(queryWrapper);
                    if(Objects.isNull(autoScriptPlan)){
                        ArrayList<String> newArr = new ArrayList<>();
                        newArr.add(id);
                        addScript(newArr,testCaseId);
                    }
                }
            return true;
        } catch (Exception e) {
            log.error("方案绑定脚本失败 " + e.getMessage());
            return false;
        }
    }

    @Override
    public void delete(String id) {
        AutoScriptPlan autoScriptPlan = autoScriptPlanMapper.selectById(id);
        autoScriptPlan.setIdel(CommonConstant.DATA_INT_IDEL_1);
        autoScriptPlanMapper.updateById(autoScriptPlan);
    }

    @Override
    public void scriptUp(AutoScriptPlan autoScriptPlan) {
        try {
            //当前对象
            AutoScriptPlan aspNow = autoScriptPlanMapper.selectOne(new QueryWrapper<AutoScriptPlan>()
                    .eq(CommonConstant.DATA_STRING_SORT, autoScriptPlan.getSort())
                    .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                    .eq(CommonConstant.DATA_STRING_TEST_CASE_ID,autoScriptPlan.getTestCaseId()));
            //前一个对象
            AutoScriptPlan previousAsp = autoScriptPlanMapper.aspMoveUp(aspNow);
            //最上面不能上移
            if(Objects.isNull(previousAsp)){
                return ;
            }
            //交换位置
            Integer tepm = aspNow.getSort();
            aspNow.setSort(previousAsp.getSort());
            previousAsp.setSort(tepm);
            autoScriptPlanMapper.updateById(aspNow);
            autoScriptPlanMapper.updateById(previousAsp);
        } catch (Exception e) {
            log.error("脚本上移失败,原因是" + e.getMessage());
        }
    }

    @Override
    public void scriptdown(AutoScriptPlan autoScriptPlan) {
        try {
            //当前对象
            AutoScriptPlan aspNow = autoScriptPlanMapper.selectOne(new QueryWrapper<AutoScriptPlan>()
                    .eq(CommonConstant.DATA_STRING_SORT, autoScriptPlan.getSort())
                    .eq(CommonConstant.DATA_STRING_IDEL,CommonConstant.DATA_INT_IDEL_0)
                    .eq(CommonConstant.DATA_STRING_TEST_CASE_ID,autoScriptPlan.getTestCaseId()));
            //后一个对象
            AutoScriptPlan behindAsp = autoScriptPlanMapper.aspMoveDown(aspNow);
            //最下面不能下移
            if(Objects.isNull(behindAsp)){
                return;
            }
            //交换位置
            Integer tepm = aspNow.getSort();
            aspNow.setSort(behindAsp.getSort());
            behindAsp.setSort(tepm);
            autoScriptPlanMapper.updateById(aspNow);
            autoScriptPlanMapper.updateById(behindAsp);
        } catch (Exception e) {
            log.error("脚本下移失败,原因是" + e.getMessage());
        }
    }

    /**
     * 添加方案下绑定的脚本
     */
    private void addScript(List<String> ids, String testCaseId) {
        Integer sort;
        List<AutoScript> autoScripts = autoScriptMapper.selectBatchIds(ids);
        //查询当前方案下绑定的脚本列表
        List<AutoScriptPlan> scriptPlanList = autoScriptPlanService.list(new QueryWrapper<AutoScriptPlan>()
                .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                .eq(CommonConstant.DATA_STRING_TEST_CASE_ID,testCaseId));
        ArrayList<Integer> sortArr = new ArrayList<>();
        if (scriptPlanList == null || scriptPlanList.size() == 0) {
             sort=CommonConstant.DATA_INT_IDEL_0;
        }else{
            scriptPlanList.forEach(s -> {
                sortArr.add(s.getSort());
            });
             sort = sortArr.get(sortArr.size() - 1);
        }
        Collections.sort(sortArr);
        for (AutoScript asp : autoScripts) {
            AutoScriptPlan autoScriptPlanOne = new AutoScriptPlan();
            BeanUtils.copyProperties(asp, autoScriptPlanOne);
            autoScriptPlanOne.setTestCaseId(testCaseId);
            autoScriptPlanOne.setId(null);
            autoScriptPlanOne.setOldScriptId(asp.getId());
            autoScriptPlanOne.setRecordNodeId(asp.getRecordNodeId());
            autoScriptPlanOne.setSort(sort+1);
            autoScriptPlanService.save(autoScriptPlanOne);
        }
    }
}
