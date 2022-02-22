package org.jeecg.modules.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.task.entity.RunningCaseStep;

/**
 * @author test
 * */
public interface IRunningCaseStepService extends IService<RunningCaseStep> {

   int deleteByCaseId(String caseId);
}
