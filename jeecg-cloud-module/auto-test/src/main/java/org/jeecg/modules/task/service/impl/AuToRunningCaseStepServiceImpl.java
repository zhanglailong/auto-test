package org.jeecg.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.task.entity.RunningCaseStep;
import org.jeecg.modules.task.mapper.AuToRunningCaseStepMapper;
import org.jeecg.modules.task.mapper.RunningCaseStepMapper;
import org.jeecg.modules.task.service.IAuToRunningCaseStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuToRunningCaseStepServiceImpl extends ServiceImpl<AuToRunningCaseStepMapper, RunningCaseStep> implements IAuToRunningCaseStepService {
    @Autowired
    AuToRunningCaseStepMapper auToRunningCaseStepMapper;

    @Override
    public int deleteByCaseId(String caseId) {

        return auToRunningCaseStepMapper.deleteByCaseId(caseId);
    }
}
