package org.jeecg.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.project.entity.RunningProjectTurnVersion;

import java.util.List;

@Mapper
public interface AuToRunningProjectTurnVersionMapper extends BaseMapper<RunningProjectTurnVersion> {
    @Select("DELETE FROM running_project_turn_version WHERE turn_id = #{turnId}")
    void deleteByTurnId(String turnId);

    @Select("SELECT b.version_id FROM running_project_turn a,running_project_turn_version b WHERE a.id = b.turn_id AND a.id = #{turnId}")
    List<String> getProjectTurnVersionId(String turnId);
}
