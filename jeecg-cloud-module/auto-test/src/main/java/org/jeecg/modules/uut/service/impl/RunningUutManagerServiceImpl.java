package org.jeecg.modules.uut.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.uut.entity.RunningUutManager;
import org.jeecg.modules.uut.mapper.RunningUutManagerMapper;
import org.jeecg.modules.uut.service.IRunningUutManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 被测对象流程任务表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutManagerServiceImpl extends ServiceImpl<RunningUutManagerMapper, RunningUutManager> implements IRunningUutManagerService {
	@Resource
	private RunningUutManagerMapper runningUutManagerMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public RunningUutManager getStartInstance(String instanceId) {
		return runningUutManagerMapper.getStartInstance(instanceId);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public RunningUutManager getCurrentInstance(String instanceId) {
		return runningUutManagerMapper.getCurrentInstance(instanceId);
	}

	@Override
	@Transactional
	public List<RunningUutManager> getList(String instanceId) {
		return runningUutManagerMapper.getList(instanceId);
	}

	@Override
	public Page<RunningUutManager> queryPageList(Page<RunningUutManager> page, Map<String, Object> map) {
		return page.setRecords(runningUutManagerMapper.queryPageList(map));
	}

	@Override
	public Page<RunningUutManager> getPageList(Page<RunningUutManager> page, Map<String, Object> map) {
		return page.setRecords(runningUutManagerMapper.getPageList(map));
	}
}
