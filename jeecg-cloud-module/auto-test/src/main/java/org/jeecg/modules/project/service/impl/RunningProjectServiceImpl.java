package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.project.entity.RunningProjectHistory;
import org.jeecg.modules.project.entity.RunningProjectTurn;
import org.jeecg.modules.project.mapper.RunningProjectInfoMapper;
import org.jeecg.modules.project.mapper.RunningProjectMapper;
import org.jeecg.modules.project.mapper.RunningProjectTurnMapper;
import org.jeecg.modules.project.service.IRunningProjectService;
import org.jeecg.modules.project.vo.RunningProjectInfo;
import org.jeecg.modules.project.vo.RunningProjectVo;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.task.controller.RunningCaseController;
import org.jeecg.modules.task.controller.RunningQuestionController;
import org.jeecg.modules.task.controller.RunningTaskController;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.mapper.RunningCaseMapper;
import org.jeecg.modules.task.mapper.RunningQuestionMapper;
import org.jeecg.modules.task.mapper.RunningTaskMapper;
import org.jeecg.modules.task.service.IRunningCaseService;
import org.jeecg.modules.task.service.IRunningQuestionService;
import org.jeecg.modules.task.service.IRunningTaskService;
import org.jeecg.modules.task.vo.RunningCaseVO;
import org.jeecg.modules.task.vo.RunningQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningProjectServiceImpl extends ServiceImpl<RunningProjectMapper, RunningProject> implements IRunningProjectService {

	@Autowired
	private IRunningTaskService runningTaskService;
	@Autowired
	private IRunningCaseService runningCaseService;
	@Autowired
	private IRunningQuestionService runningQuestionService;
	@Autowired
	private RunningProjectMapper runningProjectMapper;
	@Autowired
	private RunningProjectInfoMapper runningProjectInfoMapper;
	@Autowired
	private RunningTaskMapper taskMapper;
	@Autowired
	private RunningCaseMapper caseMapper;
	@Autowired
	private RunningQuestionMapper questionMapper;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private RunningProjectTurnMapper runningProjectTurnMapper;
	@Autowired
	private RunningTaskController runningTaskController;
	@Autowired
	private RunningCaseController runningCaseController;
	@Autowired
	private RunningQuestionController runningQuestionController;
	@Autowired
	private IRunningProjectService runningProjectService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public RunningProject findUniqueBy(String fieldname, String value) {
		return runningProjectMapper.findUniqueBy(fieldname, value);
	}

	@Override
	public 	List<RunningProjectInfo> getListDataById(String id) {
		// TODO Auto-generated method stub
		return runningProjectInfoMapper.getListDataById(id);
	}


	@Override
	public Page<RunningProjectInfo> queryPageList(Page<RunningProjectInfo> page, String projectName, String projectCode,
	                                              String createTime, String projectId) {
		// TODO Auto-generated method stub
		return page.setRecords(runningProjectInfoMapper.getListData(projectName, projectCode,createTime, projectId));
	}

	@Override
	public Page<RunningProjectInfo> queryFileProjectPageList(Page<RunningProjectInfo> page, String projectName, String projectCode, String createTime, String projectId) {
		return page.setRecords(runningProjectInfoMapper.getFileProjectListData(projectName,projectCode,createTime,projectId));
	}

	@Override
	public List<RunningProject> getNameByProjectId(String id) {
		// TODO Auto-generated method stub
		return runningProjectMapper.getNameByProjectId(id);
	}

	@Override
	public Integer getProjectNums() {
		// TODO Auto-generated method stub
		return runningProjectMapper.getProjectNums();
	}

	@Override
	public List<RunningProject> getProjectInfo() {
		// TODO Auto-generated method stub
		return runningProjectMapper.getProjectInfo();
	}

	@Override
	/** dq add
	 * 根据项目id查询 任务数量  测试用例数量  问题数量
	 * @return
	 */
	public Map<String, Object> getRelatedCount(String projectId)
	{
		Integer taskCount = 0;
		Integer caseCount = 0;
		Integer questionCount = 0;
		// 根据项目id查询任务数量
		QueryWrapper<RunningTask> taskQueryWrapper = new QueryWrapper<>();
		taskQueryWrapper.eq("project_id",projectId);
		taskQueryWrapper.eq("del_flag",0);
		List<RunningTask> runningTaskList = taskMapper.selectList(taskQueryWrapper);
		if(runningTaskList != null)
		{
			taskCount = runningTaskList.size();
		}
		// 根据任务查用例数量
		for(RunningTask task : runningTaskList)
		{
			String taskId = task.getId();
			QueryWrapper<RunningCase> runningCaseQueryWrapper = new QueryWrapper<>();
			runningCaseQueryWrapper.eq("test_task_id",taskId);
			runningCaseQueryWrapper.eq("del_flag",0);
			List<RunningCase> runningCaseList = caseMapper.selectList(runningCaseQueryWrapper);
			if(runningCaseList != null)
			{
				caseCount += runningCaseList.size();
			}
			// 根据用例查问题数量
			for(RunningCase runningCase : runningCaseList)
			{
				String caseId = runningCase.getId();
				QueryWrapper<RunningQuestion> runningQuestionQueryWrapper = new QueryWrapper<>();
				runningQuestionQueryWrapper.eq("case_id",caseId);
				runningQuestionQueryWrapper.eq("del_flag",0);
				List<RunningQuestion> runningQuestions = questionMapper.selectList(runningQuestionQueryWrapper);
				if(runningQuestions != null)
				{
					questionCount += runningQuestions.size();
				}
			}
		}
		HashMap<String, Object> returnMap = new HashMap<>(2000);
		returnMap.put("taskCount",taskCount);
		returnMap.put("caseCount",caseCount);
		returnMap.put("questionCount",questionCount);
		return returnMap;
	}

	@Override
	public void updateProject(String id) {
		//逻辑删除项目信息
		RunningProject project = new RunningProject();
		String projectId =id;
		project.setId(projectId);
		project.setDelFlag(1);
		this.updateById(project);
		//查询项目下所有任务信息
		List<RunningTask>tasks=runningTaskService.getTaskInfoByProjectId(projectId);
		if(tasks!=null) {
			for(int i=0;i<tasks.size();i++) {
				//逻辑删除项目下所有任务信息
				RunningTask task = new RunningTask();
				String taskId =tasks.get(i).getId();
				task.setId(taskId);
				task.setDelFlag(1);
				runningTaskService.updateById(task);
				//查询任务下所有测试用例信息
				List<RunningCaseVO> cases= runningCaseService.getRunningCaseData(taskId);
				if(cases!=null) {
					for(int j=0;j<cases.size();j++) {
						//逻辑删除任务下所有测试用例信息
						RunningCase case1 = new RunningCase();
						String caseId = cases.get(j).getId();
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
		}
	}

	@Override
	/**操作历史记录查询*/
	public IPage<Map<String, Object>> getOperationHistoryList(Page page, RunningProjectHistory params)
	{
		IPage<Map<String, Object>> historyList = runningProjectMapper.getOperationHistoryList(page, params);
		List<Map<String, Object>> recordList = historyList.getRecords();
		for(Map<String, Object> record : recordList)
		{
			/*以下把用户id转为用户名*/
			String modifyField = (String) record.get("modifyField");
			if("projectMembers".equals(modifyField))
			{
				record.put("modifyFieldVale",getUsernamesByIds(record.get("modifyFieldVale")));
				record.put("modifyFieldOldVale",getUsernamesByIds(record.get("modifyFieldOldVale")));
			}
		}
		return historyList;
	}

	@Override
	/**
	 * 根据用户id返回用户realname,用户id逗号分隔
	 */
	public String getUsernamesByIds(Object userIds)
	{
		if(StringUtils.isEmpty(userIds))
		{
			return "";
		}
		QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
		sysUserQueryWrapper.in("id",((String)userIds).split(","));
		List<SysUser> users = sysUserService.list(sysUserQueryWrapper);
		String userNames = users.stream().map(SysUser::getRealname).collect(Collectors.joining(","));
		return userNames;
	}

	@Override
	public Page<RunningProjectVo> queryList(Page page) {
		Page<RunningProjectVo> list = runningProjectMapper.queryList(page);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		for(RunningProjectVo runningProjectVo :list.getRecords()){
			runningProjectVo.setCreateBy(sysUser.getUsername());
		}
		return list;
	}
	/**
	 * 获取项目名称
	 * */
	@Override
	public String getProjectName(String projectId) {
		return runningProjectInfoMapper.getProjectName(projectId);
	}

	/**
	 * 根据项目名称查询项目id
	 * @param projectCode
	 * @return
	 */
	@Override
	public String getProjectId(String projectCode) {
		return runningProjectInfoMapper.getProjectId(projectCode);
	}

	/**
	 * 获取轮次
	 * */
	public List<RunningProjectTurn> getTurn(String projectId){
		return runningProjectTurnMapper.getTurn(projectId);
	}

	@Override
	@DS("uutDatabase")
	public List<Map<String, String>> getAllVersion(String uutId) {return runningProjectTurnMapper.getAllVersion(uutId);}

	@Override
	public List<Map<String, String>> getRoleToUser(String projectId) {
		return runningProjectMapper.getRoleToUser(projectId);
	}

	// 下拉选层级列表
	@Override
	public List<DictModel> getOptionByCondition(String projectId, String turnId){
		return runningProjectTurnMapper.getOptionByCondition(projectId, turnId);
	};

	// 下拉选层级列表
	@Override
	public List<DictModel> getOptionByConditionByTaskId(String taskId, String turnId){
		return runningProjectTurnMapper.getOptionByConditionByTaskId(taskId, turnId);
	};

	public List<String> getUserIdList(String userName) {
		return runningProjectMapper.getUserIdList(userName);
	}

	@Override
	@DS("uutDatabase")
	public List<DictModel> getUutNameByStatus() {
		return runningProjectTurnMapper.getUutNameByStatus();
	}

	/**
	 * 将归档时未选择的测试项进行逻辑删除且记录到测试项履历表中
	 * @param projectIds
	 * @param taskIds
	 */
	public void signFileTaskData(String projectIds, String taskIds){

		if(!me.zhyd.oauth.utils.StringUtils.isEmpty(taskIds)) {
			List<String> taskIdList = Arrays.asList(taskIds.split(","));
			if (!me.zhyd.oauth.utils.StringUtils.isEmpty(projectIds)) {
				List<String> projectIdList = Arrays.asList(projectIds.split(","));
				QueryWrapper<RunningTask> taskQueryWrapper = new QueryWrapper();
				taskQueryWrapper.eq("del_flag", 0).in("project_id", projectIdList);
				List<RunningTask> runningTaskList = runningTaskService.list(taskQueryWrapper);
				List<String> taskIdLists = new ArrayList<>();
				if (runningTaskList.size() > 0 && runningTaskList != null) {
					for (RunningTask runningTask : runningTaskList) {
						taskIdLists.add(runningTask.getId());
					}
					taskIdLists.removeAll(taskIdList);
					if (taskIdLists.size() > 0 && taskIdLists != null) {
						for (String taskId : taskIdLists) {
							RunningTask record = runningTaskService.getById(taskId);
							record.setDelFlag(1);
							runningTaskService.updateById(record);
							runningTaskController.insertHistory(record, "2", taskId);
						}
					}
				}
			}
		}else{
			if(!me.zhyd.oauth.utils.StringUtils.isEmpty(projectIds)){
				List<String> projectIdList = Arrays.asList(projectIds.split(","));
				List<String> taskIdList = new ArrayList<>();
				for(String projectId : projectIdList){
					QueryWrapper<RunningTask> taskQueryWrapper = new QueryWrapper<>();
					taskQueryWrapper.eq("project_id",projectId).eq("del_flag", 0);
					List<RunningTask> runningTaskLists = runningTaskService.list(taskQueryWrapper);
					if(runningTaskLists.size() > 0 && runningTaskLists != null){
						for(RunningTask runningTask : runningTaskLists){
							taskIdList.add(runningTask.getId());
							runningTask.setDelFlag(1);
							runningTaskService.updateById(runningTask);
							runningTaskController.insertHistory(runningTask, "2", runningTask.getId());
						}
					}
				}
				List<String> caseIdList = new ArrayList<>();
				for(String taskId : taskIdList){
					QueryWrapper<RunningCase> caseQueryWrapper = new QueryWrapper<>();
					caseQueryWrapper.eq("test_task_id", taskId).eq("del_flag", 0);
					List<RunningCase> runningCaseLists = runningCaseService.list(caseQueryWrapper);
					if(runningCaseLists.size() > 0 &&runningCaseLists != null){
						for(RunningCase runningCase : runningCaseLists){
							caseIdList.add(runningCase.getId());
							runningCase.setDelFlag(1);
							runningCaseService.updateById(runningCase);
							runningCaseController.insertHistory(runningCase, "2", runningCase.getId());
						}
					}
				}
				if(caseIdList.size() > 0 && caseIdList != null){
					for(String caseId : caseIdList){
						QueryWrapper<RunningQuestion> questionQueryWrapper = new QueryWrapper<>();
						questionQueryWrapper.eq("case_id", caseId).eq("del_flag", 0);
						List<RunningQuestion> runningQuestionLists = runningQuestionService.list(questionQueryWrapper);
						if(runningQuestionLists.size() > 0 && runningQuestionLists != null){
							for(RunningQuestion runningQuestion : runningQuestionLists){
								runningQuestion.setDelFlag(1);
								runningQuestionService.updateById(runningQuestion);
								runningQuestionController.insertHistory(runningQuestion, 2, runningQuestion.getId());
							}
						}
					}
				}
			}
		}
	}

	@Override
	public List<RunningProject> getRunningProjectList(String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		return runningProjectMapper.getRunningProjectList(list);
	}
}
