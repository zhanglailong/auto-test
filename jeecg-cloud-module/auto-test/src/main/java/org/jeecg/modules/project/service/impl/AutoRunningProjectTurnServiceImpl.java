package org.jeecg.modules.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningProjectTurn;
import org.jeecg.modules.project.mapper.AuToRunningProjectTurnMapper;
import org.jeecg.modules.project.service.IAuToRunningProjectTurnService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: running_project_turn
 * @Author: jeecg-boot
 * @Date:   2021-11-17
 * @Version: V1.0
 */
@Service
public class AutoRunningProjectTurnServiceImpl extends ServiceImpl<AuToRunningProjectTurnMapper, RunningProjectTurn> implements IAuToRunningProjectTurnService {
    @Override
    public void deleteByProjectId(String projectId) {
        this.getBaseMapper().deleteByProjectId(projectId);
    }

    @Override
    public List<String> getIdsByProjectId(String projectId) {
        return this.getBaseMapper().getIdsByProjectId(projectId);
    }
}
