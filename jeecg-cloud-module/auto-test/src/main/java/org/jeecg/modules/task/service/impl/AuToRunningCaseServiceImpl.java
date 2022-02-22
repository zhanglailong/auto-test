package org.jeecg.modules.task.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.project.entity.AutoRunningProject;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.jeecg.modules.scriptandplan.entity.AutoScriptPlan;
import org.jeecg.modules.scriptandplan.service.IAutoScriptPlanService;
import org.jeecg.modules.task.entity.*;
import org.jeecg.modules.task.mapper.AuToRunningCaseMapper;
import org.jeecg.modules.task.model.CaseTreeIdModel;
import org.jeecg.modules.task.service.*;
import org.jeecg.modules.task.vo.CaseTreeVO;
import org.jeecg.modules.task.vo.RunningCaseVO;
import org.jeecg.modules.task.vo.RunningQuestionVO;
import org.jeecg.modules.uut.entity.RunningUutList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * @Description: 测试用例表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Service
public class AuToRunningCaseServiceImpl extends ServiceImpl<AuToRunningCaseMapper, AuToRunningCase> implements IAuToRunningCaseService {
    @Autowired
    private IRunningQuestionService runningQuestionService;
    @Autowired
    private AuToRunningCaseMapper auToRunningCaseMapper;

    @Autowired
    private IRunningCaseStepService runningCaseStepService;
    @Autowired
    private IRunningCaseStepHistoryService runningCaseStepHistoryService;
    @Autowired
    private IRunningCaseHistoryService runningCaseHistoryService;
    @Autowired
    private IAuToRunningTaskService auToRunningTaskService;
    @Autowired
    private IAuToRunningCaseService auToRunningCaseService;
    @Autowired
    private IAutoScriptService autoScriptService;

    @Override
    public Page<RunningCaseVO> queryPageList(Page<RunningCaseVO> page, String testTaskId, String testName, String testCode, String testPerson, String testDate) {
        // TODO Auto-generated method stub
        return page.setRecords(auToRunningCaseMapper.getListData(testTaskId.split(","), testName, testCode,testPerson,testDate));
    }

    @Override
    public Page<RunningCaseVO> queryFileCasePageList(Page<RunningCaseVO> page,String testTaskId, String testName, String testCode, String testPerson,String testDate) {
        // TODO Auto-generated method stub
        List<RunningCaseVO> runningCaseVOList = auToRunningCaseMapper.getFileCaseListData(testTaskId.split(","), testName, testCode,testPerson,testDate);
        List<RunningCaseVO> runningCaseVOS = new ArrayList<>();
        if(runningCaseVOList.size() > 0 && runningCaseVOList != null){
            for(RunningCaseVO runningCaseVO : runningCaseVOList){
                runningCaseVO.setQuestionFiledNum(auToRunningCaseMapper.getFiledQuestionNum(runningCaseVO.getId()));
                runningCaseVOS.add(runningCaseVO);
            }
        }
        return page.setRecords(runningCaseVOS);
    }

    @Override
    public Page<RunningCaseVO> queryPageListByTestName(Page<RunningCaseVO> page, String testName) {
        // TODO Auto-generated method stub
        return page.setRecords(auToRunningCaseMapper.queryPageListByTestName(testName));
    }

    @Override
    public List<RunningCaseVO> queryPageListByTestId(String testId) {
        return auToRunningCaseMapper.queryPageListByTestId(testId.split(","));
    }

    @Override
    public List<AuToRunningCase> getTestCopyDataById(String caseId) {
        // TODO Auto-generated method stub
        return auToRunningCaseMapper.getTestCopyDataById(caseId.split(","));
    }

    @Override
    public List<RunningCase> getCopyDataById(String id) {
        // TODO Auto-generated method stub
        return auToRunningCaseMapper.getCopyDataById(id);
    }
    @Override
    public String getTestTemplateById(String id) {
        // TODO Auto-generated method stub
        return auToRunningCaseMapper.getTestTemplateById(id);
    }


