package org.jeecg.modules.serve.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.autoResult.entity.AutoResult;
import org.jeecg.modules.autoResult.service.IAutoResultService;
import org.jeecg.modules.autoVideo.entity.AutoVideo;
import org.jeecg.modules.autoVideo.service.IAutoVideoService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.IscTools;
import org.jeecg.modules.plan.entity.AutoPlan;
import org.jeecg.modules.plan.service.IAutoPlanService;
import org.jeecg.modules.runscriptresult.entity.RunScriptResult;
import org.jeecg.modules.runscriptresult.service.IRunScriptResultService;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.jeecg.modules.scriptResult.entity.AutoResultScript;
import org.jeecg.modules.scriptResult.service.IAutoResultScriptService;
import org.jeecg.modules.scriptrecord.entity.RunScriptRecord;
import org.jeecg.modules.scriptrecord.service.IRunScriptRecordService;
import org.jeecg.modules.serve.entity.ServerUpReportPara;
import org.jeecg.modules.serve.entity.ServerUpScriptPara;
import org.jeecg.modules.service.IpfsService;
import org.jeecg.modules.task.entity.AuToRunningCase;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.service.IAuToRunningCaseService;
import org.nfunk.jep.function.Str;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yeyl
 */
@Service
@Slf4j
public class ServeService {
    @Resource
    IAutoPlanService iAutoPlanService;
    @Resource
    IAutoScriptService iAutoScriptService;
    @Resource
    IAutoVideoService iAutoVideoService;
    @Resource
    IAutoResultService autoResultService;
    @Resource
    IAutoResultScriptService autoResultScriptService;
    @Resource
    IRunScriptResultService runScriptResultService;
    @Resource
    IRunScriptRecordService runScriptRecordService;
    @Resource
    IAuToRunningCaseService auToRunningCaseService;
    @Resource
    IAutoScriptService autoScriptService;

    @Resource
    IpfsService ipfsService;

    @Transactional(rollbackFor = Exception.class)
    public boolean uploadScript(ServerUpScriptPara serverUpScriptPara) {
        String scriptPath = "/home/" + serverUpScriptPara.getContent() + "/" + serverUpScriptPara.getScriptName();
        //??????????????????????????????
        if (!ipfsService.filesStat(scriptPath)) {
            throw new UnsupportedOperationException(":???????????????????????????");
        }
        String videoPath = "/home/" + serverUpScriptPara.getVideo() + "/" + serverUpScriptPara.getVideoName();
        //??????????????????????????????
        if (!ipfsService.filesStat(videoPath)) {
            throw new UnsupportedOperationException(":?????????????????????????????????");
        }
        //??????????????????????????????
        //AuToRunningCase auToRunningCase = getCase(serverUpScriptPara.getTaskName(), serverUpScriptPara.getTestName());
        //???????????????????????????
        QueryWrapper<AutoScript> scriptQueryWrapper = new QueryWrapper<>();
        scriptQueryWrapper.eq("script_name", serverUpScriptPara.getScriptName());
        scriptQueryWrapper.eq("script_content", serverUpScriptPara.getContent());
        scriptQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        List<AutoScript> list = iAutoScriptService.list(scriptQueryWrapper);
        if (!IscTools.isCollection(list) && list.size() > 0) {
            throw new UnsupportedOperationException("??????????????????????????????????????????");
        }
        //????????????
//        AutoScript autoScript=new AutoScript();
//        autoScript.setScriptName(serverUpScriptPara.getScriptName());
//        autoScript.setScriptContent(serverUpScriptPara.getContent());
//        autoScript.setTestCaseId(auToRunningCase.getId());
//        autoScript.setTestCaseName(auToRunningCase.getTestTaskName());
//        autoScript.setIdel(CommonConstant.DATA_INT_0);
//        autoScript.setWeight(CommonConstant.DATA_INT_IDEL_0);
        AutoScript autoScript = iAutoScriptService.getById(serverUpScriptPara.getScriptCode());
        autoScript.setScriptContent(serverUpScriptPara.getContent());
        iAutoScriptService.saveOrUpdate(autoScript);

        //?????????????????????
        return addVideo(autoScript, serverUpScriptPara);
    }

