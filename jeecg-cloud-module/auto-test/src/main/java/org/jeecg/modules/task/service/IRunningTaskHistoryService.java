package org.jeecg.modules.task.service;

import java.util.Map;

import org.jeecg.modules.task.entity.RunningTaskHistory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 任务管理历史记录
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
public interface IRunningTaskHistoryService extends IService<RunningTaskHistory> {
	/**
	 * 获取历史记录列表
	 * @param page true
	 * @param params true
	 * @return 没有返回值
	 * */
	IPage<Map<String,Object>> getOperationHistoryList(Page<RunningTaskHistory> page, RunningTaskHistory params);

}
