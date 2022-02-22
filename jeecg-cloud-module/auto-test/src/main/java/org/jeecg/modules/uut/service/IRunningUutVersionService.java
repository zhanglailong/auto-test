package org.jeecg.modules.uut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.uut.entity.RunningUutVersion;

import java.util.List;

public interface IRunningUutVersionService extends IService<RunningUutVersion> {

    String getProjectTurnVersion(String versionId);

}
