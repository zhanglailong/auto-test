package org.jeecg.modules.task.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.mapper.RunningCaseManageMapper;
import org.jeecg.modules.task.model.CaseTreeIdModel;
import org.jeecg.modules.task.service.IRunningCaseManageService;
import org.jeecg.modules.task.service.IRunningQuestionService;
import org.jeecg.modules.task.vo.CaseTreeVO;
import org.jeecg.modules.task.vo.RunningCaseManageVO;
import org.jeecg.modules.task.vo.RunningQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 测试用例管理
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningCaseManageServiceImpl extends ServiceImpl<RunningCaseManageMapper, RunningCase> implements IRunningCaseManageService {

	@Autowired
	private IRunningQuestionService runningQuestionService;
	@Autowired
	RunningCaseManageMapper runningCaseManageMapper;
	
	@Override
	public Page<RunningCaseManageVO> queryPageList(Page<RunningCaseManageVO> page,String testTaskId,String projectId, String testName, String testCode, String testPerson,String testDate) {
		// TODO Auto-generated method stub
		return page.setRecords(runningCaseManageMapper.getListData(testTaskId,projectId,testName, testCode,testPerson,testDate));
	}
	
	@Override
	public List<RunningCase> getCopyDataById(String id) {
		// TODO Auto-generated method stub
		return runningCaseManageMapper.getCopyDataById(id);
	}
	@Override
	public String getTestTemplateById(String id) {
		// TODO Auto-generated method stub
		return runningCaseManageMapper.getTestTemplateById(id);
	}
	

	@Override
	public List<CaseTreeIdModel> queryTreeList(String templateId) {
		// 获取树形下拉列表数据
		List<CaseTreeVO> list = runningCaseManageMapper.getTreeData(templateId);
		List<CaseTreeIdModel> treeList = new ArrayList<CaseTreeIdModel>();

		// 处理中的子特性
		String prePid = null;
		// 处理中的质量特性
		String preRootId = null;
		// 质量特性节点
		CaseTreeIdModel rootModel = null;
		// 子特性节点
		CaseTreeIdModel parentModel = null;
		
		for (CaseTreeVO caseTreeVO : list) {
			String pid = caseTreeVO.getParentId();
			String rootId = caseTreeVO.getRootId();
			// 初期设定：将处理中的质量特性ID和子特性ID记录下来
			if (preRootId == null && prePid == null) {
				prePid = pid;
				preRootId = rootId;
				rootModel = new CaseTreeIdModel();
				rootModel.setTitle(caseTreeVO.getRootName());
				rootModel.setKey(rootId);
				rootModel.setValue(rootId);
				parentModel = new CaseTreeIdModel();
				parentModel.setTitle(caseTreeVO.getParentName());
				parentModel.setKey(pid);
				parentModel.setValue(pid);
			}
			// 比较处理中的质量特性ID和当前质量特性ID
			if (preRootId.equals(rootId)) {
				// 比较处理中的子特性ID和当前子特性ID
				if (prePid.equals(pid)) {
					// 相同的情况下，添加度量节点
					CaseTreeIdModel childModel = new CaseTreeIdModel();
					childModel.setTitle(caseTreeVO.getName());
					childModel.setKey(caseTreeVO.getId());
					childModel.setValue(caseTreeVO.getId());
					parentModel.getChildren().add(childModel);
				} else {
					// 不相同的情况下，将处理中的子特性信息添加到处理中的质量特性的节点下方
					rootModel.getChildren().add(parentModel);
					// 重新初始化子特性ID
					prePid = pid;
					// 创建子特性节点
					parentModel = new CaseTreeIdModel();
					parentModel.setTitle(caseTreeVO.getParentName());
					parentModel.setKey(pid);
					parentModel.setValue(pid);
					// 添加度量节点
					CaseTreeIdModel childModel = new CaseTreeIdModel();
					childModel.setTitle(caseTreeVO.getName());
					childModel.setKey(caseTreeVO.getId());
					childModel.setValue(caseTreeVO.getId());
					parentModel.getChildren().add(childModel);
				}
			} else {
				// 更换了质量特性ID的时候，将处理中的子特性信息添加到处理中的质量特性的节点下方
				rootModel.getChildren().add(parentModel);
				treeList.add(rootModel);
				// 重新初始化处理中的质量特性ID和当前质量特性ID
				preRootId = rootId;
				prePid = pid;
				// 创建质量特性节点
				rootModel = new CaseTreeIdModel();
				rootModel.setTitle(caseTreeVO.getRootName());
				rootModel.setKey(rootId);
				rootModel.setValue(rootId);
				// 创建子特性节点
				parentModel = new CaseTreeIdModel();
				parentModel.setTitle(caseTreeVO.getParentName());
				parentModel.setKey(pid);
				parentModel.setValue(pid);
				// 添加度量节点
				CaseTreeIdModel childModel = new CaseTreeIdModel();
				childModel.setTitle(caseTreeVO.getName());
				childModel.setKey(caseTreeVO.getId());
				childModel.setValue(caseTreeVO.getId());
				parentModel.getChildren().add(childModel);
			}
		}
		// 将最后的处理的子特性信息添加到处理中的质量特性的节点下方
		rootModel.getChildren().add(parentModel);
		treeList.add(rootModel);
		
		return treeList;		
	}

	@Override
	public List<RunningCaseManageVO> getRunningCaseManageData() {
		// TODO Auto-generated method stub
		return runningCaseManageMapper.getRunningCaseManageData();
	}

	@Override
	public List<RunningCaseManageVO> getRunningCaseManageData1() {
		// TODO Auto-generated method stub
		return runningCaseManageMapper.getRunningCaseManageData1();
	}

	@Override
	public RunningProject getProjectNameByTaskId(String taskId) {
		// TODO Auto-generated method stub
		return runningCaseManageMapper.getProjectNameByTaskId(taskId);
	}
	
	@Override
	public void updateCaseManage(String id) {
		//逻辑删除测试用例信息
		RunningCase cases= new RunningCase();
		String caseId = id;
		cases.setId(caseId);
		cases.setDelFlag(1);
		this.updateById(cases);
		//查询测试用例下所有问题信息
		List<RunningQuestionVO> questions = runningQuestionService.getRunningQuestionData(caseId);
		if(questions!=null) {
			for(int i=0;i<questions.size();i++) {
				//逻辑删除测试用例下所有问题信息
				RunningQuestion question = new RunningQuestion();
				String questionId = questions.get(i).getId();
				question.setId(questionId);
				question.setDelFlag(1);
				runningQuestionService.updateById(question);
			}
		}
	
		
	}

	@Override
	public RunningTask getProjectIdByTaskId(String taskId) {
		// TODO Auto-generated method stub
		return runningCaseManageMapper.getProjectIdByTaskId(taskId);
	}



}