    //??????????????????
    private boolean addVideo(AutoScript autoScript, ServerUpScriptPara serverUpScriptPara) {
        //????????????????????????
        try {
            AutoVideo autoVideo = new AutoVideo();
            autoVideo.setIdel(CommonConstant.DATA_INT_IDEL_0);
            autoVideo.setScriptId(autoScript.getId());
            autoVideo.setScriptName(autoScript.getScriptName());
            autoVideo.setVideoAddress(serverUpScriptPara.getVideo());
            autoVideo.setName(serverUpScriptPara.getVideoName());
            autoVideo.setTestCaseName(serverUpScriptPara.getTestName());
            autoVideo.setTestItemName(serverUpScriptPara.getTaskName());
            autoVideo.setProjectName(serverUpScriptPara.getProjectName());
            autoVideo.setNodeName(autoScript.getRecordNodeName());
            autoVideo.setClientIp(autoScript.getRecordNodeIp());
            return iAutoVideoService.save(autoVideo);
        } catch (Exception e) {
            log.info("?????????????????????????????????" + e.getMessage());
            return false;
        }
    }

    private AutoPlan getAutoPlan(String scheme, String project) {
        QueryWrapper<AutoPlan> planQueryWrapper = new QueryWrapper<>();
        planQueryWrapper.eq("plan_name", scheme);
        planQueryWrapper.eq("project_name", project);
        planQueryWrapper.orderByDesc(CommonConstant.DATA_START_TIME);
        planQueryWrapper.last("limit 1");
        planQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        AutoPlan plan = iAutoPlanService.getOne(planQueryWrapper);
        if (plan == null || StringUtils.isBlank(plan.getProjectId())) {
            throw new UnsupportedOperationException("?????????????????????????????????");
        }
        return plan;
    }

    private AuToRunningCase getCase(String taskName, String testName) {
        QueryWrapper<AuToRunningCase> caseQueryWrapper = new QueryWrapper<>();
        caseQueryWrapper.eq("test_task_name", taskName);
        caseQueryWrapper.eq("test_name", testName);
        caseQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        AuToRunningCase auToRunningCase = auToRunningCaseService.getOne(caseQueryWrapper);
        if (auToRunningCase == null) {
            throw new UnsupportedOperationException("???????????????????????????");
        }
        return auToRunningCase;
    }

