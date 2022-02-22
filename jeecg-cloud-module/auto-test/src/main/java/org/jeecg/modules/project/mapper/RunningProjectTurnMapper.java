package org.jeecg.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.project.entity.RunningProjectTurn;

import java.util.List;
import java.util.Map;

@Mapper
public interface RunningProjectTurnMapper extends BaseMapper<RunningProjectTurn> {
    /**
     * 获取信息列表
     * @param projectId true
     * @return 没有返回值
     * */
    List<RunningProjectTurn> getTurn(String projectId);
    /**
     * 获取信息列表
     * @param uutId true
     * @return 没有返回值
     * */
    @Select("SELECT id AS value, version AS text " +
            "FROM running_uut_version " +
            "WHERE uut_list_id = #{uutId}")
    List<Map<String, String>> getAllVersion(String uutId);

    @Select("DELETE FROM running_project_turn WHERE project_id = #{projectId}")
    void deleteByProjectId(String projectId);

    @Select("SELECT id FROM running_project_turn WHERE project_id = #{projectId}")
    List<String> getIdsByProjectId(String projectId);

    // 下拉选层级列表
    List<DictModel> getOptionByCondition(String projectId, String turnId);

    // 下拉选层级列表
    List<DictModel> getOptionByConditionByTaskId(String taskId, String turnId);

    // 下拉选层级列表
    List<DictModel> getUutNameByStatus();

    @Select("SELECT * FROM running_project_turn WHERE project_id = #{id}")
    List<RunningProjectTurn> getRunningProjectTurnList(String id);
}
