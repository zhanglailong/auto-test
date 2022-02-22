package org.jeecg.modules.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.task.entity.RunningTaskHistory;

import java.util.Map;

public interface AuToRunningTaskHistoryMapper extends BaseMapper<RunningTaskHistory> {
    /**
     * 获取历史记录列表
     * @param params true
     * @param page true
     * @return IPage<Map<String, Object>>
     * */
    IPage<Map<String, Object>> getOperationHistoryList(Page<RunningTaskHistory> page, @Param("params") RunningTaskHistory params);
}
