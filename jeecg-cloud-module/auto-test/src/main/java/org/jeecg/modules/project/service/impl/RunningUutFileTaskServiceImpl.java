package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningUutFileTask;
import org.jeecg.modules.project.mapper.RunningUutFileTaskMapper;
import org.jeecg.modules.project.service.IRunningUutFileTaskService;
import org.springframework.stereotype.Service;

@Service
@DS("jeecbootDatabase")
public class RunningUutFileTaskServiceImpl extends ServiceImpl<RunningUutFileTaskMapper, RunningUutFileTask> implements IRunningUutFileTaskService {

}
