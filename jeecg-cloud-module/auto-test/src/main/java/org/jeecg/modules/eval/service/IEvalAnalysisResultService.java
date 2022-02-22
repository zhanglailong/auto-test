package org.jeecg.modules.eval.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.eval.entity.EvalAnalysisResult;
import org.jeecg.modules.eval.vo.EvalAnalysisResultVO;


/**
 * @Description: 分析评价结果表
 * @Author: jeecg-boot
 * @Date:   2020-12-30
 * @Version: V1.0
 */
public interface IEvalAnalysisResultService extends IService<EvalAnalysisResult> {
	/**
	 * 分页
	 * @param page true
	 * @param evaluate true
	 * @param projectId true
	 * @return 无返回值
	 * */
	Page<EvalAnalysisResultVO> queryPageList(Page<EvalAnalysisResultVO> page, String uutName, String evaluate, String projectId);
	/**
	 * 查询
	 * @param id true
	 * @return 无返回值
	 * */
	EvalAnalysisResultVO evalResult(String id);
	/**
	 * 查询最终评价
	 * @param projectId true
	 * @return String
	 * */
	String getEvaluation(String projectId);
}
