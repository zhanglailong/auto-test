package org.jeecg.modules.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.project.entity.RunningProjectHistory;
import org.jeecg.modules.project.entity.RunningProjectTurn;
import org.jeecg.modules.project.vo.RunningProjectInfo;
import org.jeecg.modules.project.vo.RunningProjectVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Mapper
public interface IRunningProjectService extends IService<RunningProject> {

	/**
	 * 根据字段查唯一实体
	 * @param fieldname true
	 * @param value true
	 * @return 没有返回值
	 */
	public RunningProject findUniqueBy(String fieldname, String value);

	/**
	 * 根据项目id,查询项目和被测对象数据
	 * @param id true
	 * @return 没有返回值
	 */
	List<RunningProjectInfo> getListDataById(String id);


	/**
	 * 分页查询项目列表
	 * @param page true
	 * @param projectId true
	 * @param projectName true
	 * @param projectCode true
	 * @param createTime true
	 * @return 没有返回值
	 */
	Page<RunningProjectInfo> queryPageList(Page<RunningProjectInfo> page, String projectName, String projectCode, String createTime, String projectId);

	/**
	 * 分页查询归档项目列表
	 * @param page true
	 * @param projectId true
	 * @param projectName true
	 * @param projectCode true
	 * @param createTime true
	 * @return 没有返回值
	 */
	Page<RunningProjectInfo> queryFileProjectPageList(Page<RunningProjectInfo> page, String projectName, String projectCode, String createTime, String projectId);
	/**
	 * 根据项目ID查询项目成员
	 * @param id true
	 * @return name
	 */
	List<RunningProject> getNameByProjectId(String id);

	/**
	 * 获取总项目数
	 * @return 没有返回值
	 */
	Integer getProjectNums();

	/**
	 * 获取已出库项目信息接口
	 * @return 没有返回值
	 */
	List<RunningProject> getProjectInfo();

	/** dq add
	 * 根据项目id查询 任务数量  测试用例数量  问题数量
	 * @param projectId true
	 * @return 没有返回值
	 */
	Map<String,Object> getRelatedCount(String projectId);

	/**
	 * 根据项目ID逻辑删除项目信息、任务信息、测试用例信息、问题信息
	 * @param id true
	 * @return 没有返回值
	 */
	void updateProject(String id);

	/**
	 * 操作历史记录查询
	 * @param page true
	 * @param params true
	 * @return 没有返回值
	 */
	IPage<Map<String,Object>> getOperationHistoryList(Page page, RunningProjectHistory params);

	/**
	 * 根据用户id返回用户realname,用户id逗号分隔
	 * @param userIds true
	 * @return 没有返回值
	 */
	String getUsernamesByIds(Object userIds);

	/**
	 * 我的审批查询
	 * @param page true
	 * @return 没有返回值
	 */
	IPage<RunningProjectVo> queryList(Page page);

	/**获取ProjectId
	 * @param projectId true
	 * @return 没有发返回值
	 * */
	String getProjectName(String projectId);

	/**
	 * 根据项目名称查询项目id
	 * @param projectCode
	 * @return
	 */
	String getProjectId(String projectCode);

	/**获取轮次
	 * @param projectId true
	 * @return 没有返回值
	 * */
	List<RunningProjectTurn> getTurn(String projectId);

	/**获取轮次
	 * @param uutId true
	 * @return 没有返回值
	 * */
	List<Map<String, String>> getAllVersion(String uutId);

	/**获取轮次
	 * @param projectId true
	 * @return 没有返回值
	 * */
	List<Map<String, String>> getRoleToUser(String projectId);

	// 下拉选层级列表
	List<DictModel> getOptionByCondition(String projectId, String turnId);

	// 下拉选层级列表
	List<DictModel> getOptionByConditionByTaskId(String taskId, String turnId);
	/**获取轮次
	 * @param userName true
	 * @return 没有返回值
	 * */
	List<String> getUserIdList(String userName);

	// 下拉选层级列表
	List<DictModel> getUutNameByStatus();

	/**
	 * 将归档时未选择的测试项进行逻辑删除且记录到测试项履历表中
	 * @param projectIds
	 * @param taskIds
	 */
	public void signFileTaskData(String projectIds, String taskIds);

	List<RunningProject> getRunningProjectList(String ids);

}