    @Override
    public List<CaseTreeIdModel> queryTreeList(String templateId) {
        // 获取树形下拉列表数据
        List<CaseTreeVO> list = auToRunningCaseMapper.getTreeData(templateId);
        List<CaseTreeIdModel> treeList = new ArrayList<CaseTreeIdModel>();
        // 处理中的子特性
        String prePid = null;
        // 处理中的质量特性
        String preRootId = null;
        // 质量特性节点
        CaseTreeIdModel rootModel = null;
        // 子特性节点
        CaseTreeIdModel parentModel = null;

        for (CaseTreeVO caseTreeVO : list) {
            String pid = caseTreeVO.getParentId();
            String rootId = caseTreeVO.getRootId();
            // 初期设定：将处理中的质量特性ID和子特性ID记录下来
            if (preRootId == null && prePid == null) {
                prePid = pid;
                preRootId = rootId;
                rootModel = new CaseTreeIdModel();
                rootModel.setTitle(caseTreeVO.getRootName());
                rootModel.setKey(rootId);
                rootModel.setValue(rootId);
                parentModel = new CaseTreeIdModel();
                parentModel.setTitle(caseTreeVO.getParentName());
                parentModel.setKey(pid);
                parentModel.setValue(pid);
            }
            // 比较处理中的质量特性ID和当前质量特性ID
            if (preRootId.equals(rootId)) {
                // 比较处理中的子特性ID和当前子特性ID
                if (prePid.equals(pid)) {
                    // 相同的情况下，添加度量节点
                    CaseTreeIdModel childModel = new CaseTreeIdModel();
                    childModel.setTitle(caseTreeVO.getName());
                    childModel.setKey(caseTreeVO.getId());
                    childModel.setValue(caseTreeVO.getId());
                    parentModel.getChildren().add(childModel);
                } else {
                    // 不相同的情况下，将处理中的子特性信息添加到处理中的质量特性的节点下方
                    rootModel.getChildren().add(parentModel);
                    // 重新初始化子特性ID
                    prePid = pid;
                    // 创建子特性节点
                    parentModel = new CaseTreeIdModel();
                    parentModel.setTitle(caseTreeVO.getParentName());
                    parentModel.setKey(pid);
                    parentModel.setValue(pid);
                    // 添加度量节点
                    CaseTreeIdModel childModel = new CaseTreeIdModel();
                    childModel.setTitle(caseTreeVO.getName());
                    childModel.setKey(caseTreeVO.getId());
                    childModel.setValue(caseTreeVO.getId());
                    parentModel.getChildren().add(childModel);
                }
            } else {
                // 更换了质量特性ID的时候，将处理中的子特性信息添加到处理中的质量特性的节点下方
                rootModel.getChildren().add(parentModel);
                treeList.add(rootModel);
                // 重新初始化处理中的质量特性ID和当前质量特性ID
                preRootId = rootId;
                prePid = pid;
                // 创建质量特性节点
                rootModel = new CaseTreeIdModel();
                rootModel.setTitle(caseTreeVO.getRootName());
                rootModel.setKey(rootId);
                rootModel.setValue(rootId);
                // 创建子特性节点
                parentModel = new CaseTreeIdModel();
                parentModel.setTitle(caseTreeVO.getParentName());
                parentModel.setKey(pid);
                parentModel.setValue(pid);
                // 添加度量节点
                CaseTreeIdModel childModel = new CaseTreeIdModel();
                childModel.setTitle(caseTreeVO.getName());
                childModel.setKey(caseTreeVO.getId());
                childModel.setValue(caseTreeVO.getId());
                parentModel.getChildren().add(childModel);
            }
        }
        // 将最后的处理的子特性信息添加到处理中的质量特性的节点下方
        rootModel.getChildren().add(parentModel);
        treeList.add(rootModel);

        return treeList;
    }
    @Override
    public List<RunningCase> getCaseNameByTaskId(String taskId) {
        // TODO Auto-generated method stub
        return auToRunningCaseMapper.getCaseNameByTaskId(taskId);
    }

    @Override
    public List<RunningCase> getRunningCaseList(String taskId) {
        return auToRunningCaseMapper.getRunningCaseList(taskId);
    }

