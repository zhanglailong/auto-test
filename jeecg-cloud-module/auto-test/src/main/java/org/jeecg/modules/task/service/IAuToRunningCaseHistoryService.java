package org.jeecg.modules.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningCaseHistory;

public interface IAuToRunningCaseHistoryService extends IService<RunningCaseHistory> {
    Page<RunningCaseHistory> queryHistoryListData(Page page, RunningCaseHistory runningCaseHistory);
    /**
     * 更新插入
     * @param runningCase true
     * @param operationType true
     * @return 没有返回值
     * */
    boolean updateInsert(RunningCase runningCase, String operationType);
    /**
     * 更新插入
     * @param page true
     * @param runningCaseHistory true
     * @return 没有返回值
     * */
    //IPage<Map<String,Object>> getOperationHistoryList(Page page, RunningCaseHistory runningCaseHistory);

    /**
     * 更新插入
     * @param userIds true
     * @return 没有返回值
     * */
    String getUsernamesByIds(Object userIds);
}
