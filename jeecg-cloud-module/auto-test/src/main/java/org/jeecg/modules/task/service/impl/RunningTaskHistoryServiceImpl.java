package org.jeecg.modules.task.service.impl;

import java.util.Map;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.task.entity.RunningTaskHistory;
import org.jeecg.modules.task.mapper.RunningTaskHistoryMapper;
import org.jeecg.modules.task.service.IRunningTaskHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 任务管理历史记录
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningTaskHistoryServiceImpl extends ServiceImpl<RunningTaskHistoryMapper, RunningTaskHistory> implements IRunningTaskHistoryService {

	@Autowired
	private RunningTaskHistoryMapper runningTaskHistoryMapper;
	
	@Override
	public IPage<Map<String, Object>> getOperationHistoryList(Page<RunningTaskHistory> page,
			RunningTaskHistory params) {
		IPage<Map<String, Object>> historyList = runningTaskHistoryMapper.getOperationHistoryList(page, params);
		
		return historyList;
	}

}
