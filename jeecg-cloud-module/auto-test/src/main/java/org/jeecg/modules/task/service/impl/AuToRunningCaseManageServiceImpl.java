package org.jeecg.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.mapper.AuToRunningCaseManageMapper;
import org.jeecg.modules.task.model.CaseTreeIdModel;
import org.jeecg.modules.task.service.IAuToRunningCaseManageService;
import org.jeecg.modules.task.service.IRunningQuestionService;
import org.jeecg.modules.task.vo.CaseTreeVO;
import org.jeecg.modules.task.vo.RunningCaseManageVO;
import org.jeecg.modules.task.vo.RunningQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class AuToRunningCaseManageServiceImpl extends ServiceImpl<AuToRunningCaseManageMapper, RunningCase> implements IAuToRunningCaseManageService {
    @Autowired
    private IRunningQuestionService runningQuestionService;
    @Autowired
    private AuToRunningCaseManageMapper auToRunningCaseManageMapper;

    @Override
    public Page<RunningCaseManageVO> queryPageList(Page<RunningCaseManageVO> page, String testTaskId, String projectId, String testName, String testCode, String testPerson, String testDate) {
        // TODO Auto-generated method stub
        return page.setRecords(auToRunningCaseManageMapper.getListData(testTaskId.split(","),projectId,testName, testCode,testPerson,testDate));
    }

    @Override
    public List<RunningCase> getCopyDataById(String id) {
        // TODO Auto-generated method stub
        return auToRunningCaseManageMapper.getCopyDataById(id);
    }
    @Override
    public String getTestTemplateById(String id) {
        // TODO Auto-generated method stub
        return auToRunningCaseManageMapper.getTestTemplateById(id);
    }


    @Override
    public List<CaseTreeIdModel> queryTreeList(String templateId) {
        // ??????????????????????????????
        List<CaseTreeVO> list = auToRunningCaseManageMapper.getTreeData(templateId);
        List<CaseTreeIdModel> treeList = new ArrayList<CaseTreeIdModel>();

        // ?????????????????????
        String prePid = null;
        // ????????????????????????
        String preRootId = null;
        // ??????????????????
        CaseTreeIdModel rootModel = null;
        // ???????????????
        CaseTreeIdModel parentModel = null;

        for (CaseTreeVO caseTreeVO : list) {
            String pid = caseTreeVO.getParentId();
            String rootId = caseTreeVO.getRootId();
            // ??????????????????????????????????????????ID????????????ID????????????
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
            // ??????????????????????????????ID?????????????????????ID
            if (preRootId.equals(rootId)) {
                // ???????????????????????????ID??????????????????ID
                if (prePid.equals(pid)) {
                    // ???????????????????????????????????????
                    CaseTreeIdModel childModel = new CaseTreeIdModel();
                    childModel.setTitle(caseTreeVO.getName());
                    childModel.setKey(caseTreeVO.getId());
                    childModel.setValue(caseTreeVO.getId());
                    parentModel.getChildren().add(childModel);
                } else {
                    // ??????????????????????????????????????????????????????????????????????????????????????????????????????
                    rootModel.getChildren().add(parentModel);
                    // ????????????????????????ID
                    prePid = pid;
                    // ?????????????????????
                    parentModel = new CaseTreeIdModel();
                    parentModel.setTitle(caseTreeVO.getParentName());
                    parentModel.setKey(pid);
                    parentModel.setValue(pid);
                    // ??????????????????
                    CaseTreeIdModel childModel = new CaseTreeIdModel();
                    childModel.setTitle(caseTreeVO.getName());
                    childModel.setKey(caseTreeVO.getId());
                    childModel.setValue(caseTreeVO.getId());
                    parentModel.getChildren().add(childModel);
                }
            } else {
                // ?????????????????????ID??????????????????????????????????????????????????????????????????????????????????????????
                rootModel.getChildren().add(parentModel);
                treeList.add(rootModel);
                // ???????????????????????????????????????ID?????????????????????ID
                preRootId = rootId;
                prePid = pid;
                // ????????????????????????
                rootModel = new CaseTreeIdModel();
                rootModel.setTitle(caseTreeVO.getRootName());
                rootModel.setKey(rootId);
                rootModel.setValue(rootId);
                // ?????????????????????
                parentModel = new CaseTreeIdModel();
                parentModel.setTitle(caseTreeVO.getParentName());
                parentModel.setKey(pid);
                parentModel.setValue(pid);
                // ??????????????????
                CaseTreeIdModel childModel = new CaseTreeIdModel();
                childModel.setTitle(caseTreeVO.getName());
                childModel.setKey(caseTreeVO.getId());
                childModel.setValue(caseTreeVO.getId());
                parentModel.getChildren().add(childModel);
            }
        }
        // ????????????????????????????????????????????????????????????????????????????????????
        rootModel.getChildren().add(parentModel);
        treeList.add(rootModel);

        return treeList;
    }

    @Override
    public List<RunningCaseManageVO> getRunningCaseManageData() {
        // TODO Auto-generated method stub
        return auToRunningCaseManageMapper.getRunningCaseManageData();
    }

    @Override
    public List<RunningCaseManageVO> getRunningCaseManageData1() {
        // TODO Auto-generated method stub
        return auToRunningCaseManageMapper.getRunningCaseManageData1();
    }

    @Override
    public RunningProject getProjectNameByTaskId(String taskId) {
        // TODO Auto-generated method stub
        return auToRunningCaseManageMapper.getProjectNameByTaskId(taskId);
    }

    @Override
    public void updateCaseManage(String id) {
        //??????????????????????????????
        RunningCase cases= new RunningCase();
        String caseId = id;
        cases.setId(caseId);
        cases.setDelFlag(1);
        this.updateById(cases);
        //???????????????????????????????????????
        List<RunningQuestionVO> questions = runningQuestionService.getRunningQuestionData(caseId);
        if(questions!=null) {
            for(int i=0;i<questions.size();i++) {
                //?????????????????????????????????????????????
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
        return auToRunningCaseManageMapper.getProjectIdByTaskId(taskId);
    }
}
