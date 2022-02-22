package org.jeecg.modules.task.service;

import java.util.List;

import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.vo.RunningQuestionManageVO;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 问题单管理
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
public interface IRunningQuestionManageService extends IService<RunningQuestion> {
	
	/**
	 * 分页查询问题管理列表
	 * @param page true
	 * @param projectId true
	 * @param questionCode true
	 * @param questionType true
	 * @param questionLevel true
	 * @return 没有返回值
	 */
	Page<RunningQuestionManageVO> queryPageList(Page<RunningQuestionManageVO> page, String projectId, String questionCode, String questionType, String questionLevel);
	
	/**
	 * 根据ID查询问题单管理单条数据
	 * @param id true
	 * @return 没有返回值
	 */
	RunningQuestion getQuestionManageDataById(String id);
	

	/**
	 * 问题单管理导出数据查询
	 * @return 没有返回值
	 */
	List<RunningQuestionManageVO> getRunningQuestionManageData();
	
	/**
	 * 根据测试用例ID查询项目ID
	 * @param caseId true
	 * @return 没有返回值
	 */
	String getProjectIdByCaseId(String caseId);

}
