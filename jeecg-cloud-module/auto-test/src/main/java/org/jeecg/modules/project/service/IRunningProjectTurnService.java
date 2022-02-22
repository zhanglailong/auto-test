package org.jeecg.modules.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.project.entity.RunningProjectTurn;

import java.util.List;

/**
 * @Description: running_project_turn
 * @Author: jeecg-boot
 * @Date:   2021-07-21
 * @Version: V1.0
 */
@Mapper
public interface IRunningProjectTurnService extends IService<RunningProjectTurn> {
   void deleteByProjectId(String projectId);
   List<String> getIdsByProjectId(String projectId);
   List<RunningProjectTurn> getRunningProjectTurnList(String id);
}
