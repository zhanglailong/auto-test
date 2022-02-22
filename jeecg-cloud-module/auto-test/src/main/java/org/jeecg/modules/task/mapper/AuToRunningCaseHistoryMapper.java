package org.jeecg.modules.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.task.entity.RunningCaseHistory;

import java.util.List;

@Mapper
public interface AuToRunningCaseHistoryMapper extends BaseMapper<RunningCaseHistory> {
    /**
     * 获取历史列表
     * @param params true
     * @return IPage集合
     * */
    List<RunningCaseHistory> getRunningCaseHistoryList(@Param("params") RunningCaseHistory params);
}
