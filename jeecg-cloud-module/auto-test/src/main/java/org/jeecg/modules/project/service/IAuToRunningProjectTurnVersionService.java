package org.jeecg.modules.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.project.entity.RunningProjectTurnVersion;

import java.util.List;

/**
 * @Description: running_project_turn
 * @Author: jeecg-boot
 * @Date:   2021-11-17
 * @Version: V1.0
 */
public interface IAuToRunningProjectTurnVersionService extends IService<RunningProjectTurnVersion> {
    void deleteByTurnId(String turnId);

    List<String> getProjectTurnVersionId(String turnId);
}
