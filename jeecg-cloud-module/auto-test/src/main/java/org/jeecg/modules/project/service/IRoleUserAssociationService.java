package org.jeecg.modules.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.project.entity.ProjectUserAssociation;
import org.jeecg.modules.project.entity.RoleUserAssociation;

import java.util.List;
import java.util.Map;

/**
 * @Description: role_user_association
 * @Author: jeecg-boot
 * @Date:   2021-04-01
 * @Version: V1.0
 */
public interface IRoleUserAssociationService extends IService<RoleUserAssociation> {
	/**
	 * 获取用户数据
	 * @param userList true
	 * @return 没有返回值
	 * */
	List<Map<String,Object>> getUserData(List<ProjectUserAssociation> userList);

	/**
	 * 获取角色数据
	 * @param roleList true
	 * @return 没有返回值
	 * */
	//List<Map<String,Object>> getRoleData (List<ProjectRoleAssociation> roleList);
	/**
	 * 获取角色数据
	 * @param projectId true
	 * @param userId true
	 * @return 没有返回值
	 * */
	List<RoleUserAssociation> getRoleUserInfo(String projectId, String userId);
	/**
	 * 获取角色数据
	 * @param projectId true
	 * @param userId true
	 * @return 没有返回值
	 * */
	void deleteRoleUserInfo(String projectId, String userId);
	
	/**
	 *根据项目ID、用户ID查询单条角色ID
	 * @param projectId true
	 * @param userId true
	 * @return roleId
	 */
	String getRoleIdByUserId(String projectId, String userId);
	
	/**
	 * 根据项目ID、用户ID查询角色ID集合
	 * @param projectId true
	 * @param userId true
	 * @return roleIdList
	 */
	List<String> getRoleIdsByProIdAndUserId(String projectId, String userId);
	/**
	 * 获取角色信息
	 * @param roleId true
	 * @return roleIdList
	 */
	List<Map<String,Object>> getRoleInfo(String roleId);
	/**
	 * 获取角色信息集合
	 * @param roleId true
	 * @return roleIdList
	 */
	Map<String,Object> getRoleListInfo(String roleId);
	
	/**
	 * 根据用户ID 删除用户角色关联表数据
	 * @param userId true
	 */
	void deleteInfoByUserId(String userId);
	
	/**
	 * 根据角色ID查询用户角色关联表的角色ID
	 * @param roleId true
	 * @return roleId
	 */
	String selectLeaderByRoleId(String roleId);
	
	/**
	 * 根据角色ID删除用户角色关联表数据
	 * @param leaderRoleId true
	 */
	void deleteInfoByRoleId(String leaderRoleId);
	
	/**
	 * 根据项目ID和角色ID删除相关数据
	 * @param projectId true
	 * @param roleId true
	 * @return 没有返回值
	 */
	int delByProAndRole(String projectId, String roleId);
	
	/**
	 * 根据项目及角色获取相关人员列表
	 * @param projectId true
	 * @param roleId true
	 * @return 返回集合
	 */
	List<String> getUserIdByProAndRole(String projectId, String roleId);

}
