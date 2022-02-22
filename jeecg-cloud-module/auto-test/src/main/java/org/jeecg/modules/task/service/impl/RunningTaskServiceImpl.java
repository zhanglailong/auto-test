package org.jeecg.modules.task.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.project.mapper.RunningProjectMapper;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.mapper.RunningTaskMapper;
import org.jeecg.modules.task.service.IRunningCaseService;
import org.jeecg.modules.task.service.IRunningQuestionService;
import org.jeecg.modules.task.service.IRunningTaskService;
import org.jeecg.modules.task.vo.RunningCaseVO;
import org.jeecg.modules.task.vo.RunningQuestionVO;
import org.jeecg.modules.task.vo.RunningTaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 任务管理
 * @Author: jeecg-boot
 * @Date:   2020-12-25
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningTaskServiceImpl extends ServiceImpl<RunningTaskMapper, RunningTask> implements IRunningTaskService {

	@Autowired
	private IRunningCaseService runningCaseService;
	@Autowired
	private IRunningQuestionService runningQuestionService;
	@Autowired
	private RunningTaskMapper runningTaskMapper;
	@Autowired
	private RunningProjectMapper runningProjectMapper;
	@Override
	public String getCjUrlByTaskId(String id) {
		// TODO Auto-generated method stub
		return runningTaskMapper.getCjUrlByTaskId(id);
	}
	@Override
	public String getRealName(String userName) {
		// TODO Auto-generated method stub
		return runningTaskMapper.getRealName(userName);
	}
	@Override
	public Page<RunningTaskVO> queryPageList(Page<RunningTaskVO> page, String projectId, String taskName,
			String priority, String taskCode, String taskSoftName) {
		// TODO Auto-generated method stub
		return page.setRecords(runningTaskMapper.getListData(page, projectId, taskName,priority,taskCode,taskSoftName));
	}
	@Override
	public Page<RunningTaskVO> queryFileTaskPageList(Page<RunningTaskVO> page, String projectId, String taskName,
											 String priority, String taskCode, String taskSoftName) {
		// TODO Auto-generated method stub
		List<RunningTaskVO> runningTaskVOList = runningTaskMapper.getFileTaskListData(page, projectId, taskName,priority,taskCode,taskSoftName);
		List<RunningTaskVO> runningTaskVOS = new ArrayList<>();
		if(runningTaskVOList.size() > 0 && runningTaskVOList != null){
			for(RunningTaskVO runningTaskVO : runningTaskVOList){
				runningTaskVO.setCaseFiledNum(runningTaskMapper.getCaseFiledNum(runningTaskVO.getId()));
				runningTaskVOS.add(runningTaskVO);
			}
		}
		return page.setRecords(runningTaskVOS);
	}
	@Override
	public List<RunningTask> getTaskInfoByProjectId(String projectId) {
		// TODO Auto-generated method stub
		return runningTaskMapper.getTaskInfoByProjectId(projectId);
	}
	@Override
	public void updateTask(String id) {
		//逻辑删除任务信息
		RunningTask task = new RunningTask();
		String taskId = id;
		task.setId(taskId);
		task.setDelFlag(1);
		this.updateById(task);
		//查询任务下所有测试用例信息
		List<RunningCaseVO> cases= runningCaseService.getRunningCaseData(taskId);
		if(cases!=null) {
			for(int i=0;i<cases.size();i++) {
				//逻辑删除任务下所有测试用例信息
				RunningCase case1 = new RunningCase();
				String caseId = cases.get(i).getId();
				case1.setId(caseId);
				case1.setDelFlag(1);
				runningCaseService.updateById(case1);
				//查询测试用例下所有问题信息
				List<RunningQuestionVO> questions = runningQuestionService.getRunningQuestionData(caseId);
				if(questions!=null) {
					for(int z=0;z<questions.size();z++) {
						//逻辑删除测试用例下所有问题信息
						RunningQuestion question = new RunningQuestion();
						String questionId = questions.get(z).getId();
						question.setId(questionId);
						question.setDelFlag(1);
						runningQuestionService.updateById(question);
					}
				}
			}
		}
	}

	/**
	 * 根据项目ID查询uut_list_id
	 * @param projectId true
	 * @return
	 */
	@Override
	public List<String> getUutListId(String projectId){
		return runningTaskMapper.getUutListId(projectId);
	}

	/**
	 * 根据uut_list_id查询user_id
	 * @param uutListId true
	 * @return
	 */
	@Override
	@DS("uutDatabase")
	public List<String> getUserId(String uutListId){
		return runningTaskMapper.getUserId(uutListId);
	}

	/**
	 * 根据userId查询realname
	 * @param userId
	 * @return
	 */
	@Override
	public String getUserRealName(String userId){
		return runningTaskMapper.getUserRealName(userId);
	};

	/**
	 * 根据turnId查询turnNum
	 * @param turnId
	 * @return
	 */
	@Override
	public String getTurnNum(String turnId){
		return runningTaskMapper.getTurnNum(turnId);
	};
	
	
	
	
	

}
