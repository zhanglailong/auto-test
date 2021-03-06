package org.jeecg.modules.script.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.ipfs.api.NamedStreamable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.config.SnowflakeConfig;
import org.jeecg.modules.autoNodes.entity.AutoNodes;
import org.jeecg.modules.autoNodes.service.IAutoNodesService;
import org.jeecg.modules.autoResult.service.IAutoResultService;
import org.jeecg.modules.autoVideo.entity.AutoVideo;
import org.jeecg.modules.autoVideo.service.IAutoVideoService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.IscTools;
import org.jeecg.modules.file.controller.FileController;
import org.jeecg.modules.nodeserve.entity.NodeStartScriptPara;
import org.jeecg.modules.nodeserve.service.INodeLogService;
import org.jeecg.modules.nodeserve.service.ext.NodeServiceExt;
import org.jeecg.modules.parameter.entity.ScriptParameter;
import org.jeecg.modules.parameter.service.IScriptParameterService;
import org.jeecg.modules.plan.entity.AutoPlan;
import org.jeecg.modules.plan.service.IAutoPlanService;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.entity.AutoScriptVo;
import org.jeecg.modules.script.entity.ScriptViewVo;
import org.jeecg.modules.script.mapper.AutoScriptMapper;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.jeecg.modules.scriptandplan.entity.AutoScriptPlan;
import org.jeecg.modules.scriptandplan.service.IAutoScriptPlanService;
import org.jeecg.modules.service.IpfsService;
import org.jeecg.modules.task.entity.AuToRunningCase;
import org.jeecg.modules.task.service.IAuToRunningCaseService;
import org.jeecg.modules.task.vo.RunningCaseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.jeecg.modules.common.FileUtils.*;

/**
 * @Description: ????????????
 * @Author: zll
 * @Date: 2021-08-17
 * @Version: V1.0
 */
@Service
@Slf4j
@Transactional
public class AutoScriptServiceImpl extends ServiceImpl<AutoScriptMapper, AutoScript> implements IAutoScriptService {
    /**
     * ipfsURL
     */
    @Value(value = "${ipfs.queryUrl}")
    private String ipfsQueryUrl;
    @Resource
    private AutoScriptMapper autoScriptMapper;
    @Resource
    IAutoScriptPlanService autoScriptPlanService;
    @Resource
    FileController fileController;
    @Resource
    IpfsService ipfsService;
    @Resource
    IScriptParameterService iScriptParameterService;

    @Resource
    SnowflakeConfig snowflakeConfig;
    @Resource
    NodeServiceExt nodeServiceExt;
    @Resource
    IAutoPlanService iAutoPlanService;
    @Resource
    IAutoVideoService iAutoVideoService;
    @Resource
    private IAutoNodesService autoNodesService;
    @Resource
    private IAuToRunningCaseService autoRunningCaseService;
    @Resource
    private IAutoResultService autoResultService;
    @Resource
    private INodeLogService nodeLogService;