    @Override
    public List<AuToRunningTask> getCasesTree(String projectId) {
        //查询测试项列表
        List<AuToRunningTask> auToRunningTaskLis = auToRunningTaskService.list(new QueryWrapper<AuToRunningTask>()
                .eq(CommonConstant.DATA_STRING_DEL_FLAG, CommonConstant.DATA_INT_IDEL_0)
                .eq(CommonConstant.DATA_STRING_PROJECT_ID,projectId));
        //查询测试用例列表
        List<AuToRunningCase> auToRunningCaseList = auToRunningCaseService.list(new QueryWrapper<AuToRunningCase>()
                .eq(CommonConstant.DATA_STRING_DEL_FLAG, CommonConstant.DATA_INT_IDEL_0));

        auToRunningTaskLis.stream().peek(AuToRunningTask -> AuToRunningTask.setChildren(
                getChildrenCaseTree(AuToRunningTask,auToRunningCaseList)
        )).collect(Collectors.toList());
        return  auToRunningTaskLis;
    }

    @Override
    public List<AuToRunningTask> getResultTree(String projectId) {
        //查询测试项列表
        List<AuToRunningTask> auToRunningTaskLis = auToRunningTaskService.list(new QueryWrapper<AuToRunningTask>()
                .eq(CommonConstant.DATA_STRING_DEL_FLAG, CommonConstant.DATA_INT_IDEL_0)
                .eq(CommonConstant.DATA_STRING_PROJECT_ID,projectId));
        //查询测试用例列表
        List<AuToRunningCase> auToRunningCaseList = auToRunningCaseService.list(new QueryWrapper<AuToRunningCase>()
                .eq(CommonConstant.DATA_STRING_DEL_FLAG, CommonConstant.DATA_INT_IDEL_0));
        //查询所有测试用例下的脚本
        List<AutoScript> autoScriptList = autoScriptService.list(new QueryWrapper<AutoScript>().eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0));

