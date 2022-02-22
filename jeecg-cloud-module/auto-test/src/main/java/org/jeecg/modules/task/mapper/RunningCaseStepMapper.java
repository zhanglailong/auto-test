package org.jeecg.modules.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.task.entity.RunningCaseStep;

@Mapper
/**
 * @Author: test
 * */
public interface RunningCaseStepMapper extends BaseMapper<RunningCaseStep> {
    @Delete(
            "DELETE FROM running_case_step WHERE case_id = #{caseId}"
    )
    int deleteByCaseId(String caseId);
}