    @Override
    public boolean edit(AutoScriptVo autoScript) {
        try {
            String scriptPath = "/home/" + autoScript.getScriptContent() + "/" + autoScript.getScriptName();
            //??????????????????????????????
            if (!ipfsService.filesStat(scriptPath)) {
                throw new UnsupportedOperationException(":???????????????????????????");
            }
//            String videoPath ="/home/" +autoScript.getVideo()+"/"+autoScript.getVideoName();
//            //??????????????????????????????
//            if(!ipfsService.filesStat(videoPath)){
//                throw new UnsupportedOperationException(":?????????????????????????????????");
//            }
            //?????????????????? ????????????????????????
            //AutoPlan plan = getAutoPlan(autoScript.getPlanName(),autoScript.getProjectName());
            AutoScript script = new AutoScript();
            BeanUtils.copyProperties(autoScript, script);
            script.setIdel(CommonConstant.DATA_INT_IDEL_0);
            script.setWeight(CommonConstant.DATA_INT_IDEL_0);
            //????????????

            this.saveOrUpdate(script);

            //?????????????????????????????????????????????
            if (StringUtils.isNotBlank(autoScript.getVideoHash())) {
                AutoVideo autoVideo = new AutoVideo();
                autoVideo.setIdel(CommonConstant.DATA_INT_IDEL_0);
                autoVideo.setScriptId(autoScript.getId());
                autoVideo.setScriptName(autoScript.getScriptName());
                autoVideo.setVideoAddress(autoScript.getVideoHash());
                autoVideo.setName(autoScript.getVideoName());
                iAutoVideoService.save(autoVideo);
            }
            return true;
        } catch (Exception e) {
            log.info("?????????????????? ????????????" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try {
            AutoScript autoScript = autoScriptMapper.selectById(id);
            autoScript.setIdel(CommonConstant.DATA_INT_IDEL_1);
            int flag = autoScriptMapper.updateById(autoScript);
            if (flag == CommonConstant.DATA_INT_1) {
                //??????????????????????????????
                QueryWrapper<AutoScriptPlan> planQueryWrapper = new QueryWrapper<>();
                planQueryWrapper.eq(CommonConstant.DATA_STRING_OLDSCRIPTID, id);
                planQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                List<AutoScriptPlan> list = autoScriptPlanService.list(planQueryWrapper);
                if (CollectionUtils.isNotEmpty(list)) {
                    for (AutoScriptPlan autoScriptPlan : list) {
                        autoScriptPlan.setIdel(CommonConstant.DATA_INT_IDEL_1);
                        autoScriptPlanService.updateById(autoScriptPlan);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.info("?????????????????????????????????" + e.getMessage());
            return false;
        }
    }

    @Override
    public List<AutoScript> treeScriptList() {
        List<AutoScript> scriptList = autoScriptMapper.treeScript();
        return scriptList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String bindingScript(String testCaseId, String scriptIds) {
        try {

            AuToRunningCase auToRunningCase = autoRunningCaseService.getById(testCaseId);
            List<String> ids = Arrays.asList(scriptIds.split(","));
            for (String id : ids) {
                AutoScript bindScript = this.getOne(new QueryWrapper<AutoScript>().eq(CommonConstant.DATA_STRING_ID, id)
                        .ne(CommonConstant.DATA_STRING_TEST_CASE_ID, CommonConstant.DATA_INT_IDEL_0)
                        .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0));
                if (!Objects.isNull(bindScript)) {
                    throw new UnsupportedOperationException("?????????????????????????????????????????????????????????!");
                }
                AutoScript autoScript = autoScriptMapper.selectById(id);
                AutoScript script = new AutoScript();
                BeanUtils.copyProperties(autoScript, script);
                script.setTestCaseId(testCaseId);
                script.setTestCaseName(auToRunningCase.getTestName());
                this.saveOrUpdate(script);
            }
            return "????????????????????????";
        } catch (Exception e) {
            log.error("???????????????????????? " + e.getMessage());
            return "????????????????????????????????????" + e.getMessage();
        }
    }

    @Override
    public void scriptUp(AutoScript autoScript) {
        try {
            //????????????
            AutoScript scriptNow = autoScriptMapper.selectOne(new QueryWrapper<AutoScript>()
                    .eq(CommonConstant.DATA_STRING_SORT, autoScript.getSort())
                    .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0));
            //???????????????
            AutoScript previousScript = autoScriptMapper.moveUp(scriptNow);
            //?????????????????????
            if (Objects.isNull(previousScript)) {
                return;
            }
            //????????????
            Integer tepm = scriptNow.getSort();
            scriptNow.setSort(previousScript.getSort());
            previousScript.setSort(tepm);
            autoScriptMapper.updateById(scriptNow);
            autoScriptMapper.updateById(previousScript);
        } catch (Exception e) {
            log.error("??????????????????,?????????" + e.getMessage());
        }
    }

    @Override
    public void scriptdown(AutoScript autoScript) {
        try {
            //????????????
            AutoScript scriptNow = autoScriptMapper.selectOne(new QueryWrapper<AutoScript>()
                    .eq(CommonConstant.DATA_STRING_SORT, autoScript.getSort())
                    .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0));
            //???????????????
            AutoScript behindScript = autoScriptMapper.moveDown(scriptNow);
            //?????????????????????
            if (Objects.isNull(behindScript)) {
                return;
            }
            //????????????
            Integer tepm = scriptNow.getSort();
            scriptNow.setSort(behindScript.getSort());
            behindScript.setSort(tepm);
            autoScriptMapper.updateById(scriptNow);
            autoScriptMapper.updateById(behindScript);
        } catch (Exception e) {
            log.error("??????????????????,?????????" + e.getMessage());
        }
    }

    @Override
    public Integer getScriptList(QueryWrapper queryWrapper) {
        List list = autoScriptMapper.selectList(queryWrapper);
        return list.size();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean inportScript(AutoScriptVo autoScript) {
        try {
            String scriptPath = "/home/" + autoScript.getScriptContent() + "/" + autoScript.getScriptName();
            //??????????????????????????????
            if (!ipfsService.filesStat(scriptPath)) {
                throw new UnsupportedOperationException(":???????????????????????????");
            }
//            String videoPath ="/home/" +autoScript.getVideo()+"/"+autoScript.getVideoName();
//            //??????????????????????????????
//            if(!ipfsService.filesStat(videoPath)){
//                throw new UnsupportedOperationException(":?????????????????????????????????");
//            }
            //?????????????????? ????????????????????????
            // AutoPlan plan = getAutoPlan(autoScript.getPlanName(),autoScript.getProjectName());
            //???????????????????????????
            QueryWrapper<AutoScript> scriptQueryWrapper = new QueryWrapper<>();
            scriptQueryWrapper.eq("script_name", autoScript.getScriptName());
            scriptQueryWrapper.eq("script_content", autoScript.getScriptContent());
            scriptQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
            List<AutoScript> list = this.list(scriptQueryWrapper);
            if (!IscTools.isCollection(list) && list.size() > 0) {
                throw new UnsupportedOperationException("??????????????????????????????????????????");
            }
            AutoScript script = new AutoScript();
            script.setIdel(CommonConstant.DATA_INT_IDEL_0);
            script.setWeight(0);
            //????????????
            script.setScriptName(autoScript.getFileName());
            script.setFileName(autoScript.getFileName());
            script.setScriptContent(autoScript.getScriptContent());
            script.setIdel(CommonConstant.DATA_INT_IDEL_0);
            script.setWeight(0);
            script.setTestCaseId(autoScript.getTestCaseId());
            script.setTestCaseName(autoScript.getTestCaseName());
            this.save(script);
            //?????????????????????????????????????????????
            if (StringUtils.isNotBlank(autoScript.getVideoHash())) {
                AutoVideo autoVideo = new AutoVideo();
                autoVideo.setIdel(CommonConstant.DATA_INT_IDEL_0);
                autoVideo.setScriptId(autoScript.getId());
                autoVideo.setScriptName(autoScript.getScriptName());
                autoVideo.setVideoAddress(autoScript.getVideoHash());
                autoVideo.setName(autoScript.getVideoName());
                iAutoVideoService.save(autoVideo);
            }
            return true;
        } catch (Exception e) {
            log.info("?????????????????? ????????????" + e.getMessage());
            return false;
        }
    }


    private AutoPlan getAutoPlan(String scheme, String project) {
        QueryWrapper<AutoPlan> planQueryWrapper = new QueryWrapper<>();
        planQueryWrapper.eq("plan_name", scheme);
        planQueryWrapper.eq("project_name", project);
        planQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        AutoPlan plan = iAutoPlanService.getOne(planQueryWrapper);
        if (plan == null || StringUtils.isBlank(plan.getProjectId())) {
            throw new UnsupportedOperationException("?????????????????????????????????");
        }
        return plan;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveScriptText(List<ScriptViewVo> scriptViewVos, String scriptId) {
        File localFile = null;
        File pyLocalFile = null;
        try {
            //?????????????????????
            AutoScript script = this.getById(scriptId);
            if (script == null
                    || StringUtils.isBlank(script.getScriptContent())
                    || StringUtils.isBlank(script.getScriptName())
            ) {
                log.info(script + "??????????????????????????????");
                throw new UnsupportedOperationException("??????????????????????????????");
            }
            //?????????json??????
            String scriptViewJson = JSON.toJSONString(scriptViewVos);
            String viewFileName = scriptViewVos.get(0).getViewFileName();
            log.info("????????????:" + viewFileName + "?????????????????????:" + scriptViewJson);
            String ipfsPath = "/home/" + script.getScriptContent() + "/" + script.getScriptName() + "/" + viewFileName;
            String localPath = File.separator + "home" + File.separator + viewFileName;
            localFile = new File(isWindows(localPath));
            //??????????????????
            createFile(localFile);
            FileOutputStream fos = new FileOutputStream(localFile);
            fos.write(scriptViewJson.getBytes());
            fos.close();
            fos.flush();
            //?????????????????????
            ipfsService.writeFile(new NamedStreamable.FileWrapper(localFile), ipfsPath, Long.parseLong(script.getScriptContent()));
            //todo ??????????????????????????????py?????? ????????????
            String viewToPy = nodeServiceExt.viewToPy(script.getId(), script.getScriptName(), scriptViewJson);
            if (StringUtils.isBlank(viewToPy)) {
                throw new UnsupportedOperationException("??????????????????py????????????");
            }
            Object filesLs = ipfsService.filesLs("/home/" + script.getScriptContent() + "/" + script.getScriptName());
            List<JSONObject> objectList = JSONObject.parseObject(JSON.toJSONString(filesLs), new TypeReference<List<JSONObject>>() {
            });
            for (int i = 0; i < objectList.size(); i++) {
                if (objectList.get(i).get("Name").toString().contains("py")) {
                    String ipfsPyPath = "/home/" + script.getScriptContent() + "/" + script.getScriptName() + "/" + objectList.get(i).get("Name").toString();
                    String pyLocalPath = File.separator + "home" + File.separator + objectList.get(i).get("Name").toString();
                    pyLocalFile = new File(isWindows(pyLocalPath));
                    createFile(pyLocalFile);
                    FileOutputStream pfos = new FileOutputStream(pyLocalFile);
                    pfos.write(viewToPy.getBytes());
                    pfos.close();
                    pfos.flush();
                    ipfsService.writeFile(new NamedStreamable.FileWrapper(pyLocalFile), ipfsPyPath, Long.parseLong(script.getScriptContent()));
                }
            }
            return true;
        } catch (Exception e) {
            log.info("????????????????????????" + e.getMessage());
            return false;
        } finally {
            //??????????????????
            if (localFile != null) {
                log.info(deleteFile(localFile) ? "??????" + "??????" + localFile.getName() + "?????????????????????" : "??????" + "??????" + localFile.getName() + "?????????????????????");
            }
            if (pyLocalFile != null) {
                log.info(deleteFile(pyLocalFile) ? "??????" + "??????" + pyLocalFile.getName() + "?????????????????????" : "??????" + "??????" + pyLocalFile.getName() + "?????????????????????");
            }

        }
    }

    /**
     * @param path ???????????????
     * @return ????????????????????????
     */
    public String isWindows(String path) {
        if (IscTools.isWindowsOS()) {
            return "C:" + path;
        } else {
            return path;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void copyScript(AutoScript desScript, AutoScript sourceScript) {
        try {
            //????????????
            String sourceScriptId = sourceScript.getId();
            AutoVideo autoVideo = iAutoVideoService.getVideo(sourceScriptId);
            //????????? /home/1430469924274438144/??????
            String snowflakeSourCeDirectoryPath = "/home/" + sourceScript.getScriptContent() + "/" + sourceScript.getScriptName();
            long snowflakeId = snowflakeConfig.snowflakeId();
            //   /home/235666334555
            String snowflakePath = "/home/" + snowflakeId;
            // ???????????? /home/235666334555/??????Copy
            String snowflakeDestDirectoryPath = snowflakePath + "/" + desScript.getScriptName();
            //???????????????????????????
            if (!ipfsService.filesStat(snowflakePath)) {
                if (!ipfsService.filesMkdir(snowflakePath)) {
                    throw new UnsupportedOperationException("?????????????????????!");
                }
            }
            //????????????
            if (ipfsService.fileCp(snowflakeSourCeDirectoryPath, snowflakeDestDirectoryPath)) {
               /* //????????????sort??????1
                List<AutoScript> autoScriptList = this.list(new QueryWrapper<AutoScript>().eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0));
                AutoScript autoScript = autoScriptList.stream().max(Comparator.comparing(AutoScript::getSort)).get();
                Integer sort = autoScript.getSort() + 1;*/
                //???????????? ???????????? ?????? ????????????
                sourceScript.setIdel(CommonConstant.DATA_INT_IDEL_0);
                sourceScript.setScriptName(desScript.getScriptName());
                sourceScript.setRemark(desScript.getRemark());
                sourceScript.setScriptContent(String.valueOf(snowflakeId));
                sourceScript.setTestCaseName(desScript.getTestCaseName());
                sourceScript.setTestCaseId(CommonConstant.DATA_STR_0);
                sourceScript.setSort(CommonConstant.DATA_INT_0);
                sourceScript.setWeight(CommonConstant.DATA_INT_0);
                sourceScript.setId(null);
                if (this.save(sourceScript)) {
                    //????????????
                    if (!Objects.isNull(autoVideo)) {
                        AutoVideo newAutoVideo = new AutoVideo();
                        BeanUtils.copyProperties(autoVideo, newAutoVideo);
                        newAutoVideo.setId(null);
                        newAutoVideo.setScriptName(null);
                        newAutoVideo.setScriptId(sourceScript.getId());
                        newAutoVideo.setCreateTime(new Date());
                        newAutoVideo.setScriptName(sourceScript.getScriptName());
                        newAutoVideo.setVideoAddress(autoVideo.getVideoAddress());
                        iAutoVideoService.save(newAutoVideo);
                    }
                    //???????????? ?????????????????????
                    List<ScriptParameter> parameterCopyList = iScriptParameterService.list(new QueryWrapper<ScriptParameter>()
                            .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                            .eq(CommonConstant.DATA_STRING_SCRIPT_ID, desScript.getId())
                    );
                    parameterCopyList.forEach(scriptParameter -> {
                        scriptParameter.setScriptId(sourceScript.getId());
                        scriptParameter.setId(null);
                        scriptParameter.setIdel(CommonConstant.DATA_INT_IDEL_0);
                    });
                    //??????????????????
                    iScriptParameterService.saveBatch(parameterCopyList);
                } else {
                    throw new UnsupportedOperationException("??????????????????!");
                }
            } else {
                throw new UnsupportedOperationException("??????????????????!");
            }
        } catch (Exception e) {
            log.error("?????????????????? ????????????" + e.getMessage());
            throw new UnsupportedOperationException("?????????????????? ????????????" + e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public List<ScriptViewVo> getViewContentByScriptId(String scriptId) {
        BufferedReader br = null;
        StringBuilder viewResult = new StringBuilder();
        File snowFolder = null;
        try {
            //????????????id??????????????????
            AutoScript script = this.getById(scriptId);
            //????????????
            String localPath = "/home/" + script.getScriptContent();
            //???????????????hash
            String scriptPath = localPath + "/" + script.getScriptName();
            String fileHash = ipfsService.filesStatHash(scriptPath);
            //?????????????????????
            ipfsService.download(scriptPath, fileHash);
            File localFile;
            if (IscTools.isWindowsOS()) {
                localFile = new File("C:" + scriptPath);
                snowFolder = new File("C:" + localPath);
            } else {
                localFile = new File(scriptPath);
                snowFolder = new File(localPath);
            }
            if (localFile.exists() && localFile.isDirectory()) {
                File[] files = localFile.listFiles();
                if (!IscTools.isEmptyArray(files)) {
                    for (File f : files) {
                        if (f.getName().contains("view")) {
                            //????????????BufferedReader??????????????????
                            br = new BufferedReader(new FileReader(f));
                            String s;
                            while ((s = br.readLine()) != null) {
                                //??????readLine????????????????????????
                                viewResult.append(System.lineSeparator()).append(s);
                            }
                            List<ScriptViewVo> viewVos;
                            if (viewResult.toString().contains(CommonConstant.DATA_STRING_CONTENT)) {
                                JSONObject jsonObject = JSON.parseObject(viewResult.toString());
                                String content = jsonObject.get(CommonConstant.DATA_STRING_CONTENT).toString();
                                viewVos = JSONArray.parseArray(content, ScriptViewVo.class);
                            } else {
                                viewVos = JSONArray.parseArray(viewResult.toString(), ScriptViewVo.class);
                            }

                            //String ??? ??????
                            viewVos.forEach(scriptViewVo -> {
                                //    ??????????????????
                                scriptViewVo.setViewFileName(f.getName());
                                if (scriptViewVo.getObject().contains("????????????")) {
                                    String value = scriptViewVo.getValue();
                                    //??????????????????
                                    String imgName = "";
                                    for (String format : imgFormatList) {
                                        if (value.contains(format)) {
                                            //????????? format ????????????????????????
                                            String subName = value.substring(0, value.indexOf(format));
                                            int begin = subName.lastIndexOf("r");
                                            String imgNameNoSuffix = subName.substring(begin + 2).replace("\"", "");
                                            //???????????????
                                            imgName = imgNameNoSuffix + format;
                                            log.info("????????????:" + imgName);
                                            break;
                                        }
                                    }
                                    String viewHash = ipfsService.filesStatHash(scriptPath + "/" + imgName);
                                    scriptViewVo.setImgName(imgName);
                                    scriptViewVo.setImgUrl(ipfsQueryUrl + viewHash);
                                }
                            });
                            return viewVos;
                        }
                    }
                }
            } else {
                throw new UnsupportedOperationException("??????????????????!");
            }
        } catch (IOException e) {
            log.info("??????????????????????????? ????????????" + e.getMessage());
            return null;
        } finally {
            if (br != null) {
                br.close();
            }
            //??????????????????????????????
            if (snowFolder != null) {
                log.info(deleteFile(snowFolder) ? "??????" + "??????" + snowFolder.getName() + "?????????????????????" : "??????" + "??????" + snowFolder.getName() + "?????????????????????");
            }
        }
        return null;
    }

    @Override
    public boolean addScript(AutoScriptVo autoScript) {
        try {
            String scriptPath = "/home/" + autoScript.getScriptContent() + "/" + autoScript.getScriptName();
            //??????????????????????????????
            if (!ipfsService.filesStat(scriptPath)) {
                throw new UnsupportedOperationException(":???????????????????????????");
            }
//            String videoPath ="/home/" +autoScript.getVideo()+"/"+autoScript.getVideoName();
//            //??????????????????????????????
//            if(!ipfsService.filesStat(videoPath)){
//                throw new UnsupportedOperationException(":?????????????????????????????????");
//            }
            //?????????????????? ????????????????????????
            // AutoPlan plan = getAutoPlan(autoScript.getPlanName(),autoScript.getProjectName());
            //???????????????????????????
            QueryWrapper<AutoScript> scriptQueryWrapper = new QueryWrapper<>();
            scriptQueryWrapper.eq("script_name", autoScript.getScriptName());
            scriptQueryWrapper.eq("script_content", autoScript.getScriptContent());
            scriptQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
            List<AutoScript> list = this.list(scriptQueryWrapper);
            if (!IscTools.isCollection(list) && list.size() > 0) {
                throw new UnsupportedOperationException("??????????????????????????????????????????");
            }
            //????????????
            AutoScript script = new AutoScript();
            BeanUtils.copyProperties(autoScript, script);
            script.setIdel(CommonConstant.DATA_INT_IDEL_0);
            script.setWeight(CommonConstant.DATA_INT_IDEL_0);
            this.save(script);
            //?????????????????????????????????????????????
            if (StringUtils.isNotBlank(autoScript.getVideoHash())) {
                AutoVideo autoVideo = new AutoVideo();
                autoVideo.setIdel(CommonConstant.DATA_INT_IDEL_0);
                autoVideo.setScriptId(autoScript.getId());
                autoVideo.setScriptName(autoScript.getScriptName());
                autoVideo.setVideoAddress(autoScript.getVideoHash());
                autoVideo.setName(autoScript.getVideoName());
                iAutoVideoService.save(autoVideo);
            }
            return true;
        } catch (Exception e) {
            log.info("?????????????????? ????????????" + e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<AutoScript> updateNodesAddScript(RunningCaseVO runningCaseVO, Integer type, List<AutoNodes> autoNodesList) {
        try {
            boolean rollback = false;
            //????????????-??????????????????+????????????+??????
            List<String> scriptNames = new ArrayList<>();
            Map<String, AutoNodes> nodesMap = new HashMap<>();
            //??????????????????-???????????? state 1
            autoNodesList.forEach(node -> {
                if (CommonConstant.DATA_INT_0.equals(type)) {
                    node.setState(CommonConstant.DATA_INT_1);
                } else {
                    node.setState(CommonConstant.DATA_INT_0);
                }
                long randomId = snowflakeConfig.snowflakeId();
                scriptNames.add(runningCaseVO.getTestName() + "-" + node.getNodeName() + "-" + runningCaseVO.getTurnNum()+"-"+randomId);
                nodesMap.put(runningCaseVO.getTestName() + "-" + node.getNodeName() + "-" + runningCaseVO.getTurnNum()+"-"+randomId, node);
            });
            //????????????????????????????????????
            UpdateWrapper<AuToRunningCase> updateRunningCaseWrapper = new UpdateWrapper<>();
            updateRunningCaseWrapper.eq(CommonConstant.DATA_STRING_ID, runningCaseVO.getId());
            if (CommonConstant.DATA_INT_0.equals(type)) {
                updateRunningCaseWrapper.set("start_time", new Date());
                updateRunningCaseWrapper.set("node_ids", autoNodesList.stream().map(AutoNodes::getId).collect(Collectors.joining(",")));
            } else {
                updateRunningCaseWrapper.set("end_time", new Date());
            }
            if (!autoRunningCaseService.update(updateRunningCaseWrapper)) {
                throw new UnsupportedOperationException("??????/??????-????????????????????????!");
            }
            List<AutoScript> autoScripts;
            if (CommonConstant.DATA_INT_0.equals(type)) {
                List<AutoScript> addAutoScripts = new ArrayList<>();
                scriptNames.forEach(name -> {
                    AutoScript autoScript = new AutoScript();
                    autoScript.setIdel(CommonConstant.DATA_INT_IDEL_0);
                    autoScript.setScriptName(name);
                    autoScript.setWeight(CommonConstant.DATA_INT_0);
                    autoScript.setRecordNodeName(nodesMap.get(name).getNodeName());
                    autoScript.setSort(CommonConstant.DATA_INT_0);
                    autoScript.setRecordNodeId(nodesMap.get(name).getId());
                    autoScript.setRecordNodeIp(nodesMap.get(name).getClientIp());
                    autoScript.setState(CommonConstant.DATA_STATE_0);
                    autoScript.setTestCaseId(runningCaseVO.getId());
                    autoScript.setTestCaseName(runningCaseVO.getTestName());
                    addAutoScripts.add(autoScript);
                });
                autoScripts = addAutoScripts;
            } else {
                QueryWrapper<AutoScript> autoScriptQueryWrapper = new QueryWrapper<>();
                autoScriptQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                autoScriptQueryWrapper.in(CommonConstant.DATA_STRING_NODEID,autoNodesList.stream().map(AutoNodes::getId).collect(Collectors.toList()));
                autoScripts = this.list(autoScriptQueryWrapper);
            }
            if (!autoNodesService.updateBatchById(autoNodesList)) {
                throw new UnsupportedOperationException("??????/??????-??????????????????-??????????????????!");
            }
            if (autoScripts.size() > 0) {
                if (!this.saveOrUpdateBatch(autoScripts)) {
                    rollback = true;
                }
            }
            if (rollback) {
                throw new UnsupportedOperationException("????????????????????????????????????,????????????????????????!");
            }
            return autoScripts;
        } catch (Exception e) {
            log.error("?????????????????????????????????????????????:" + e.getMessage());
            throw new UnsupportedOperationException("????????????????????????????????????,??????:" + e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void playBackScript(List<AutoScript> autoScriptList, String id, Integer type) {
        try {
            //????????????/??????????????????
            autoScriptList.forEach(n -> {
                if (StringUtils.isEmpty(n.getRecordNodeIp())) {
                    throw new UnsupportedOperationException("??????????????????-??????:" + n.getScriptName() + "????????????IP?????????");
                }
            });
            if (CommonConstant.DATA_INT_0.equals(type)) {
                String autoResultId = autoResultService.addRecord(id, type);
                //??????????????????
                autoScriptList.forEach(n -> {
                    RunningCaseVO runningCaseVo = autoRunningCaseService.getRunningCaseVoByCaseId(n.getTestCaseId());
                    if (runningCaseVo == null) {
                        throw new UnsupportedOperationException("??????????????????-?????????????????????");
                    }
                    n.setState(CommonConstant.DATA_STR_2);
                    nodeLogService.ascyncPlayBackScript(runningCaseVo,n,type,autoResultId);
                });
            }
            //???????????? type 1
            if (CommonConstant.DATA_INT_1.equals(type)) {
                //??????????????????
                autoScriptList.forEach(n -> {
                    RunningCaseVO runningCaseVo = autoRunningCaseService.getRunningCaseVoByCaseId(n.getTestCaseId());
                    if (runningCaseVo == null) {
                        throw new UnsupportedOperationException("??????????????????-?????????????????????");
                    }
                    n.setState(CommonConstant.DATA_STR_2);
                    nodeLogService.ascyncPlayBackScript(runningCaseVo,n,type,"");
                });
            }
        } catch (Exception e) {
            log.error("?????????????????????????????????:" + e.getMessage());
            throw new UnsupportedOperationException("????????????????????????,??????:" + e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String playBackStartScript(String scriptCode) {
        try {
            AutoScript autoScript = this.getById(scriptCode);
            if (autoScript != null) {
                //????????????id????????????????????????
                RunningCaseVO runningCaseVo = autoRunningCaseService.getRunningCaseVoByCaseId(autoScript.getTestCaseId());
                //????????????/??????????????????
                //String runRecordId = autoResultService.addRecord(scriptCode, CommonConstant.DATA_INT_1);
                NodeStartScriptPara nodeStartScriptPara = new NodeStartScriptPara();
                nodeStartScriptPara.setProjectName(runningCaseVo.getProjectName());
                nodeStartScriptPara.setTaskName(runningCaseVo.getTaskName());
                nodeStartScriptPara.setTestName(runningCaseVo.getTestName());
                nodeStartScriptPara.setScriptCode(autoScript.getId());
                nodeStartScriptPara.setScriptName(autoScript.getScriptName());
                nodeStartScriptPara.setRecordNodeIp(autoScript.getRecordNodeIp());
                //nodeStartScriptPara.setRunRecordId(runRecordId);
                nodeStartScriptPara.setStatus(CommonConstant.DATA_STR_0);
                //TODO ????????????????????????
                //autoScript.setState(CommonConstant.DATA_STR_3);
                if (nodeServiceExt.startScript(nodeStartScriptPara)) {
                    autoScript.setState(CommonConstant.DATA_STR_3);
                } else {
                    throw new UnsupportedOperationException("???????????????" + autoScript.getScriptName() + "??????");
                }
            }
            if (!this.updateById(autoScript)) {
                throw new UnsupportedOperationException("????????????-????????????????????????");
            }
            return CommonConstant.DATA_STR_0;
        } catch (Exception e) {
            log.error("???????????????????????????:" + e.getMessage());
            throw new UnsupportedOperationException("??????????????????,??????:" + e.getMessage());
        }
    }

    @Override
    public void removeBind(String id) {
        AutoScript autoScript = this.getById(id);
        autoScript.setTestCaseId(CommonConstant.DATA_STR_0);
        this.saveOrUpdate(autoScript);
    }

    @Override
    public boolean setSort(AutoScriptVo autoScript) {
        try {
            AutoScript script = this.getById(autoScript.getId());
            script.setSort(autoScript.getSort());
            this.saveOrUpdate(script);
            return true;
        } catch (Exception e) {
            log.info("?????????????????????????????????" + e.getMessage());
            return false;
        }
    }
}
