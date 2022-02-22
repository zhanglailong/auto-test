package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.ProjectUserAssociation;
import org.jeecg.modules.project.mapper.ProjectUserAssociationMapper;
import org.jeecg.modules.project.service.ProjectUserAssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 项目用户关联
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class ProjectUserAssociationServiceImpl extends ServiceImpl<ProjectUserAssociationMapper, ProjectUserAssociation> implements ProjectUserAssociationService {

	@Autowired
	private ProjectUserAssociationMapper projectUserAssociationMapper;
	


	@Override
	public List<ProjectUserAssociation> getInfoByProjectId(String projectId) {
		// TODO Auto-generated method stub
		return projectUserAssociationMapper.getInfoByProjectId(projectId);
	}



	@Override
	public void deleteDataByProjectId(String projectId) {
		// TODO Auto-generated method stub
		projectUserAssociationMapper.deleteDataByProjectId(projectId);
	}



	@Override
	public void deleteInfoByUserId(String userId) {
		// TODO Auto-generated method stub
		List<ProjectUserAssociation> projectUserInfo=null;
		projectUserInfo= projectUserAssociationMapper.getInfoByUserId(userId);
		if(projectUserInfo!=null) {
			projectUserAssociationMapper.deleteInfoByUserId(userId);
		}
		
	}



	@Override
	public List<String> getUserIdsByProjectId(String projectId) {
		// TODO Auto-generated method stub
		return projectUserAssociationMapper.getUserIdsByProjectId(projectId);
	}
	
	
	
	
	
	
	
}
