package org.jeecg.modules.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.task.entity.AuToRunningTask;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.vo.RunningTaskVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 任务管理
 * @Author: jeecg-boot
 * @Date:   2021-11-18
 * @Version: V1.0
 */
@Repository
public interface AuToRunningTaskMapper extends BaseMapper<AuToRunningTask> {
    /**
     * 获取数据列表
     * @param page true
     * @param projectId true
     * @param taskName true
     * @param priority true
     * @param taskCode true
     * @param taskSoftName true
     * @return  List<RunningQuestionManageVO>
     * */
    List<RunningTaskVO> getListData(Page<RunningTaskVO> page, @Param("projectId") String projectId, @Param("taskName") String taskName, @Param("priority") String priority, @Param("taskCode") String taskCode, @Param("taskSoftName") String taskSoftName);

    /**
     * 获取已归档测试项数据列表
     * @param page true
     * @param projectId true
     * @param taskName true
     * @param priority true
     * @param taskCode true
     * @param taskSoftName true
     * @return  List<RunningQuestionManageVO>
     * */
    List<RunningTaskVO> getFileTaskListData(Page<RunningTaskVO> page, @Param("projectId") String projectId, @Param("taskName") String taskName, @Param("priority") String priority, @Param("taskCode") String taskCode, @Param("taskSoftName") String taskSoftName);


    @Select("SELECT\n"
            + "	sjwjurl\n"
            + "FROM\n"
            + "	collection_data_management\n"
            + "WHERE\n"
            + "	task_id = #{id}\n"
            + "ORDER BY\n"
            + "	create_time DESC\n"
            + "LIMIT 1")
    /**
     * 获取cjur
     * @param id true
     * @return 没有返回值
     * */
    String getCjUrlByTaskId(String id);

    @Select("SELECT\n"
            + "	realname\n"
            + "FROM\n"
            + "	sys_user\n"
            + "WHERE\n"
            + "	username = #{userName}")
    /**
     * 获取真实名称
     * @param userName true
     * @return 没有返回值
     * */
    String getRealName(String userName);

    @Select("SELECT "
            + "*"
            + " FROM running_task"
            + " WHERE project_id=#{projectId}"
            + " and del_flag=0 ")
    /**
     * 获取任务信息
     * @param projectId true
     * @return List<RunningTask>
     * */
    List<AuToRunningTask> getTaskInfoByProjectId(String projectId);

    @Select("SELECT "
            + " id,\n"
            + "	task_name,\n"
            + "	task_code\n"
            + " FROM running_task"
            + " WHERE ${field}=#{value}"
            + " and del_flag=0 ")
    /**
     * 获取任务信息
     * @param projectId true
     * @return List<RunningTask>
     * */
    List<AuToRunningTask> selectByField(String field, String value);


    //	@Select(" SELECT DISTINCT a.uut_list_id FROM running_project a,running_task b WHERE a.id = b.project_id AND a.id = #{projectId} ")
    @Select(" SELECT DISTINCT uut_list_id FROM running_project WHERE id = #{projectId} ")
    /**
     * 根据项目ID查询uut_list_id
     * @param projectId
     * @return
     */
    List<String> getUutListId(@Param("projectId") String projectId);

    @Select(" SELECT user_id FROM running_uut_list_user WHERE uut_id = #{uutListId} ")
    /**
     * 根据uut_list_id查询user_id
     * @param uutListId
     * @return
     */
    List<String> getUserId(String uutListId);


    @Select(" SELECT realname FROM sys_user WHERE id = #{userId} ")
    /**
     * 根据userId查询realname
     * @param userId
     * @return
     */
    String getUserRealName(String userId);

    @Select(" select turn_num from running_project_turn where id = #{turnId} ")
    /**
     * 根据turnId查询turnNum
     * @param turnId
     * @return
     */
    String getTurnNum(@Param("turnId") String turnId);

    @Select("SELECT count(*) FROM running_case WHERE test_task_id = #{taskId} AND del_flag = 0 AND file_status = 1")
    /**
     * 根据测试项id查询下属已归档测试用例个数
     * @param taskId
     * @return
     */
    Integer getCaseFiledNum(String taskId);
}
