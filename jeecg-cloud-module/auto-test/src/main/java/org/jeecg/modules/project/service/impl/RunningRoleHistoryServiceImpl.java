package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningRoleHistory;
import org.jeecg.modules.project.mapper.RunningRoleHistoryMapper;
import org.jeecg.modules.project.service.IRunningRoleHistoryService;
import org.springframework.stereotype.Service;

/**
 * @Description:角色管理服务实现类
 * @Author: jeecg-boot
 * @Date:   2021-04-21
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningRoleHistoryServiceImpl extends ServiceImpl<RunningRoleHistoryMapper, RunningRoleHistory> implements IRunningRoleHistoryService
{
	
}
