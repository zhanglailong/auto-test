package org.jeecg.modules.task.service.impl;



import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.task.entity.RunningCaseStepHistory;
import org.jeecg.modules.task.mapper.RunningCaseStepHistoryMapper;
import org.jeecg.modules.task.service.IRunningCaseStepHistoryService;
import org.springframework.stereotype.Service;

@Service
@DS("jeecbootDatabase")
public class RunningCaseStepHistoryServiceImpl extends ServiceImpl<RunningCaseStepHistoryMapper, RunningCaseStepHistory> implements IRunningCaseStepHistoryService {

}
