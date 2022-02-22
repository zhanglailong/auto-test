package org.jeecg.modules.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningProjectTurnVersion;
import org.jeecg.modules.project.mapper.AuToRunningProjectTurnVersionMapper;
import org.jeecg.modules.project.service.IAuToRunningProjectTurnVersionService;
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
public class AuToRunningProjectTurnVersionServiceImpl extends ServiceImpl<AuToRunningProjectTurnVersionMapper, RunningProjectTurnVersion> implements IAuToRunningProjectTurnVersionService {
    @Autowired
    private AuToRunningProjectTurnVersionMapper auToRunningProjectTurnVersionMapper;

    @Override
    public void deleteByTurnId(String turnId) {
        this.getBaseMapper().deleteByTurnId(turnId);
    }

    @Override
    public List<String> getProjectTurnVersionId(String turnId) {
        return auToRunningProjectTurnVersionMapper.getProjectTurnVersionId(turnId);
    }
}
