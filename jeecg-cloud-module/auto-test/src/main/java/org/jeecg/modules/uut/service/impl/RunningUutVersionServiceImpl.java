package org.jeecg.modules.uut.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.uut.entity.RunningUutVersion;
import org.jeecg.modules.uut.mapper.RunningUutVersionMapper;
import org.jeecg.modules.uut.service.IRunningUutVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("uutDatabase")
public class RunningUutVersionServiceImpl extends ServiceImpl<RunningUutVersionMapper, RunningUutVersion> implements IRunningUutVersionService {

    @Autowired
    private RunningUutVersionMapper runningUutVersionMapper;

    @Override
    public String getProjectTurnVersion(String versionId) {
        return runningUutVersionMapper.getProjectTurnVersion(versionId);
    }

}
