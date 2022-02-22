package org.jeecg.modules.task.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.task.entity.RunningCaseStep;
import org.jeecg.modules.task.mapper.RunningCaseStepMapper;
import org.jeecg.modules.task.service.IRunningCaseStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 测试用例表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningCaseStepServiceImpl extends ServiceImpl<RunningCaseStepMapper, RunningCaseStep> implements IRunningCaseStepService {
@Autowired
RunningCaseStepMapper runningCaseStepMapper;

    @Override
    public int deleteByCaseId(String caseId) {

        return runningCaseStepMapper.deleteByCaseId(caseId);
    }
}
