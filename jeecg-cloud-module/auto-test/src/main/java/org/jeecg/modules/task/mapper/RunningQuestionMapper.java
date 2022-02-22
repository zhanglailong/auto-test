package org.jeecg.modules.task.mapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.entity.RunningQuestionHistory;
import org.jeecg.modules.task.vo.RunningQuestionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 问题单
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
@Mapper
public interface RunningQuestionMapper extends BaseMapper<RunningQuestion> {
	/**
	 * 获取问题单所有列表
	 * @param caseId true
	 * @param questionCode true
	 * @param questionType true
	 * @param questionLevel true
	 * @return 没有返回值
	 *
	 * */

	  List<RunningQuestionVO> getListData(@Param("caseId") String[] caseId, @Param("questionCode") String questionCode, @Param("questionType") String questionType, @Param("questionLevel") String questionLevel);

	/**
	 * 获取已归档问题单列表
	 * @param caseId true
	 * @param questionCode true
	 * @param questionType true
	 * @param questionLevel true
	 * @return 没有返回值
	 *
	 * */

	List<RunningQuestionVO> getFileQuestionListData(@Param("caseId") String[] caseId, @Param("questionCode") String questionCode, @Param("questionType") String questionType, @Param("questionLevel") String questionLevel);
	  
	  
	  @Select("SELECT\n"
	  		+ "	qe.id,\n"
	  		+ "	qe.case_id,\n"
	  		+ "	task.task_name AS taskName,\n"
	  		+ "	project.project_name AS projectName,\n"
	  		+ "	rCase.test_name AS caseName,\n"
	  		+ "	qe.question_code,\n"
	  		+ "	qe.question_version,\n"
	  		+ "	qe.question_type,\n"
	  		+ "	qe.question_level,\n"
	  		+ "	qe.question_number,\n"
	  		+ "	qe.question_location,\n"
	  		+ "	qe.question_description,\n"
	  		+ "	qe.opinion,\n"
	  		+ "	qe.reporter,\n"
	  		+ "	qe.report_date\n"
	  		+ "	FROM\n"
	  		+ "	running_question AS qe\n"
	  		+ "	LEFT JOIN running_case AS rCase ON qe.case_id = rCase.id\n"
	  		+ "	LEFT JOIN running_task AS task ON rCase.test_task_id = task.id\n"
	  		+ "	LEFT JOIN running_project AS project ON project.id = task.project_id\n"
	  		+ "	where qe.case_id = #{caseId}"
	  		+ " and qe.del_flag=0 ")
	  /**
	   * 获取问题单列表
	   * @param caseId true
	   * @return 没有返回值
	   * */
	  List<RunningQuestionVO> getRunningQuestionData(String caseId);
	  
	    /**根据测试用例ID查询出项目ID*/
	  @Select("SELECT\n"
	  		+ "	task.project_id\n"
	  		+ "FROM\n"
	  		+ "	running_case AS tCase\n"
	  		+ "INNER JOIN running_task AS task\n"
	  		+ "on tCase.test_task_id=task.id \n"
	  		+ "where tCase.id=#{caseId} ")
	  /**
	   * 根据测试用例ID查询出项目ID
	   * @param caseId true
	   * @return m没有返回值
	   * */
	  String getProjectIdByCaseId(String caseId);

	/**
	 * 查询用例问题单操作历史记录
	 * @param page true
	 * @param params true
	 * @return 没有返回值
	 * */
	IPage<Map<String,Object>> getOperationHistoryList(Page page, @Param("params") RunningQuestionHistory params);

	/**
	 * 根据caseId查询问题单记录
	 * @param caseId
	 * @return
	 */
	@Select("SELECT * FROM running_question WHERE case_id = #{caseId}")
	List<RunningQuestionVO> queryByCaseId(@Param("caseId") String caseId);
	  
}