    public boolean uploadReport(ServerUpReportPara serverUpReportPara) {

        try {

            String scriptPath = "/home/" + serverUpReportPara.getReportFile() + "/" + serverUpReportPara.getScriptName();
            Object filesLs = ipfsService.filesLs(scriptPath);
            List<JSONObject> objectList = JSONObject.parseObject(JSON.toJSONString(filesLs), new TypeReference<List<JSONObject>>() {});
            if(CollectionUtils.isEmpty(objectList)){
                throw new UnsupportedOperationException(serverUpReportPara.getScriptName() + "??????????????????????????????");
            }
            //String testCaseId="";
            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //1?????????????????????????????????????????????????????????????????????????????????
            // QueryWrapper<AutoResult> queryWrapper = new QueryWrapper<>();
            //1.1?????????
//            queryWrapper.eq(CommonConstant.DATA_STRING_TEST_ITEM_NAME,serverUpReportPara.getTaskName());
//            queryWrapper.eq(CommonConstant.DATA_STRING_TEST_CASE_NAME,serverUpReportPara.getTestName());
            // queryWrapper.eq(CommonConstant.DATA_STRING_IDEL,CommonConstant.DATA_INT_IDEL_0);
            // queryWrapper.orderByDesc(CommonConstant.DATA_START_TIME);
            //queryWrapper.last("limit 1");
            // for (AutoResult autoResult : list) {
//                testCaseId=autoResult.getId();
//            }

            //??????????????????
            RunScriptRecord rsr = runScriptRecordService.getById(serverUpReportPara.getRunRecordId());
            if (!Objects.isNull(rsr)) {
                //1.2 ?????????
                RunScriptRecord runScriptRecord = runScriptRecordService.getById(serverUpReportPara.getRunRecordId());
                //??????????????????
                runScriptRecordService.saveOrUpdate(runScriptRecord.setEndTime(new Date()));
                //2????????????????????????????????????????????????????????????
                RunScriptResult runScriptResult = new RunScriptResult();
                runScriptResult.setAutoScriptName(serverUpReportPara.getScriptName());
                runScriptResult.setAutoScriptId(serverUpReportPara.getScriptCode());
                //??????????????????????????????id????????????????????????????????????
                runScriptResult.setScriptRecordId(runScriptRecord.getId());
                runScriptResult.setCreateTime(new Date());
                runScriptResult.setReport(serverUpReportPara.getReportFile());
                runScriptResult.setIdel(CommonConstant.DATA_STR_0);
                runScriptResult.setState(serverUpReportPara.getState());
                if (runScriptResultService.saveOrUpdate(runScriptResult)) {
                    //??????????????????
                    autoScriptService.update(new UpdateWrapper<AutoScript>().eq(CommonConstant.DATA_STRING_ID, serverUpReportPara.getScriptCode()).set(CommonConstant.DATA_STRING_STATE, CommonConstant.DATA_STR_0));
                    log.info("??????????????????????????????????????????????????????????????????");
                } else {
                    throw new UnsupportedOperationException("????????????????????????");
                }
            }

            if (Objects.isNull(rsr)) {
                //????????????????????????
                AutoResult autoResult = autoResultService.getById(serverUpReportPara.getRunRecordId());
                //??????????????????
                autoResultService.saveOrUpdate(autoResult.setEndTime(new Date()));
                //2????????????????????????????????????????????????????????????
                AutoResultScript autoResultScript = new AutoResultScript();
                autoResultScript.setAutoScriptName(serverUpReportPara.getScriptName());
                autoResultScript.setAutoScriptId(serverUpReportPara.getScriptCode());
                //??????????????????????????????id????????????????????????????????????
                autoResultScript.setPlanResultId(autoResult.getId());
                autoResultScript.setCreateTime(new Date());
                autoResultScript.setReport(serverUpReportPara.getReportFile());
                autoResultScript.setIdel(CommonConstant.DATA_STR_0);
                autoResultScript.setState(serverUpReportPara.getState());
                autoResultScriptService.saveOrUpdate(autoResultScript);
                //3???????????????????????????????????????????????????
                QueryWrapper<AutoResultScript> autoResultScriptQueryWrapper = new QueryWrapper<AutoResultScript>().eq(CommonConstant.DATA_STRING_PLAN_RESULT_ID, autoResult.getId());
                List<AutoResultScript> autoResultScripts = autoResultScriptService.list(autoResultScriptQueryWrapper);

                //4????????????????????????????????????????????????
                if (CollectionUtils.isNotEmpty(autoResultScripts)) {
                    //?????????????????????????????????
                    if (autoScriptService.update(new UpdateWrapper<AutoScript>().eq(CommonConstant.DATA_STRING_ID, serverUpReportPara.getScriptCode()).set(CommonConstant.DATA_STRING_STATE, CommonConstant.DATA_STR_0))) {
                        log.info("???????????????????????????????????????????????????????????????");
                    } else {
                        throw new UnsupportedOperationException("????????????????????????");
                    }
                    QueryWrapper<AutoResult> resultQueryWrapper = new QueryWrapper<>();
                    resultQueryWrapper.eq(CommonConstant.DATA_STRING_TEST_ITEM_NAME, serverUpReportPara.getTaskName());
                    resultQueryWrapper.eq(CommonConstant.DATA_STRING_TEST_CASE_NAME, serverUpReportPara.getTestName());
                    resultQueryWrapper.notIn(CommonConstant.DATA_STRING_ID, autoResult.getId());
                    List<AutoResult> collect = autoResultService.list(resultQueryWrapper);
                    for (AutoResult ar : collect) {
                        ar.setIdel(CommonConstant.DATA_INT_IDEL_1);
                        autoResultService.saveOrUpdate(ar);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.info("?????????????????????????????????" + e.getMessage());
            return false;
        }

//        //?????????????????? ????????????????????????
//        //AutoPlan autoPlan = getAutoPlan(serverUpReportPara.getScheme(), serverUpReportPara.getProjectName());
//        QueryWrapper<AutoScript> scriptQueryWrapper = new QueryWrapper<>();
//        scriptQueryWrapper.eq("script_name",serverUpReportPara.getScriptName());
//        //scriptQueryWrapper.eq("plan_id",autoPlan.getId());
//        scriptQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL,CommonConstant.DATA_INT_IDEL_0);
//        AutoScript script  = iAutoScriptService.getOne(scriptQueryWrapper);
//        if (script==null||StringUtils.isBlank(script.getScriptContent())){
//            throw new UnsupportedOperationException("???????????????????????????");
//        }
//        //???????????????????????????????????????
//        script.setReport(serverUpReportPara.getReportFile());
//       return iAutoScriptService.updateById(script);
    }

    public boolean scriptCallback(String uniqueCode) {
        AutoScript script = iAutoScriptService.getById(uniqueCode);
        if (script == null || StringUtils.isBlank(script.getScriptContent())) {
            throw new UnsupportedOperationException(uniqueCode + "???????????????");
        }
        //0?????? 1??????
        script.setState("0");
        return iAutoScriptService.updateById(script);
    }
}
