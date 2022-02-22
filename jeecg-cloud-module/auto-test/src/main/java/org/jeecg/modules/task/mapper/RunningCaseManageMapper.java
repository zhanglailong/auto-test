package org.jeecg.modules.task.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.vo.CaseTreeVO;
import org.jeecg.modules.task.vo.RunningCaseManageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 测试用例管理
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Mapper
public interface RunningCaseManageMapper extends BaseMapper<RunningCase> {
	
	/**
	* 获取列表信息
	* @param projectId true
	* @param testCode true
	* @param testDate true
	* @param testName true
	* @param testPerson true
	* @param testTaskId true
	 * @return  List<RunningCaseManageVO>
	* */
	List<RunningCaseManageVO> getListData(@Param("testTaskId") String testTaskId, @Param("projectId") String projectId, @Param("testName") String testName, @Param("testCode") String testCode, @Param("testPerson") String testPerson, @Param("testDate") String testDate);
	
	
	@Select(" SELECT\n"
			+ "	testCase.test_task_id,\n"
			+ "	task.task_name AS taskName,\n"
			+ "	project.project_name AS projectInfo,\n"
			+ "	testCase.test_name,\n"
			+ "	testCase.test_code,\n"
			+ "	testCase.test_type,\n"
			+ "	testCase.test_instructions,\n"
			+ "	testCase.test_init,\n"
			+ "	testCase.test_constraint,\n"
			+ "	testCase.test_over,\n"
			+ "	testCase.test_process,\n"
			+ "	testCase.test_person,\n"
			+ "	testCase.test_date,\n"
			+ "	testCase.test_situation,\n"
			+ "	testCase.test_result,\n"
			+ "	testCase.test_supervisor,\n"
			+ "	testCase.test_version,\n"
			+ "	testCase.test_question_code,\n"
			+ "	testCase.test_attributes,\n"
			+ "	testCase.operating_instructions,\n"
			+ "	testCase.expect_result,\n"
			+ "	testCase.actual_result,\n"
			+ "	testCase.test_sx,\n"
			+ "	testCase.user_order,\n"
			+ "	testCase.test_real_result\n"
			+ "	FROM\n"
			+ "	running_case AS testCase\n"
			+ "	LEFT JOIN running_task AS task ON testCase.test_task_id = task.id\n"
			+ "	LEFT JOIN running_project AS project ON project.id = task.project_id"
			+ " WHERE testCase.id=#{id} ")
	/**
	 * 通过id获取复制信息
	 * @param id true
	 * @return List<RunningCase>
	 * */
	List<RunningCase> getCopyDataById(String id);
	
	@Select("SELECT "
			+ "DISTINCT(c.test_template)\n"
			+ "FROM\n"
			+ "	running_task AS a\n"
			+ "INNER JOIN running_project AS b ON a.project_id = b.id\n"
			+ "INNER JOIN running_uut_list AS c ON b.uut_list_id = c.id WHERE a.id= #{id} ")
	/**
	 * 通过id获取测试用例信息
	 * @param id true
	 * @return List<RunningCase>
	 * */
	    String getTestTemplateById(String id);
	
