package org.jeecg.modules.task.service.impl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.mapper.RunningQuestionManageMapper;
import org.jeecg.modules.task.service.IRunningQuestionManageService;
import org.jeecg.modules.task.vo.RunningQuestionManageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 问题单管理
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningQuestionManageServiceImpl extends ServiceImpl<RunningQuestionManageMapper, RunningQuestion> implements IRunningQuestionManageService {

	@Autowired
	private RunningQuestionManageMapper runningQuestionManageMapper;
	
	@Override
	public Page<RunningQuestionManageVO> queryPageList(Page<RunningQuestionManageVO> page,String projectId,String questionCode,String questionType,String questionLevel) {
		// TODO Auto-generated method stub
		return page.setRecords(runningQuestionManageMapper.getListData(projectId,questionCode,questionType,questionLevel));
	}

	@Override
	public RunningQuestion getQuestionManageDataById(String id) {
		// TODO Auto-generated method stub
		return runningQuestionManageMapper.getQuestionManageDataById(id);
	}

	@Override
	public List<RunningQuestionManageVO> getRunningQuestionManageData() {
		// TODO Auto-generated method stub
		return runningQuestionManageMapper.getRunningQuestionManageData();
	}

	@Override
	public String getProjectIdByCaseId(String caseId) {
		// TODO Auto-generated method stub
		return runningQuestionManageMapper.getProjectIdByCaseId(caseId);
	}


}
