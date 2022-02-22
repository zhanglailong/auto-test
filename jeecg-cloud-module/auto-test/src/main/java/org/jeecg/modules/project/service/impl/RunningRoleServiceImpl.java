package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningRole;
import org.jeecg.modules.project.entity.RunningRoleHistory;
import org.jeecg.modules.project.mapper.RoleUserAssociationMapper;
import org.jeecg.modules.project.mapper.RunningRoleMapper;
import org.jeecg.modules.project.service.IRunningRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 项目角色表
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningRoleServiceImpl extends ServiceImpl<RunningRoleMapper, RunningRole> implements IRunningRoleService {

	@Autowired
	private RunningRoleMapper runningRoleMapper;
	@Autowired
	RoleUserAssociationMapper roleUserAssociationMapper;
	
	
	@Override
	public Page<RunningRole> queryPageList(Page<RunningRole> page, String roleId, String createTime) {
		// TODO Auto-generated method stub
		return page.setRecords(runningRoleMapper.getListData(page, roleId,createTime));
	}
	
	@Override
	public List<RunningRole> getRoleData() {
		// TODO Auto-generated method stub
		return runningRoleMapper.getRoleData();
	}

	/**
	 *角色管理操作历史记录查询
	 */
	@Override
	public IPage<Map<String, Object>> getOperationHistoryList(Page page, RunningRoleHistory params) {
		// TODO Auto-generated method stub
		return runningRoleMapper.getOperationHistoryList(page,params);
	}

}