	@Select("SELECT \n"
			+ "vtbl.root_id,\n"
			+ "vtbl.root_name,\n"
			+ "vtbl.parent_id,\n"
			+ "vtbl.parent_name,\n"
			+ "mct1.id,\n"
			+ "mct1.`name`\n"
			+ "FROM \n"
			+ "mass_custom_template AS mct1\n"
			+ "left join evaluation_system_view vtbl on mct1.id = vtbl.id\n"
			+ "WHERE mct1.template_name = #{templateId}\n"
			+ "and mct1.`level` = 3\n"
			+ "and mct1.checked = 0\n"
			+ "ORDER BY mct1.id +0, vtbl.root_id")
	/**
	 * 通过树状信息
	 * @param templateId true
	 * @return List<CaseTreeVO>
	 * */
	List<CaseTreeVO> getTreeData(String templateId);
	
	
	@Select(" SELECT\n"
			+ "	testCase.test_task_id,\n"
			+ "	task.task_name AS taskName,\n"
			+ "	project.project_name AS projectInfo,\n"
			+ "	testCase.test_name,\n"
			+ "	testCase.test_code,\n"
			+ "	testCase.test_type,\n"
			+ "	testCase.test_instructions,\n"
			+ "	testCase.test_init,\n"
			+ "	testCase.test_constraint,\n"
			+ "	testCase.test_over,\n"
			+ "	testCase.test_process,\n"
			+ "	testCase.test_person,\n"
			+ "	testCase.test_date,\n"
			+ "	testCase.test_situation,\n"
			+ "	testCase.test_result,\n"
			+ "	testCase.test_supervisor,\n"
			+ "	testCase.test_version,\n"
			+ "	testCase.test_question_code,\n"
			+ "	testCase.test_attributes,\n"
			+ "	testCase.operating_instructions,\n"
			+ "	testCase.expect_result,\n"
			+ "	testCase.actual_result,\n"
			+ "	testCase.test_sx,\n"
			+ "	testCase.user_order,\n"
			+ "	testCase.test_real_result\n"
			+ "	FROM\n"
			+ "	running_case AS testCase\n"
			+ "	LEFT JOIN running_task AS task ON testCase.test_task_id = task.id\n"
			+ "	LEFT JOIN running_project AS project ON project.id = task.project_id"
			+ " where testCase.del_flag=0 ")
	/**
	 * 获取测试用例管理信息
	 * @return List<RunningCaseManageVO>
	 * */
	List<RunningCaseManageVO> getRunningCaseManageData();

	@Select(" SELECT\n"
			+ "	testCase.test_task_id,\n"
			+ "	task.task_name AS taskName,\n"
			+ "	project.project_name AS projectInfo,\n"
			+ "	project.uut_name AS uutName,\n"
			+ "	project.uut_code AS uutCode,\n"
			+ "	project.uut_version AS uutVersion,\n"
			+ "	project.uut_type AS uutType,\n"
			+ "	testCase.test_name,\n"
			+ "	testCase.test_code,\n"
			+ "	testCase.test_type,\n"
			+ "	testCase.test_instructions,\n"
			+ "	testCase.test_init,\n"
			+ "	testCase.test_constraint,\n"
			+ "	testCase.test_over,\n"
			+ "	testCase.test_process,\n"
			+ "	testCase.test_person,\n"
			+ "	testCase.test_date,\n"
			+ "	testCase.test_situation,\n"
			+ "	testCase.test_result,\n"
			+ "	testCase.test_supervisor,\n"
			+ "	testCase.test_version,\n"
			+ "	testCase.test_question_code,\n"
			+ "	testCase.test_attributes,\n"
			+ "	testCase.operating_instructions,\n"
			+ "	testCase.expect_result,\n"
			+ "	testCase.actual_result,\n"
			+ "	testCase.test_sx,\n"
			+ "	testCase.user_order,\n"
			+ "	testCase.test_real_result\n"
			+ "	FROM\n"
			+ "	running_case AS testCase\n"
			+ "	LEFT JOIN running_task AS task ON testCase.test_task_id = task.id\n"
			+ "	LEFT JOIN running_project AS project ON project.id = task.project_id"
			+ " where testCase.del_flag=0 ")
	/**
	 * 获取测试用例管理信息
	 * @return List<RunningCaseManageVO>
	 * */
	List<RunningCaseManageVO> getRunningCaseManageData1();
	
	
	@Select("SELECT\n"
			+ "	a.id,\n"
			+ "	a.project_name\n"
			+ "FROM\n"
			+ "	running_project AS a\n"
			+ "INNER JOIN running_task AS b ON a.id = b.project_id\n"
			+ "WHERE\n"
			+ "	b.id = #{taskId} \n"
			+ "AND a.del_flag = 0")
	/**
	 * 获取测试用例管理信息
	 * @param taskId true
	 * @return List<RunningCaseManageVO>
	 * */
	RunningProject getProjectNameByTaskId(String taskId);

	@Select(" select project_id from running_task where id=#{taskId} ")
	/**
	 * 获取任务id
	 * @param taskId true
	 * @return List<RunningCaseManageVO>
	 * */
	RunningTask getProjectIdByTaskId(String taskId);
	
		
	
}
