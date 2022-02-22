package org.jeecg.modules.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.project.entity.AutoRunningProject;
import org.jeecg.modules.task.entity.AuToRunningCase;
import org.jeecg.modules.task.entity.AuToRunningTask;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.model.CaseTreeIdModel;
import org.jeecg.modules.task.vo.RunningCaseVO;
import org.jeecg.modules.uut.entity.RunningUutList;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Description: 测试用例表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
public interface IAuToRunningCaseService extends IService<AuToRunningCase> {
    /**
     * 分页查询测试用例列表
     * @param page true
     * @param testTaskId true
     * @param testName true
     * @param testCode true
     * @param testPerson true
     * @param testDate true
     * @return 没有返回值
     */
    Page<RunningCaseVO> queryPageList(Page<RunningCaseVO> page, String testTaskId, String testName, String testCode, String testPerson, String testDate);

    /**
     * 分页查询已归档测试用例列表
     * @param page true
     * @param testTaskId true
     * @param testName true
     * @param testCode true
     * @param testPerson true
     * @param testDate true
     * @return 没有返回值
     */
    Page<RunningCaseVO> queryFileCasePageList(Page<RunningCaseVO> page, String testTaskId, String testName, String testCode, String testPerson, String testDate);

    /**
     * 分页查询测试用例列表
     * @param page true
     * @param testName true
     * @return 没有返回值
     */
    Page<RunningCaseVO> queryPageListByTestName(Page<RunningCaseVO> page, String testName);

    /**
     * 根据测试用例id查询数据
     * @param testId true
     * @return List<RunningCaseVO>
     */
    List<RunningCaseVO> queryPageListByTestId(String testId);

    /**
     * 查询用例复用所需数据
     * @param caseId true
     * @return 没有返回值
     */
    List<AuToRunningCase> getTestCopyDataById(String caseId);


    /**
     * 查询复制所需数据
     * @param id true
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
     * 根据任务ID查询测试用例名称
     * @param taskId  true
     * @return 测试用例名称
     *
     */
    List<RunningCase> getCaseNameByTaskId(String taskId);

    /**
     * 根据任务ID查询出所有测试用例信息
     * @param testTaskId true
     * @return 没有返回值
     */
    List<RunningCaseVO> getRunningCaseData(String testTaskId);


    /**
     * 根据任务ID查询出所有测试用例信息
     * @param testTaskId true
     * @return List<RunningCaseVO>
     */
    List<RunningCaseVO> getRunningCaseData1(String testTaskId);

    /**
     * 根据测试用例ID进行逻辑删除,删除测试用例信息、问题信息
     * @param id true
     */
    void updateCase(String id);

    /**
     * 根据任务ID查询项目ID
     * @param taskId true
     * @return projectId
     */
    RunningTask getProjectIdByTaskId(String taskId);

    /**
     * 根据测试用例ID查询被测对象实体
     * @param caseId true
     * @return projectId
     */
    RunningUutList getUutListByCaseId(String caseId);

    /**
     * 根据任务ID查询被测对象实体
     * @param taskId true
     * @return projectId
     */
    RunningUutList getUutListByTaskId(String taskId);

    /**
     * 根据轮次版本id查询版本号
     * @param turnVersion
     * @return
     */
    String getUutVersionId(String turnVersion);

    /**
     * word导入
     * @param file true
     * @param testTaskId true
     * @return 没有返回值
     */
    Map<String,String> importWordToDocx(MultipartFile file, String testTaskId);


    /**
     *  根据项目id查询该项目下所有测试用例
     * @param projectId
     * @return
     */
    Page<RunningCaseVO> getListByProjectId(Page<RunningCaseVO> page, String projectId);

    /**
     *  根据测试用例id查询uutListId
     * @param id
     * @return String
     */
    String getUutListIdByCaseId(String id);

    /**
     * 根据测试用例标识查询id
     * @param testCode
     * @param taskId
     * @return String
     */
    String getidByCaseCode(String testCode, String taskId);

    /**
     * 根据测试用例标识查询有无同名标识
     * @param testCode
     * @return Integer
     */
    Integer getNumOfTestCode(String testCode);

    /**
     * 根据测试测试项id查询测试用例
     * @param taskId 测试项id
     * @return list
     */
    List<RunningCase> getRunningCaseList(String taskId);

    List<AuToRunningTask> getCasesTree(String projectId);


    List<AuToRunningTask> getResultTree(String projectId);

    /**
     * 根据测试用例id查询测试用例
     * @param caseId 测试用例id
     * @return 测试用例
     */
    RunningCaseVO getRunningCaseVoByCaseId(String caseId);
}
