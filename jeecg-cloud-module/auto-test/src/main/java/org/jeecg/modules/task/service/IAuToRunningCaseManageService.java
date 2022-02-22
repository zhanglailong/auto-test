package org.jeecg.modules.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.model.CaseTreeIdModel;
import org.jeecg.modules.task.vo.RunningCaseManageVO;

import java.util.List;

public interface IAuToRunningCaseManageService extends IService<RunningCase> {
    /**
     * 分页查询测试用例列表
     * @param page true
     * @param testTaskId true
     * @param projectId true
     * @param testName true
     * @param testCode true
     * @param testPerson true
     * @param testDate true
     * @return 没有返回值
     */
    Page<RunningCaseManageVO> queryPageList(Page<RunningCaseManageVO> page, String testTaskId, String projectId, String testName, String testCode, String testPerson, String testDate);





    /**
     * 查询复制所需数据
     * @param id
     * @return 没有返回值
     */
    List<RunningCase> getCopyDataById(String id);

    /**
     * 根据任务id,查询被测对象分析体系id
     * @param id true
     * @return 分析体系id
     */
    String getTestTemplateById(String id);

    /**
     * 获取测试用例树形结构数据
     * @param templateId true
     * @return 树形结构数据
     */
    List<CaseTreeIdModel> queryTreeList(String templateId);

    /**
     * 获取要导出测试用例管理表数据
     * @return 没有返回值
     */
    List<RunningCaseManageVO> getRunningCaseManageData();

    /**
     * 获取要导出测试用例管理表数据
     * @return RunningCaseManageVO
     */
    List<RunningCaseManageVO> getRunningCaseManageData1();

    /**
     * 根据任务ID查询项目ID、项目名称
     * @param taskId true
     * @return 没有返回值
     */
    RunningProject getProjectNameByTaskId(String taskId);

    /**
     * 根据测试用例ID进行逻辑删除,删除测试用例信息、问题信息
     * @param id true
     */
    void updateCaseManage(String id);

    /**
     * 根据任务ID查询项目ID
     * @param taskId true
     * @return 没有返回值
     */
    RunningTask getProjectIdByTaskId(String taskId);
}
