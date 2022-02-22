package org.jeecg.modules.task.service;

import java.util.List;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.vo.RunningTaskVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 任务管理
 * @Author: jeecg-boot
 * @Date:   2020-12-25
 * @Version: V1.0
 */
public interface IRunningTaskService extends IService<RunningTask> {
	
	
	/**
	 * 分页查询任务列表
	 * @param page true
	 * @param projectId true
	 * @param taskName true
	 * @param priority true
	 * @param taskCode true
	 * @param taskSoftName true
	 * @return 没有返回值
	 */
	Page<RunningTaskVO> queryPageList(Page<RunningTaskVO> page, String projectId, String taskName, String priority, String taskCode, String taskSoftName);

	/**
	 * 分页查询已归档测试项列表
	 * @param page true
	 * @param projectId true
	 * @param taskName true
	 * @param priority true
	 * @param taskCode true
	 * @param taskSoftName true
	 * @return 没有返回值
	 */
	Page<RunningTaskVO> queryFileTaskPageList(Page<RunningTaskVO> page, String projectId, String taskName, String priority, String taskCode, String taskSoftName);

	/**
	 * 根据任务ID获取采集结果路径
	 * @param id true
	 * @return 没有返回值
	 */
	String getCjUrlByTaskId(String id);
	/**
	 * 根据任务ID获取采集结果路径
	 * @param userName true
	 * @return 没有返回值
	 */
	String getRealName(String userName);
	/**
	 * 根据项目id查询任务信息
	 * @param projectId true
	 * @return List
	 */
	List<RunningTask> getTaskInfoByProjectId(String projectId);
	
	/**
	 * 根据任务ID进行逻辑删除
	 * @param id true
	 */
	void updateTask(String id);

	/**
	 * 根据项目ID查询uut_list_id
	 * @param projectId true
	 */
	List<String> getUutListId(String projectId);

	/**
	 * 根据uut_list_id查询user_id
	 * @param uutListId true
	 */
	List<String> getUserId(String uutListId);

	/**
	 * 根据userId查询realname
	 * @param userId
	 * @return
	 */
	String getUserRealName(String userId);

	/**
	 * 根据turnId查询turnNum
	 * @param turnId
	 * @return
	 */
	String getTurnNum(String turnId);



}
