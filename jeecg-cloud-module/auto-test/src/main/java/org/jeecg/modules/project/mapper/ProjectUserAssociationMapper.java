package org.jeecg.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.project.entity.ProjectUserAssociation;

import java.util.List;

/**
 * @Description: 项目用户关联
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
public interface ProjectUserAssociationMapper extends BaseMapper<ProjectUserAssociation> {
	
	@Select(" SELECT id,project_id as projectId, \n "
			+ " project_member_ids as projectMemberIds \n"
			+ " FROM project_user_association"
			+ " WHERE project_id=#{projectId} ")
	/**
	 * 获取任务id信息
	 * @param projectId true
	 * @return list集合
	 * */
	List<ProjectUserAssociation> getInfoByProjectId(String projectId);
	
	@Delete(" delete from project_user_association where project_id=#{projectId} ")
	/**
	 * 删除id信息
	 * @param projectId true
	 * */
	void deleteDataByProjectId(String projectId);
	
	@Select(" select * from project_user_association where user_id=#{userId} ")
	/**
	 * 获取用户id
	 * @param userId true
	 * @return list集合
	 * */
	List<ProjectUserAssociation> getInfoByUserId(String userId);
	
	@Delete(" delete from project_user_association where user_id=#{userId} ")
	/**
	 * 通过用户id删除信息
	 * @param userId true
	 * */
	void deleteInfoByUserId(String userId);
	
	@Select(" select project_member_ids from project_user_association where project_id = #{projectId} ")
	/**
	 * 通过任务id获取用户信息
	 * @param projectId true
	 * @return list集合
	 * */
	List<String> getUserIdsByProjectId(@Param("id") String projectId);
	
}
