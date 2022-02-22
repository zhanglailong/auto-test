package org.jeecg.modules.task.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.entity.RunningQuestionHistory;
import org.jeecg.modules.task.vo.RunningQuestionVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 问题单
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
public interface IRunningQuestionService extends IService<RunningQuestion> {

	/**
	 * 分页查询问题单列表
	 * @param page true
	 * @param caseId true
	 * @param questionCode true
	 * @param questionType true
	 * @param questionLevel true
	 * @return 没有返回值
	 */
	Page<RunningQuestionVO> queryPageList(Page<RunningQuestionVO> page, String caseId, String questionCode, String questionType, String questionLevel);

	/**
	 * 分页查询已归档问题单列表
	 * @param page true
	 * @param caseId true
	 * @param questionCode true
	 * @param questionType true
	 * @param questionLevel true
	 * @return 没有返回值
	 */
	Page<RunningQuestionVO> queryFileQuestionPageList(Page<RunningQuestionVO> page, String caseId, String questionCode, String questionType, String questionLevel);
	
	/**
	 * 根据测试用例ID查询问题信息
	 * @param caseId true
	 * @return 没有返回值
	 */
	List<RunningQuestionVO> getRunningQuestionData(String caseId);

	/**
	 * 用例问题单操作历史记录查询
	 * @param page true
	 * @param params true
	 * @return 没有返回值
	 */
	IPage<Map<String,Object>> getOperationHistoryList(Page page, RunningQuestionHistory params);
	
	/**
	 * 根据测试用例ID查询项目ID
	 * @param caseId true
	 * @return 没有返回值
	 */
	String getProjectIdByCaseId(String caseId);

	/**
	 * 根据caseId查询问题单记录
	 * @param caseId
	 * @return
	 */
	List<RunningQuestionVO> queryByCaseId(String caseId);
}
