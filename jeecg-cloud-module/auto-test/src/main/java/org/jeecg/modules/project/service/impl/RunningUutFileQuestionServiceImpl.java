package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningUutFileQuestion;
import org.jeecg.modules.project.mapper.RunningUutFileQuestionMapper;
import org.jeecg.modules.project.service.IRunningUutFileQuestionService;
import org.springframework.stereotype.Service;

@Service
@DS("jeecbootDatabase")
public class RunningUutFileQuestionServiceImpl extends ServiceImpl<RunningUutFileQuestionMapper, RunningUutFileQuestion> implements IRunningUutFileQuestionService {

}
