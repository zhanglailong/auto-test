package org.jeecg.modules.task.service.impl;

import java.util.List;
import java.util.Map;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.project.service.IRunningProjectService;
import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.entity.RunningQuestionHistory;
import org.jeecg.modules.task.mapper.RunningQuestionMapper;
import org.jeecg.modules.task.service.IRunningQuestionService;
import org.jeecg.modules.task.vo.RunningQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 问题单
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningQuestionServiceImpl extends ServiceImpl<RunningQuestionMapper, RunningQuestion> implements IRunningQuestionService {

	@Autowired
	private RunningQuestionMapper runningQuestionMapper;
	@Autowired
	private IRunningProjectService projectService;
	
	@Override
	public Page<RunningQuestionVO> queryPageList(Page<RunningQuestionVO> page, String caseId, String questionCode,
	                                             String questionType, String questionLevel) {
		// TODO Auto-generated method stub
		return page.setRecords(runningQuestionMapper.getListData(caseId.split(","), questionCode, questionType,questionLevel));
	}

	@Override
	public Page<RunningQuestionVO> queryFileQuestionPageList(Page<RunningQuestionVO> page, String caseId, String questionCode,
												 String questionType, String questionLevel) {
		// TODO Auto-generated method stub
		return page.setRecords(runningQuestionMapper.getFileQuestionListData(caseId.split(","), questionCode, questionType,questionLevel));
	}
	@Override
	public List<RunningQuestionVO> getRunningQuestionData(String caseId) {
		// TODO Auto-generated method stub
		return runningQuestionMapper.getRunningQuestionData(caseId);
	}

	@Override
	/**用例问题单操作历史记录查询*/
	public IPage<Map<String, Object>> getOperationHistoryList(Page page, RunningQuestionHistory params)
	{
		IPage<Map<String, Object>> historyList = runningQuestionMapper.getOperationHistoryList(page, params);
		List<Map<String, Object>> recordList = historyList.getRecords();
		for(Map<String, Object> record : recordList)
		{
			/*以下把用户id转为用户名*/
			String modifyField = (String) record.get("modifyField");
			if("reporter".equals(modifyField))
			{
				record.put("modifyFieldVale",projectService.getUsernamesByIds(record.get("modifyFieldVale")));
				record.put("modifyFieldOldVale",projectService.getUsernamesByIds(record.get("modifyFieldOldVale")));
			}
		}
		return historyList;
	}
	@Override
	public String getProjectIdByCaseId(String caseId) {
		// TODO Auto-generated method stub
		return runningQuestionMapper.getProjectIdByCaseId(caseId);
	}

	@Override
	public List<RunningQuestionVO> queryByCaseId(String caseId) {
		return runningQuestionMapper.queryByCaseId(caseId);
	}
}
