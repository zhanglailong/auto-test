package org.jeecg.modules.task.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.vo.CaseTreeVO;
import org.jeecg.modules.task.vo.RunningCaseVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.uut.entity.RunningUutList;


/**
 * @Description: 测试用例表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Mapper
public interface RunningCaseMapper extends BaseMapper<RunningCase> {

	/**
	 * 获取列表信息
	 * @param testTaskId true
	 * @param testCode true
	 * @param testDate true
	 * @param testName true
	 * @param testPerson true
	 * @return  List<RunningCaseVO>
	 * */
	List<RunningCaseVO> getListData(@Param("testTaskId") String[] testTaskId, @Param("testName") String testName, @Param("testCode") String testCode, @Param("testPerson") String testPerson, @Param("testDate") String testDate);

	/**
	 * 获取列表信息
	 * @param testTaskId true
	 * @param testCode true
	 * @param testDate true
	 * @param testName true
	 * @param testPerson true
	 * @return  List<RunningCaseVO>
	 * */
	List<RunningCaseVO> getFileCaseListData(@Param("testTaskId") String[] testTaskId, @Param("testName") String testName, @Param("testCode") String testCode, @Param("testPerson") String testPerson, @Param("testDate") String testDate);

	/**
	 * 获取列表信息
	 * @param testName true
	 * @return  List<RunningCaseVO>
	 * */
	List<RunningCaseVO> queryPageListByTestName(@Param("testName") String testName);

	/**
	 * 获取列表信息
	 * @param testId true
	 * @return  List<RunningCaseVO>
	 * */
	List<RunningCaseVO> queryPageListByTestId(@Param("testId") String[] testId);


	/**
	 * 获取列表信息
	 * @param caseId true
	 * @return  List<RunningCase>
	 * */
	List<RunningCase> getTestCopyDataById(@Param("caseId") String[] caseId);
	
	@Select("SELECT\n"
			+ "	test_task_id,\n"
			+ "	test_name,\n"
			+ "	test_code,\n"
			+ " test_relationship,\n"
			+ "	test_type,\n"
			+ "	test_instructions,\n"
			+ "	test_init,\n"
			+ "	test_constraint,\n"
			+ "	test_over,\n"
			+ "	test_process,\n"
			+ "	test_criterion,\n"
			+ "	test_person,\n"
			+ "	test_date,\n"
			+ "	test_situation,\n"
			+ "	test_result,\n"
			+ "	test_supervisor,\n"
			+ "	test_version,\n"
			+ "	test_question_code,\n"
			+ "	test_attributes,\n"
			+ "	operating_instructions,\n"
			+ "	expect_result,\n"
			+ "	actual_result,\n"
			+ "	test_sx,\n"
			+ "	test_real_result\n"
			+ "\n"
			+ "FROM\n"
			+ "	running_case where id= #{id} ")
	/**
	 * 获取列表信息
	 * @param id true
	 * @return  List<RunningCase>
	 * */
	List<RunningCase> getCopyDataById(String id);
	
	@Select("SELECT "
			+ "DISTINCT(c.test_template)\n"
			+ "FROM\n"
			+ "	running_task AS a\n"
			+ "INNER JOIN running_project AS b ON a.project_id = b.id\n"
			+ "INNER JOIN running_uut_list AS c ON b.uut_list_id = c.id WHERE a.id= #{id} ")
	/**
	 * 获取列表信息
	 * @param id true
	 * @return  没有返回值
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
	 * 获取列表信息
	 * @param templateId true
	 * @return  List<CaseTreeVO>
	 * */
	List<CaseTreeVO> getTreeData(String templateId);
	
	@Select("SELECT DISTINCT(test_name) as testName,id FROM running_case WHERE test_task_id=#{taskId} ")
	/**
	 * 获取列表信息
	 * @param taskId true
	 * @return  List<RunningCase>
	 * */
	List<RunningCase> getCaseNameByTaskId(String taskId);

	@Select("SELECT * FROM running_case WHERE test_task_id=#{taskId} AND del_flag='0' ")
	List<RunningCase> getRunningCaseList(String taskId);
	
	@Select("SELECT\n"
			+ "	testCase.id,\n"
			+ "	testCase.test_task_id,\n"
			+ "	task.task_name AS taskName,\n"
			+ "	project.project_name AS projectName,\n"
			+ "	testCase.test_name,\n"
			+ "	testCase.test_code,\n"
			+ " testCase.test_relationship,\n"
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
			+ "	LEFT JOIN running_project AS project ON task.project_id = project.id\n"
			+ "	WHERE testCase.test_task_id = #{testTaskId} and testCase.del_flag=0")
	/**
	 * 获取列表信息
	 * @param testTaskId true
	 * @return  List<RunningCaseVO>
	 * */
    List<RunningCaseVO> getRunningCaseData(String testTaskId);


	@Select("SELECT\n"
			+ "	testCase.id,\n"
			+ "	testCase.test_task_id,\n"
			+ "	task.task_name AS taskName,\n"
			+ "	project.project_name AS projectName,\n"
			+ "	project.uut_name AS uutName,\n"
			+ "	project.uut_code AS uutCode,\n"
			+ "	project.uut_version AS uutVersion,\n"
			+ "	project.uut_type AS uutType,\n"
			+ "	testCase.test_name,\n"
			+ "	testCase.test_code,\n"
			+ " testCase.test_relationship,\n"
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
			+ "	LEFT JOIN running_project AS project ON task.project_id = project.id\n"
			+ "	WHERE testCase.test_task_id = #{testTaskId} and testCase.del_flag=0")
	/**
	 * 获取列表信息
	 * @param testTaskId true
	 * @return  List<RunningCaseVO>
	 * */
	List<RunningCaseVO> getRunningCaseData1(String testTaskId);
	
	@Select(" select project_id from running_task where id=#{taskId} ")
	/**
	 * 获取列表信息
	 * @param taskId true
	 * @return  没有返回值
	 * */
	RunningTask getProjectIdByTaskId(String taskId);

	/**
	 * 获取列表信息
	 * @param caseId true
	 * @return  没有返回值
	 * */
	RunningUutList getUutListByCaseId(String caseId);

	/**
	 * 获取列表信息
	 * @param taskId true
	 * @return  没有返回值
	 * */
	RunningUutList getUutListByTaskId(String taskId);

	/**
	 * 根据轮次版本id查询版本号
	 * @param turnVersion
	 * @return
	 */
	String getUutVersionId(String turnVersion);


	/**
	 * 根据任务ID查询被测对象实体
	 * @param projectId
	 * @return
	 */
	List<RunningCaseVO> getListByProjectId(@Param("projectId") String projectId);

	/**
	 * 根据测试用例id查询uutListId
	 * @param id
	 * @return
	 */
	@Select("SELECT rp.uut_list_id FROM running_case rc , running_task rt , running_project rp WHERE rc.test_task_id = rt.id AND rt.project_id = rp.id AND rc.id = #{id}")
	String getUutListIdByCaseId(@Param("id") String id);

	@Select("SELECT id FROM running_case WHERE test_code = #{testCode} AND test_task_id = #{taskId}")
	String getidByCaseCode(String testCode, String taskId);

	@Select("SELECT count(test_code) FROM running_case WHERE test_code = #{testCode}")
	Integer getNumOfTestCode(String testCode);

	@Select("SELECT count(*) FROM running_question WHERE case_id = #{caseId} AND del_flag = 0 AND file_status = 1")
	/**
	 * 根据测试用例id查询问题单归档数量
	 */
	Integer getFiledQuestionNum(String caseId);
}
