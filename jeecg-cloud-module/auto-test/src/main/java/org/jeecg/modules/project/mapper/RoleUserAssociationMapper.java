package org.jeecg.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.project.entity.RoleUserAssociation;

import java.util.List;

/**
 * @Description: role_user_association
 * @Author: jeecg-boot
 * @Date:   2021-04-01
 * @Version: V1.0
 */
public interface RoleUserAssociationMapper extends BaseMapper<RoleUserAssociation> {
	
	
	@Select(" select * from role_user_association where project_id=#{projectId} \n"
			+ " and user_id=#{userId} ")
	/**
	 * 获取角色信息
	 * @param projectId true
	 * @param userId true
	 * @return list集合
	 * */
	List<RoleUserAssociation> getRoleUserInfo(String projectId, String userId);
	
	@Delete(" delete from role_user_association where project_id=#{projectId} \n"
			+ " and user_id=#{userId} ")
	/**
	 * 删除角色信息
	 * @param projectId true
	 * @param userId true
	 * */
	void deleteRoleUserInfo(String projectId, String userId);
	
	@Select(" select role_id from role_user_association where project_id=#{projectId}\n"
			+ " and user_id=#{userId}")
	/**
	 * 通过用户id获取角色Id
	 * @param projectId true
	 * @param userId true
	 * @return 没有返回
	 * */
	String getRoleIdByUserId(String projectId, String userId);
	
	@Select(" select role_id from role_user_association where project_id=#{projectId}\n"
			+ " and user_id=#{userId}")
	/**
	 * 通过用户id获取角色Id
	 * @param projectId true
	 * @param userId true
	 * @return list集合
	 * */
	List<String> getRoleIdsByProIdAndUserId(String projectId, String userId);
	
	@Select(" select * from role_user_association where role_id=#{id} ")
	/**
	 * 通过角色id获取角色信息
	 * @param id true
	 * @return list集合
	 * */
	List<RoleUserAssociation> getRoleUserInfoByRoleId(String id);
	
	@Delete(" delete from role_user_association where role_id=#{id} ")
	/**
	 * 通过角色id删除
	 * @param id true
	 * */
	void deleteByRoleId(String id);
	
	@Delete(" delete from role_user_association where user_id=#{userId} ")
	/**
	 * 通过用户id删除
	 * @param userId true
	 * */
	void deleteInfoByUserId(String userId);
	
	
	@Select(" select * from role_user_association where user_id=#{userId} ")
	 /**
	  * 通过用户id获取信息
	  * @param userId true
	  * @return  List<RoleUserAssociation>
	  */
	List<RoleUserAssociation> getInfoByUserId(String userId);
	
	@Select(" select role_id from role_user_association where role_id=#{roleId} ")
	/**
	 * 通过角色id查询
	 * @param roleId true
	 * @return  selectLeaderByRoleId
	 */
	String selectLeaderByRoleId(String roleId);

	@Delete(" delete from role_user_association where role_id=#{leaderRoleId} ")
	/**
	 * 通过角色id删除信息
	 * @param leaderRoleId true
	 * */
	void deleteInfoByRoleId(String leaderRoleId);
	
	@Delete(" delete from role_user_association where project_id=#{projectId} and role_id=#{roleId} ")
	/**
	 * 通过角色id删除信息
	 * @param projectId true
	 * @param roleId true
	 * @return int类型
	 * */
	int delByProAndRole(String projectId, String roleId);
	
	@Select(" select user_id from role_user_association where project_id=#{projectId} and role_id=#{roleId} ")
	/**
	 * 获取用户id
	 * @param projectId true
	 * @param roleId true
	 * @return list集合
	 * */
	List<String> getUserIdByProAndRole(String projectId, String roleId);
}
