package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningProjectTurnVersion;
import org.jeecg.modules.project.mapper.RunningProjectTurnVersionMapper;
import org.jeecg.modules.project.service.IRunningProjectTurnVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: running_project_turn
 * @Author: jeecg-boot
 * @Date:   2021-07-21
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningProjectTurnVersionServiceImpl extends ServiceImpl<RunningProjectTurnVersionMapper, RunningProjectTurnVersion> implements IRunningProjectTurnVersionService {

    @Autowired
    private RunningProjectTurnVersionMapper runningProjectTurnVersionMapper;

    @Override
    public void deleteByTurnId(String turnId) {
        this.getBaseMapper().deleteByTurnId(turnId);
    }

    @Override
    public  List<String> getProjectTurnVersionId(String turnId) {
        return runningProjectTurnVersionMapper.getProjectTurnVersionId(turnId);
    }
}
