package org.jeecg.modules.task.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.vo.RunningQuestionManageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 问题单管理
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
public interface RunningQuestionManageMapper extends BaseMapper<RunningQuestion> {
	/**
	 * 获取数据列表
	 * @param projectId true
	 * @param questionCode true
	 * @param questionLevel true
	 * @param questionType true
	 * @return  List<RunningQuestionManageVO>
	 * */
  List<RunningQuestionManageVO> getListData(@Param("projectId") String projectId, @Param("questionCode") String questionCode, @Param("questionType") String questionType, @Param("questionLevel") String questionLevel);
  
  @Select("SELECT\n"
  		+ "	qe.id,"
  		+ " qe.case_id,\n"
  		+ "	qe.question_code,\n"
  		+ "	task.id AS taskId,\n"
  		+ "	task.task_name as taskName,\n"
  		+ "	project.project_name as projectName,\n"
  		+ "	cases.test_name AS caseName,\n"
  		+ "	qe.question_version,\n"
  		+ "	qe.question_type,\n"
  		+ "	qe.question_level,\n"
  		+ " qe.question_number,\n"
  		+ " qe.question_description,\n"
  		+ " qe.opinion,\n"
  		+ "	qe.question_location,\n"
  		+ "	qe.reporter,\n"
  		+ "	qe.report_date\n"
  		+ "	FROM\n"
  		+ "	running_question AS qe\n"
  		+ "	LEFT JOIN running_case AS cases ON qe.case_id = cases.id\n"
  		+ "	LEFT JOIN running_task AS task ON cases.test_task_id = task.id\n"
  		+ "	LEFT JOIN running_project AS project ON task.project_id = project.id"
  		+ " WHERE qe.id=#{id} and qe.del_flag=0 ")
  /**
   * 获取问题单管理id
   * @param id true
   * @return 没有返回值
   * */
  RunningQuestion getQuestionManageDataById(String id);

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
	  		+ "	LEFT JOIN running_project AS project ON project.id = task.project_id "
	  		+ " where qe.del_flag=0 ")
  /**
   * 获取问题单管理id
   * @return List<RunningQuestionManageVO>
   * */
  List<RunningQuestionManageVO> getRunningQuestionManageData();
  
  @Select(" SELECT\n"
  		+ "	DISTINCT(task.project_id)\n"
  		+ "FROM\n"
  		+ "	running_case AS rcase\n"
  		+ "INNER JOIN running_task AS task ON rcase.test_task_id = task.id\n"
  		+ "WHERE\n"
  		+ "	rcase.id = #{caseId} ")
  /**
   * 获取任务id
   * @param caseId true
   * @return List<RunningQuestionManageVO>
   * */
  String getProjectIdByCaseId(String caseId);
  

}
