package org.jeecg.modules.autoResult.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.autoResult.entity.AutoResult;
import org.jeecg.modules.autoResult.mapper.AutoResultMapper;
import org.jeecg.modules.autoResult.service.IAutoResultService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.nodeserve.entity.NodeStartScriptPara;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.jeecg.modules.scriptandplan.entity.AutoScriptPlan;
import org.jeecg.modules.scriptandplan.service.IAutoScriptPlanService;
import org.jeecg.modules.scriptrecord.entity.RunScriptRecord;
import org.jeecg.modules.scriptrecord.service.IRunScriptRecordService;
import org.jeecg.modules.task.service.IAuToRunningCaseService;
import org.jeecg.modules.task.service.IRunningCaseService;
import org.jeecg.modules.task.vo.RunningCaseVO;
import org.nfunk.jep.function.Str;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description: 测试结果管理
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Service
@Slf4j
public class AutoResultServiceImpl extends ServiceImpl<AutoResultMapper, AutoResult> implements IAutoResultService {

    @Resource
    private IAutoResultService autoResultService;

    @Resource
    private IRunScriptRecordService runScriptRecordService;

    @Resource
    private IAuToRunningCaseService auToRunningCaseService;
    @Resource
    private IAutoScriptPlanService autoScriptPlanService;
    @Resource
    private IAutoScriptService autoScriptService;

    @Override
    public String addRecord(String id,Integer type) {
        if(type.equals(CommonConstant.DATA_INT_1)){
            try {
                AutoScript autoScript = autoScriptService.getById(id);
                RunningCaseVO runningCaseVO = auToRunningCaseService.getRunningCaseVoByCaseId(autoScript.getTestCaseId());
                //获取测试用例信息
                RunScriptRecord runScriptRecord = new RunScriptRecord();
                runScriptRecord.setProjectId(runningCaseVO.getProjectId());
                runScriptRecord.setProjectName(runningCaseVO.getProjectName());
                runScriptRecord.setAutoScriptId(id);
                runScriptRecord.setIdel(CommonConstant.DATA_INT_0);
                runScriptRecord.setTestCaseId(runningCaseVO.getId());
                runScriptRecord.setTestCaseName(runningCaseVO.getTestName());
                runScriptRecord.setTestItemName(runningCaseVO.getTestTaskName());
                runScriptRecord.setTestItemId(runningCaseVO.getTestTaskId());
                runScriptRecord.setStartTime(new Date());
                runScriptRecordService.saveOrUpdate(runScriptRecord);
                return runScriptRecord.getId();
            } catch (Exception e) {
               log.info("运行脚本记录添加失败"+e.getMessage());
               return "";
            }
        }
        if(type.equals(CommonConstant.DATA_INT_0)){
            try {
                RunningCaseVO runningCaseVO = auToRunningCaseService.getRunningCaseVoByCaseId(id);
                AutoResult autoResult = new AutoResult();
                autoResult.setProjectId(runningCaseVO.getProjectId());
                autoResult.setTestItemName(runningCaseVO.getTestTaskName());
                autoResult.setProjectName(runningCaseVO.getProjectName());
                autoResult.setIdel(CommonConstant.DATA_INT_0);
                autoResult.setTestCaseId(runningCaseVO.getId());
                autoResult.setTestCaseName(runningCaseVO.getTestName());
                autoResult.setTestItemId(runningCaseVO.getTestTaskId());
                autoResult.setStartTime(new Date());
                autoResultService.save(autoResult);
                return autoResult.getId();
            } catch (Exception e) {
                log.info("回放用例记录添加失败"+e.getMessage());
                return "";
            }
        }
        return null;
    }

}
