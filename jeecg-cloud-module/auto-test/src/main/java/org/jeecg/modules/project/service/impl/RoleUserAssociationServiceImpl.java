package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.ProjectUserAssociation;
import org.jeecg.modules.project.entity.RoleUserAssociation;
import org.jeecg.modules.project.mapper.RoleUserAssociationMapper;
import org.jeecg.modules.project.mapper.RunningRoleMapper;
import org.jeecg.modules.project.service.IRoleUserAssociationService;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: role_user_association
 * @Author: jeecg-boot
 * @Date:   2021-04-01
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RoleUserAssociationServiceImpl extends ServiceImpl<RoleUserAssociationMapper, RoleUserAssociation> implements IRoleUserAssociationService {
	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private RunningRoleMapper runningRoleMapper;
	@Autowired
	RoleUserAssociationMapper roleUserAssociationMapper;
	
	
	@Override
	public List<Map<String, Object>> getUserData(List<ProjectUserAssociation> userList) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> resultList = new ArrayList<>();
		for(ProjectUserAssociation data : userList) {
			String userId = data.getProjectMemberIds();
			String userName = userMapper.getRealNameById(userId);
			Map<String,Object> map = new HashMap<>(2000);
			map.put("label", userName);
			map.put("value",userId);
			resultList.add(map);
		}
		return resultList;
	}

	@Override
	public List<RoleUserAssociation> getRoleUserInfo(String projectId, String userId) {
		// TODO Auto-generated method stub
		return roleUserAssociationMapper.getRoleUserInfo(projectId,userId);
	}


	@Override
	public void deleteRoleUserInfo(String projectId, String userId) {
		// TODO Auto-generated method stub
		roleUserAssociationMapper.deleteRoleUserInfo(projectId,userId);
	}


	@Override
	public String getRoleIdByUserId(String projectId,String userId) {
		// TODO Auto-generated method stub
		return roleUserAssociationMapper.getRoleIdByUserId(projectId,userId);
	}


	@Override
	public List<Map<String, Object>> getRoleInfo(String roleId) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> resultList = new ArrayList<>();
		String roleName = runningRoleMapper.getRoleNameByRoleId(roleId);
		Map<String,Object> map = new HashMap<>(2000);
		map.put("label", roleName);
		map.put("value", roleId);
		resultList.add(map);
		return resultList;
	}


	@Override
	public void deleteInfoByUserId(String userId) {
		// TODO Auto-generated method stub
		List<RoleUserAssociation> roleUserInfo =null;
		roleUserInfo = roleUserAssociationMapper.getInfoByUserId(userId);
		if(roleUserInfo!=null) {
			roleUserAssociationMapper.deleteInfoByUserId(userId);
		}
		
	}


	@Override
	public List<String> getRoleIdsByProIdAndUserId(String projectId, String userId) {
		// TODO Auto-generated method stub
		return roleUserAssociationMapper.getRoleIdsByProIdAndUserId(projectId,userId);
	}


	@Override
	public Map<String, Object> getRoleListInfo(String roleId) {
		// TODO Auto-generated method stub
		String roleName = runningRoleMapper.getRoleNameByRoleId(roleId);
		Map<String,Object> map = new HashMap<>(2000);
		map.put("label", roleName);
		map.put("value", roleId);
		return map;
	}


	@Override
	public String selectLeaderByRoleId(String roleId) {
		// TODO Auto-generated method stub
		return roleUserAssociationMapper.selectLeaderByRoleId(roleId);
	}


	@Override
	public void deleteInfoByRoleId(String leaderRoleId) {
		// TODO Auto-generated method stub
		roleUserAssociationMapper.deleteInfoByRoleId(leaderRoleId);
	}


	@Override
	public int delByProAndRole(String projectId, String roleId) {
		return roleUserAssociationMapper.delByProAndRole(projectId, roleId);
	}


	@Override
	public List<String> getUserIdByProAndRole(String projectId, String roleId) {
		return roleUserAssociationMapper.getUserIdByProAndRole(projectId, roleId);
	}

}
