package org.jeecg.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.task.entity.RunningCaseStepHistory;
import org.jeecg.modules.task.mapper.AuToRunningCaseStepHistoryMapper;
import org.jeecg.modules.task.service.IAuToRunningCaseStepHistoryService;
import org.springframework.stereotype.Service;

@Service
public class AuToRunningCaseStepHistoryServiceImpl extends ServiceImpl<AuToRunningCaseStepHistoryMapper, RunningCaseStepHistory> implements IAuToRunningCaseStepHistoryService {
}
