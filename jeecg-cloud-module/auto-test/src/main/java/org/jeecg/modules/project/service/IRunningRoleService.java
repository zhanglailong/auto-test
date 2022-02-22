package org.jeecg.modules.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.project.entity.RunningRole;
import org.jeecg.modules.project.entity.RunningRoleHistory;

import java.util.List;
import java.util.Map;

/**
 * @Description: 项目角色表
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
public interface IRunningRoleService extends IService<RunningRole> {
	
	/**
	 * 分页查询项目角色列表
	 * @param page
	 * @param roleId
	 * @param createTime
	 * @return
	 */
	Page<RunningRole> queryPageList(Page<RunningRole> page, String roleId, String createTime);
	
	/**
	 * 查询角色基础信息
	 * @return
	 */
	List<RunningRole> getRoleData();
	
	/**
	 * 删除角色，删除项目角色关联表拥有该角色的数据，删除角色用户表中拥有该角色的数据
	 * @param id
	 */
	//void removeRoleInfo(String id);
	
	/**
	 * 角色管理操作历史记录查询
	 * @param page true
	 * @param params true
	 * @return 没有返回值
	 */
	IPage<Map<String,Object>> getOperationHistoryList(Page page, RunningRoleHistory params);
	
	
	
}
