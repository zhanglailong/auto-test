package org.jeecg.modules.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.task.entity.RunningTaskHistory;

import java.util.Map;

public interface IAuToRunningTaskHistoryService extends IService<RunningTaskHistory> {
    /**
     * 获取历史记录列表
     * @param page true
     * @param params true
     * @return 没有返回值
     * */
    IPage<Map<String,Object>> getOperationHistoryList(Page<RunningTaskHistory> page, RunningTaskHistory params);
}
