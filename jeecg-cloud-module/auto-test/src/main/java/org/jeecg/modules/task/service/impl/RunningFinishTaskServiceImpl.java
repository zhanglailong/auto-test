package org.jeecg.modules.task.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.task.entity.RunningFinishTask;
import org.jeecg.modules.task.mapper.RunningFinishTaskMapper;
import org.jeecg.modules.task.service.IRunningFinishTaskService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 完成任务表
 * @Author: jeecg-boot
 * @Date:   2021-02-22
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningFinishTaskServiceImpl extends ServiceImpl<RunningFinishTaskMapper, RunningFinishTask> implements IRunningFinishTaskService {

}