        //放入数据
        auToRunningTaskLis.stream().peek(AuToRunningTask -> AuToRunningTask.setChildren(
                getChildrenCase(AuToRunningTask,auToRunningCaseList,autoScriptList)
        )).collect(Collectors.toList());
        return  auToRunningTaskLis;
    }

    //获取测试项->测试用例
    private static List<AuToRunningCase> getChildrenCaseTree(AuToRunningTask auToRunningTask, List<AuToRunningCase> auToRunningCaseList) {
        return auToRunningCaseList.stream()
                .filter(auToRunningCase -> auToRunningCase.getTestTaskId().equals(auToRunningTask.getId())).collect(Collectors.toList());
    }

    //获取测试项->测试用例->用例下的脚本
    private static List<AuToRunningCase> getChildrenCase(AuToRunningTask auToRunningTask, List<AuToRunningCase> auToRunningCaseList,List<AutoScript> autoScriptList) {
        return auToRunningCaseList.stream()
                .filter(auToRunningCase -> auToRunningCase.getTestTaskId().equals(auToRunningTask.getId())).peek(auToRunningCase -> auToRunningCase.setChildren(getChildrenScript(auToRunningCase,autoScriptList))).collect(Collectors.toList());
    }

    private static List<AutoScript> getChildrenScript(AuToRunningCase auToRunningCase, List<AutoScript> autoScriptList) {
        return autoScriptList.stream()
                .filter(autoScript -> autoScript.getTestCaseId().equals(auToRunningCase.getId())).collect(Collectors.toList());
    }


    @Override
    public List<RunningCaseVO> getRunningCaseData(String testTaskId) {
        // TODO Auto-generated method stub
        return auToRunningCaseMapper.getRunningCaseData(testTaskId);
    }

    @Override
    public List<RunningCaseVO> getRunningCaseData1(String testTaskId) {
        // TODO Auto-generated method stub
        return auToRunningCaseMapper.getRunningCaseData1(testTaskId);
    }

    @Override
    public void updateCase(String id) {
        //逻辑删除测试用例信息
        AuToRunningCase cases= new AuToRunningCase();
        String caseId = id;
        cases.setId(caseId);
        cases.setDelFlag(1);
        this.updateById(cases);
        //查询测试用例下所有问题信息
        List<RunningQuestionVO> questions = runningQuestionService.getRunningQuestionData(caseId);
        if(questions!=null) {
            for(int i=0;i<questions.size();i++) {
                //逻辑删除测试用例下所有问题信息
                RunningQuestion question = new RunningQuestion();
                String questionId = questions.get(i).getId();
                question.setId(questionId);
                question.setDelFlag(1);
                runningQuestionService.updateById(question);
            }
        }


    }

    @Override
    public RunningTask getProjectIdByTaskId(String taskId) {
        // TODO Auto-generated method stub
        return auToRunningCaseMapper.getProjectIdByTaskId(taskId);
    }

    @Override
    public RunningUutList getUutListByCaseId(String caseId) {
        return auToRunningCaseMapper.getUutListByCaseId(caseId);
    }

    /**
     * 关闭输入流
     * @param is
     */
    private static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导入word （doc格式）
     * @param file
     * @return
     */
    private Map<String,String> importWordToDoc (MultipartFile file){
        InputStream inputStream = null;
        Map<String, String> result = new HashMap<>(30);
        try {
            //载入文档
            inputStream = file.getInputStream();
            HWPFDocument hwpf = new HWPFDocument(inputStream);
            //得到文档的读取范围
            Range range = hwpf.getRange();
            TableIterator it = new TableIterator(range);
            int index = 1;
            String value = "";
            String key = "";
            // 迭代文档中的表格
            while (it.hasNext()) {
                Table tb = (Table) it.next();
                for (int i = 0; i < tb.numRows(); i++) {
                    TableRow tr = tb.getRow(i);
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int j = 0; j < tr.numCells(); j++) {
                        //取得单元格
                        TableCell td = tr.getCell(j);
                        //取得单元格的内容
                        for (int k = 0; k < td.numParagraphs(); k++) {
                            Paragraph para = td.getParagraph(k);
                            Optional<String> text1 = Optional.ofNullable(para.text());
                            String text = text1.orElse("");
                            text = text.replaceAll("\r", "");
                            text = text.replaceAll("\r\n", "");
                            text = text.replaceAll("\\u0007", "");
                            if (index % 2 != 0) {
                                key = text;
                            } else {
                                value = text;
                                stringBuffer.append(key).append(":").append(value);
                                result.put(key, value);
                            }
                            index++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(inputStream);
        }
        return result;
    }

    /**
     * 导入word （docx格式）
     * @param file
     * @return
     */

    @Override
    public  Map<String,String> importWordToDocx (MultipartFile file, String testTaskId) {
        InputStream inputStream = null;
        Map<String,String> result = new HashMap<>(2000);
        List<String> result2 = new ArrayList<>(2000);
        List<String> number = new ArrayList<>();
        try {
            int index = 1;
            String value = "";
            String key = "";
            InputStream inputStream1 = file.getInputStream();
            XWPFDocument xdoc = new XWPFDocument(inputStream1);
            //获取文档中所有的表格
            //得到word中的表格
            Iterator<XWPFTable> it = xdoc.getTablesIterator();
            while (it.hasNext()) {
                //获取文档所有表格
                XWPFTable table = it.next();
                List<XWPFTableRow> rows = table.getRows();
                result.clear();
                result2.clear();
                number.clear();
                //读取每一行数据
                for (int i = 0; i < rows.size(); i++) {
                    XWPFTableRow row = rows.get(i);
                    //读取每一列数据
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (int j = 0; j < cells.size(); j++) {
                        XWPFTableCell cell = cells.get(j);
                        String str = cell.getText();
                        str = str.replaceAll("\r", "");
                        str = str.replaceAll("\r\n", "");
                        str = str.replaceAll("\\u0007", "");
                        //判断筛选表格
                        if(Arrays.asList("测试用例名称","标   识","追踪关系","测试用例综述", "用例初始化","前提和约束","设计方法","测试用例终止条件","测试用例通过准则","用例设计人").contains(str)){
                            result.put(str,cells.get(j + 1).getText());
                        }
                        if (i >= 8 && cells.size() == 3) {
                            if (j == 0){ number.add(str); }
                            if (j == 1) { result2.add(str); }
                            if(j == 2){ result2.add(str); }
                        }
                    }
                }
                //存入数据库
                if (result.size() == 0){ continue; }
                RunningUutList runningUutList = this.getBaseMapper().getUutListByTaskId(testTaskId);
                AuToRunningCase runningCase = new AuToRunningCase(testTaskId,result.get("测试用例名称"),result.get("标   识"),result.get("追踪关系"),result.get("测试用例综述"),result.get("用例初始化"),result.get("前提和约束"),result.get("设计方法"),result.get("测试用例终止条件"),result.get("测试用例通过准则"),result.get("用例设计人"),runningUutList.getUutVersion());
                auToRunningCaseMapper.insert(runningCase);

                RunningCaseHistory runningCaseHistory=new RunningCaseHistory();
                BeanUtils.copyProperties(runningCase,runningCaseHistory);
                runningCaseHistory.setId(null);
                runningCaseHistory.setTestTaskId(runningCase.getTestTaskId());
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                runningCaseHistory.setUpdateBy(sysUser.getId());
                runningCaseHistory.setOperationType("0");
                runningCaseHistory.setUpdateTime(new Date());
                runningCaseHistory.setMainId(runningCase.getId());
                runningCaseHistory.setTestVersion(runningCase.getTestVersion());
                runningCaseHistoryService.save(runningCaseHistory);

                final AtomicInteger k = new AtomicInteger(0);
                List<RunningCaseStep> caseStepList = number.stream().map(item->{
                    RunningCaseStep runningCaseStep = new RunningCaseStep();
                    runningCaseStep.setCaseId(runningCase.getId());
                    runningCaseStep.setStepId(item);
                    for (int j = 0 ;j < result2.size();j++) {
                        if (j % 2 == 0) { runningCaseStep.setOperatingInstructions(result2.get(j+k.intValue())); }
                        else { runningCaseStep.setExpectResult(result2.get(j+k.intValue()));break; }
                    }
                    k.addAndGet(2);
                    return runningCaseStep;
                }).collect(Collectors.toList());
                //runningCaseHistoryService.saveBatch();
                runningCaseStepService.saveBatch(caseStepList);
                //存历史记录
                List<RunningCaseStepHistory> runningCaseStepHistoryList = caseStepList.stream().map(i-> {
                    RunningCaseStepHistory runningCaseStepHistory = new RunningCaseStepHistory();
                    BeanUtils.copyProperties(i, runningCaseStepHistory);
                    runningCaseStepHistory.setOpType("0");
                    return runningCaseStepHistory;
                }).collect(Collectors.toList());
                runningCaseStepHistoryService.saveBatch(runningCaseStepHistoryList);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(inputStream);
        }
        return result;
    }
    /**
     * 新增
     * @param result
     */
    private void insert(Map<String, String> result){
        AuToRunningCase runningCase = new AuToRunningCase();
        runningCase.setId(result.get("id"));
        runningCase.setTestTaskId(result.get("test_task_id"));
        runningCase.setTestName(result.get("test_name"));
        runningCase.setTestType(result.get("test_type"));
        runningCase.setTestInstructions(result.get("test_instructions"));
        runningCase.setDelFlag(0);
        auToRunningCaseMapper.insert(runningCase);
    }

    /**
     * 根据任务ID查询被测对象实体
     */
    public RunningUutList getUutListByTaskId(String taskId){
        return auToRunningCaseMapper.getUutListByTaskId(taskId);
    }

    /**
     * 根据轮次版本id查询版本号
     * @param turnVersion
     * @return
     */
    @Override
    @DS("uutDatabase")
    public String getUutVersionId(String turnVersion){
        return auToRunningCaseMapper.getUutVersionId(turnVersion);
    }


    public Page<RunningCaseVO> getListByProjectId(Page<RunningCaseVO> page,String projectId){
        return page.setRecords(auToRunningCaseMapper.getListByProjectId(projectId));
    }

    @Override
    public String getUutListIdByCaseId(String id) {
        return auToRunningCaseMapper.getUutListIdByCaseId(id);
    }

    @Override
    public String getidByCaseCode(String testCode, String taskId) {
        return auToRunningCaseMapper.getidByCaseCode(testCode, taskId);
    }

    @Override
    public Integer getNumOfTestCode(String testCode) {
        return auToRunningCaseMapper.getNumOfTestCode(testCode);
    }

    @Override
    public RunningCaseVO getRunningCaseVoByCaseId(String caseId){
        return auToRunningCaseMapper.getRunningCaseVoByCaseId(caseId);
    }
}
