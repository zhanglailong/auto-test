package org.jeecg.modules.task.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.task.entity.RunningTaskHistory;
import org.jeecg.modules.task.mapper.AuToRunningTaskHistoryMapper;
import org.jeecg.modules.task.service.IAuToRunningTaskHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 任务管理历史记录
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Service
public class AuToRunningTaskHistoryServiceImpl extends ServiceImpl<AuToRunningTaskHistoryMapper, RunningTaskHistory> implements IAuToRunningTaskHistoryService {
    @Autowired
    private AuToRunningTaskHistoryMapper auToRunningTaskHistoryMapper;

    @Override
    public IPage<Map<String, Object>> getOperationHistoryList(Page<RunningTaskHistory> page,
                                                              RunningTaskHistory params) {
        IPage<Map<String, Object>> historyList = auToRunningTaskHistoryMapper.getOperationHistoryList(page, params);

        return historyList;
    }
}
