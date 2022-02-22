package org.jeecg.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.project.entity.AutoRunningProject;
import org.jeecg.modules.project.entity.RunningProjectHistory;
import org.jeecg.modules.project.vo.RunningProjectVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 自动化项目管理
 * @Author: jeecg-boot
 * @Date:   2021-11-16
 * @Version: V1.0
 */
@Repository
public interface AutoRunningProjectMapper extends BaseMapper<AutoRunningProject> {
    /**
     * 查看文件
     * @param fieldname true
     * @param value true
     * @return 没有返回值
     * */
    public AutoRunningProject findUniqueBy(@Param("fieldname") String fieldname, @Param("value") String value);

    @Select("SELECT\n"
            + "	project_members\n"
            + "FROM\n"
            + "	running_project\n"
            + "WHERE id = #{id} ")
    /**
     * 获取名称
     * @param id true
     * @return List<RunningProject>
     * */
    List<AutoRunningProject> getNameByProjectId(String id);

    @Select("SELECT\n"
            + "	IFNULL(count(1), 0) as pNums\n"
            + "FROM\n"
            + "	running_project"
            + " where del_flag=0")
    /**
     * 获取项目编号
     * @return 没有返回值
     * */
    Integer getProjectNums();

    @Select("SELECT id,project_name,project_code "
            + "FROM running_project"
            + " WHERE project_state='2' and del_flag=0 ")
    /**
     * 获取任务信息
     * @param id true
     * @return List<RunningProject>
     * */
    List<AutoRunningProject> getProjectInfo();

    /**
     * 查询任务到期且没有结束的任务列表
     * @return List<RunningProject>
     * */
    List<AutoRunningProject> getNotFinishProjectList();

    /**
     * 查询操作历史记录
     * @param params true
     * @param page page
     * @return IPage集合
     * */
    IPage<Map<String,Object>> getOperationHistoryList(Page page, @Param("params") RunningProjectHistory params);

    /**
     * 我的审批查询
     * @param page true
     * @return Page<RunningProjectVo>
     * */
    Page<RunningProjectVo> queryList(Page page);

    /**
     * 我的审批查询
     * @param projectId true
     * @return List<Map<String,String>> getRoleToUser
     * */
    List<Map<String,String>> getRoleToUser(String projectId);

    /**
     * 我的审批查询
     * @param userName true
     * @return List<String>
     * */
    @Select("SELECT id " +
            "FROM sys_user " +
            "WHERE del_flag = 0 AND username like CONCAT('%',#{userName},'%')")
    List<String> getUserIdList(String userName);
}
