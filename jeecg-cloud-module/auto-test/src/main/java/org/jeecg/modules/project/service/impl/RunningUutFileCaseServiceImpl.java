package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningUutFileCase;
import org.jeecg.modules.project.mapper.RunningUutFileCaseMapper;
import org.jeecg.modules.project.service.IRunningUutFileCaseService;
import org.springframework.stereotype.Service;

@Service
@DS("jeecbootDatabase")
public class RunningUutFileCaseServiceImpl extends ServiceImpl<RunningUutFileCaseMapper, RunningUutFileCase>  implements IRunningUutFileCaseService {

}
