package org.jeecg.modules.task.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.task.entity.RunningTaskHistory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 任务管理历史记录
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
public interface RunningTaskHistoryMapper extends BaseMapper<RunningTaskHistory> {
	/**
	 * 获取历史记录列表
	 * @param params true
	 * @param page true
	 * @return IPage<Map<String, Object>>
	 * */
	IPage<Map<String, Object>> getOperationHistoryList(Page<RunningTaskHistory> page, @Param("params") RunningTaskHistory params);

}
