package org.jeecg.modules.task.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.task.entity.RunningCaseHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description: running_case_history
 * @Author: jeecg-boot
 * @Date:   2021-03-24
 * @Version: V1.0
 */
@Mapper
public interface RunningCaseHistoryMapper extends BaseMapper<RunningCaseHistory> {

    //IPage<Map<String,Object>> getOperationHistoryList(Page page, @Param("params") RunningCaseHistory params);
    /**
     * 获取历史列表
     * @param params true
     * @return IPage集合
     * */
    List<RunningCaseHistory> getRunningCaseHistoryList(@Param("params") RunningCaseHistory params);

}
