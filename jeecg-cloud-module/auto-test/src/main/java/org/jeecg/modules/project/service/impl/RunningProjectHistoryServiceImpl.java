package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningProjectHistory;
import org.jeecg.modules.project.mapper.RunningProjectHistoryMapper;
import org.jeecg.modules.project.service.IRunningProjectHistoryService;
import org.springframework.stereotype.Service;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningProjectHistoryServiceImpl extends ServiceImpl<RunningProjectHistoryMapper, RunningProjectHistory> implements IRunningProjectHistoryService
{
	
}
