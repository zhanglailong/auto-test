package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningUutFileProject;
import org.jeecg.modules.project.mapper.RunningUutFileProjectMapper;
import org.jeecg.modules.project.service.IRunningUutFileProjectService;
import org.springframework.stereotype.Service;

@Service
@DS("jeecbootDatabase")
public class RunningUutFileProjectServiceImpl extends ServiceImpl<RunningUutFileProjectMapper, RunningUutFileProject> implements IRunningUutFileProjectService {

}
