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
 * @Description: 脚本管理
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
            //查看脚本文件是否上传
            if (!ipfsService.filesStat(scriptPath)) {
                throw new UnsupportedOperationException(":脚本文件没有上传！");
            }
//            String videoPath ="/home/" +autoScript.getVideo()+"/"+autoScript.getVideoName();
//            //查看脚本文件是否上传
//            if(!ipfsService.filesStat(videoPath)){
//                throw new UnsupportedOperationException(":脚本视频文件没有上传！");
//            }
            //判断方案名称 项目名称是否存在
            //AutoPlan plan = getAutoPlan(autoScript.getPlanName(),autoScript.getProjectName());
            AutoScript script = new AutoScript();
            BeanUtils.copyProperties(autoScript, script);
            script.setIdel(CommonConstant.DATA_INT_IDEL_0);
            script.setWeight(CommonConstant.DATA_INT_IDEL_0);
            //保存脚本

            this.saveOrUpdate(script);

            //如果存在视频文件则保存脚本视频
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
            log.info("添加脚本失败 ，原因是" + e.getMessage());
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
                //删除方案下绑定的脚本
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
            log.info("删除脚本失败，原因是：" + e.getMessage());
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
                    throw new UnsupportedOperationException("该脚本已被绑定，请进行备份之后将其绑定!");
                }
                AutoScript autoScript = autoScriptMapper.selectById(id);
                AutoScript script = new AutoScript();
                BeanUtils.copyProperties(autoScript, script);
                script.setTestCaseId(testCaseId);
                script.setTestCaseName(auToRunningCase.getTestName());
                this.saveOrUpdate(script);
            }
            return "用例绑定脚本成功";
        } catch (Exception e) {
            log.error("方案绑定脚本失败 " + e.getMessage());
            return "用例绑定脚本失败，原因是" + e.getMessage();
        }
    }

    @Override
    public void scriptUp(AutoScript autoScript) {
        try {
            //当前对象
            AutoScript scriptNow = autoScriptMapper.selectOne(new QueryWrapper<AutoScript>()
                    .eq(CommonConstant.DATA_STRING_SORT, autoScript.getSort())
                    .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0));
            //前一个对象
            AutoScript previousScript = autoScriptMapper.moveUp(scriptNow);
            //最上面不能上移
            if (Objects.isNull(previousScript)) {
                return;
            }
            //交换位置
            Integer tepm = scriptNow.getSort();
            scriptNow.setSort(previousScript.getSort());
            previousScript.setSort(tepm);
            autoScriptMapper.updateById(scriptNow);
            autoScriptMapper.updateById(previousScript);
        } catch (Exception e) {
            log.error("脚本上移失败,原因是" + e.getMessage());
        }
    }

    @Override
    public void scriptdown(AutoScript autoScript) {
        try {
            //当前对象
            AutoScript scriptNow = autoScriptMapper.selectOne(new QueryWrapper<AutoScript>()
                    .eq(CommonConstant.DATA_STRING_SORT, autoScript.getSort())
                    .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0));
            //后一个对象
            AutoScript behindScript = autoScriptMapper.moveDown(scriptNow);
            //最下面不能下移
            if (Objects.isNull(behindScript)) {
                return;
            }
            //交换位置
            Integer tepm = scriptNow.getSort();
            scriptNow.setSort(behindScript.getSort());
            behindScript.setSort(tepm);
            autoScriptMapper.updateById(scriptNow);
            autoScriptMapper.updateById(behindScript);
        } catch (Exception e) {
            log.error("脚本下移失败,原因是" + e.getMessage());
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
            //查看脚本文件是否上传
            if (!ipfsService.filesStat(scriptPath)) {
                throw new UnsupportedOperationException(":脚本文件没有上传！");
            }
//            String videoPath ="/home/" +autoScript.getVideo()+"/"+autoScript.getVideoName();
//            //查看脚本文件是否上传
//            if(!ipfsService.filesStat(videoPath)){
//                throw new UnsupportedOperationException(":脚本视频文件没有上传！");
//            }
            //判断方案名称 项目名称是否存在
            // AutoPlan plan = getAutoPlan(autoScript.getPlanName(),autoScript.getProjectName());
            //判断该脚本是否唯一
            QueryWrapper<AutoScript> scriptQueryWrapper = new QueryWrapper<>();
            scriptQueryWrapper.eq("script_name", autoScript.getScriptName());
            scriptQueryWrapper.eq("script_content", autoScript.getScriptContent());
            scriptQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
            List<AutoScript> list = this.list(scriptQueryWrapper);
            if (!IscTools.isCollection(list) && list.size() > 0) {
                throw new UnsupportedOperationException("该脚本已经上传，不可重复上传");
            }
            AutoScript script = new AutoScript();
            script.setIdel(CommonConstant.DATA_INT_IDEL_0);
            script.setWeight(0);
            //保存脚本
            script.setScriptName(autoScript.getFileName());
            script.setFileName(autoScript.getFileName());
            script.setScriptContent(autoScript.getScriptContent());
            script.setIdel(CommonConstant.DATA_INT_IDEL_0);
            script.setWeight(0);
            script.setTestCaseId(autoScript.getTestCaseId());
            script.setTestCaseName(autoScript.getTestCaseName());
            this.save(script);
            //如果存在视频文件则保存脚本视频
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
            log.info("添加脚本失败 ，原因是" + e.getMessage());
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
            throw new UnsupportedOperationException("这个项目下的方案不存在");
        }
        return plan;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveScriptText(List<ScriptViewVo> scriptViewVos, String scriptId) {
        File localFile = null;
        File pyLocalFile = null;
        try {
            //可视化文件名称
            AutoScript script = this.getById(scriptId);
            if (script == null
                    || StringUtils.isBlank(script.getScriptContent())
                    || StringUtils.isBlank(script.getScriptName())
            ) {
                log.info(script + "脚本相关文件不能为空");
                throw new UnsupportedOperationException("脚本相关文件不能为空");
            }
            //集合转json数组
            String scriptViewJson = JSON.toJSONString(scriptViewVos);
            String viewFileName = scriptViewVos.get(0).getViewFileName();
            log.info("文件名称:" + viewFileName + "可视化文件内容:" + scriptViewJson);
            String ipfsPath = "/home/" + script.getScriptContent() + "/" + script.getScriptName() + "/" + viewFileName;
            String localPath = File.separator + "home" + File.separator + viewFileName;
            localFile = new File(isWindows(localPath));
            //创建本地文件
            createFile(localFile);
            FileOutputStream fos = new FileOutputStream(localFile);
            fos.write(scriptViewJson.getBytes());
            fos.close();
            fos.flush();
            //上传可视化文件
            ipfsService.writeFile(new NamedStreamable.FileWrapper(localFile), ipfsPath, Long.parseLong(script.getScriptContent()));
            //todo 调用接口可视化文件转py脚本 上传文件
            String viewToPy = nodeServiceExt.viewToPy(script.getId(), script.getScriptName(), scriptViewJson);
            if (StringUtils.isBlank(viewToPy)) {
                throw new UnsupportedOperationException("可视化文件转py脚本失败");
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
            log.info("脚本文本保存失败" + e.getMessage());
            return false;
        } finally {
            //删除本地文件
            if (localFile != null) {
                log.info(deleteFile(localFile) ? "成功" + "删除" + localFile.getName() + "文件夹或者文件" : "失败" + "删除" + localFile.getName() + "文件夹或者文件");
            }
            if (pyLocalFile != null) {
                log.info(deleteFile(pyLocalFile) ? "成功" + "删除" + pyLocalFile.getName() + "文件夹或者文件" : "失败" + "删除" + pyLocalFile.getName() + "文件夹或者文件");
            }

        }
    }

    /**
     * @param path 设置的路径
     * @return 操作系统下的路径
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
            //文件拷贝
            String sourceScriptId = sourceScript.getId();
            AutoVideo autoVideo = iAutoVideoService.getVideo(sourceScriptId);
            //原路径 /home/1430469924274438144/脚本
            String snowflakeSourCeDirectoryPath = "/home/" + sourceScript.getScriptContent() + "/" + sourceScript.getScriptName();
            long snowflakeId = snowflakeConfig.snowflakeId();
            //   /home/235666334555
            String snowflakePath = "/home/" + snowflakeId;
            // 目标路径 /home/235666334555/脚本Copy
            String snowflakeDestDirectoryPath = snowflakePath + "/" + desScript.getScriptName();
            //创建新的雪花文件夹
            if (!ipfsService.filesStat(snowflakePath)) {
                if (!ipfsService.filesMkdir(snowflakePath)) {
                    throw new UnsupportedOperationException("初始化目录失败!");
                }
            }
            //脚本拷贝
            if (ipfsService.fileCp(snowflakeSourCeDirectoryPath, snowflakeDestDirectoryPath)) {
               /* //新增脚本sort添加1
                List<AutoScript> autoScriptList = this.list(new QueryWrapper<AutoScript>().eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0));
                AutoScript autoScript = autoScriptList.stream().max(Comparator.comparing(AutoScript::getSort)).get();
                Integer sort = autoScript.getSort() + 1;*/
                //新增脚本 改变名称 备注 雪花路径
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
                    //备份视频
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
                    //拷贝参数 获取原脚本参数
                    List<ScriptParameter> parameterCopyList = iScriptParameterService.list(new QueryWrapper<ScriptParameter>()
                            .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                            .eq(CommonConstant.DATA_STRING_SCRIPT_ID, desScript.getId())
                    );
                    parameterCopyList.forEach(scriptParameter -> {
                        scriptParameter.setScriptId(sourceScript.getId());
                        scriptParameter.setId(null);
                        scriptParameter.setIdel(CommonConstant.DATA_INT_IDEL_0);
                    });
                    //批量添加参数
                    iScriptParameterService.saveBatch(parameterCopyList);
                } else {
                    throw new UnsupportedOperationException("脚本拷贝失败!");
                }
            } else {
                throw new UnsupportedOperationException("脚本拷贝失败!");
            }
        } catch (Exception e) {
            log.error("拷贝脚本失败 ，原因是" + e.getMessage());
            throw new UnsupportedOperationException("拷贝脚本失败 ，原因是" + e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public List<ScriptViewVo> getViewContentByScriptId(String scriptId) {
        BufferedReader br = null;
        StringBuilder viewResult = new StringBuilder();
        File snowFolder = null;
        try {
            //根据雪花id下载脚本文件
            AutoScript script = this.getById(scriptId);
            //雪花路径
            String localPath = "/home/" + script.getScriptContent();
            //查询文件夹hash
            String scriptPath = localPath + "/" + script.getScriptName();
            String fileHash = ipfsService.filesStatHash(scriptPath);
            //下载文件到本地
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
                            //构造一个BufferedReader类来读取文件
                            br = new BufferedReader(new FileReader(f));
                            String s;
                            while ((s = br.readLine()) != null) {
                                //使用readLine方法，一次读一行
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

                            //String 转 集合
                            viewVos.forEach(scriptViewVo -> {
                                //    可视化文件名
                                scriptViewVo.setViewFileName(f.getName());
                                if (scriptViewVo.getObject().contains("断言存在")) {
                                    String value = scriptViewVo.getValue();
                                    //获取图片后缀
                                    String imgName = "";
                                    for (String format : imgFormatList) {
                                        if (value.contains(format)) {
                                            //字符串 format 第一次出现的位置
                                            String subName = value.substring(0, value.indexOf(format));
                                            int begin = subName.lastIndexOf("r");
                                            String imgNameNoSuffix = subName.substring(begin + 2).replace("\"", "");
                                            //名称加后缀
                                            imgName = imgNameNoSuffix + format;
                                            log.info("图片名称:" + imgName);
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
                throw new UnsupportedOperationException("文件下载失败!");
            }
        } catch (IOException e) {
            log.info("获取可视化文本失败 ，原因是" + e.getMessage());
            return null;
        } finally {
            if (br != null) {
                br.close();
            }
            //删除下载到本地的文件
            if (snowFolder != null) {
                log.info(deleteFile(snowFolder) ? "成功" + "删除" + snowFolder.getName() + "文件夹或者文件" : "失败" + "删除" + snowFolder.getName() + "文件夹或者文件");
            }
        }
        return null;
    }

    @Override
    public boolean addScript(AutoScriptVo autoScript) {
        try {
            String scriptPath = "/home/" + autoScript.getScriptContent() + "/" + autoScript.getScriptName();
            //查看脚本文件是否上传
            if (!ipfsService.filesStat(scriptPath)) {
                throw new UnsupportedOperationException(":脚本文件没有上传！");
            }
//            String videoPath ="/home/" +autoScript.getVideo()+"/"+autoScript.getVideoName();
//            //查看脚本文件是否上传
//            if(!ipfsService.filesStat(videoPath)){
//                throw new UnsupportedOperationException(":脚本视频文件没有上传！");
//            }
            //判断方案名称 项目名称是否存在
            // AutoPlan plan = getAutoPlan(autoScript.getPlanName(),autoScript.getProjectName());
            //判断该脚本是否唯一
            QueryWrapper<AutoScript> scriptQueryWrapper = new QueryWrapper<>();
            scriptQueryWrapper.eq("script_name", autoScript.getScriptName());
            scriptQueryWrapper.eq("script_content", autoScript.getScriptContent());
            scriptQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
            List<AutoScript> list = this.list(scriptQueryWrapper);
            if (!IscTools.isCollection(list) && list.size() > 0) {
                throw new UnsupportedOperationException("该脚本已经上传，不可重复上传");
            }
            //保存脚本
            AutoScript script = new AutoScript();
            BeanUtils.copyProperties(autoScript, script);
            script.setIdel(CommonConstant.DATA_INT_IDEL_0);
            script.setWeight(CommonConstant.DATA_INT_IDEL_0);
            this.save(script);
            //如果存在视频文件则保存脚本视频
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
            log.info("添加脚本失败 ，原因是" + e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<AutoScript> updateNodesAddScript(RunningCaseVO runningCaseVO, Integer type, List<AutoNodes> autoNodesList) {
        try {
            boolean rollback = false;
            //脚本名称-测试用例名称+节点名称+轮次
            List<String> scriptNames = new ArrayList<>();
            Map<String, AutoNodes> nodesMap = new HashMap<>();
            //修改节点状态-录制状态 state 1
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
            //新增修改测试用例节点信息
            UpdateWrapper<AuToRunningCase> updateRunningCaseWrapper = new UpdateWrapper<>();
            updateRunningCaseWrapper.eq(CommonConstant.DATA_STRING_ID, runningCaseVO.getId());
            if (CommonConstant.DATA_INT_0.equals(type)) {
                updateRunningCaseWrapper.set("start_time", new Date());
                updateRunningCaseWrapper.set("node_ids", autoNodesList.stream().map(AutoNodes::getId).collect(Collectors.joining(",")));
            } else {
                updateRunningCaseWrapper.set("end_time", new Date());
            }
            if (!autoRunningCaseService.update(updateRunningCaseWrapper)) {
                throw new UnsupportedOperationException("启动/停止-修改测试用例失败!");
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
                throw new UnsupportedOperationException("启动/停止-修改节点状态-录制状态失败!");
            }
            if (autoScripts.size() > 0) {
                if (!this.saveOrUpdateBatch(autoScripts)) {
                    rollback = true;
                }
            }
            if (rollback) {
                throw new UnsupportedOperationException("修改节点状态新增脚本失败,请联系管理员处理!");
            }
            return autoScripts;
        } catch (Exception e) {
            log.error("修改节点状态新增脚本异常，原因:" + e.getMessage());
            throw new UnsupportedOperationException("修改节点状态新增脚本异常,原因:" + e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void playBackScript(List<AutoScript> autoScriptList, String id, Integer type) {
        try {
            //测试用例/脚本新增记录
            autoScriptList.forEach(n -> {
                if (StringUtils.isEmpty(n.getRecordNodeIp())) {
                    throw new UnsupportedOperationException("下发脚本列表-脚本:" + n.getScriptName() + "执行节点IP没有值");
                }
            });
            if (CommonConstant.DATA_INT_0.equals(type)) {
                String autoResultId = autoResultService.addRecord(id, type);
                //循环下发脚本
                autoScriptList.forEach(n -> {
                    RunningCaseVO runningCaseVo = autoRunningCaseService.getRunningCaseVoByCaseId(n.getTestCaseId());
                    if (runningCaseVo == null) {
                        throw new UnsupportedOperationException("下发脚本列表-测试用例找不到");
                    }
                    n.setState(CommonConstant.DATA_STR_2);
                    nodeLogService.ascyncPlayBackScript(runningCaseVo,n,type,autoResultId);
                });
            }
            //下发脚本 type 1
            if (CommonConstant.DATA_INT_1.equals(type)) {
                //循环下发脚本
                autoScriptList.forEach(n -> {
                    RunningCaseVO runningCaseVo = autoRunningCaseService.getRunningCaseVoByCaseId(n.getTestCaseId());
                    if (runningCaseVo == null) {
                        throw new UnsupportedOperationException("下发脚本列表-测试用例找不到");
                    }
                    n.setState(CommonConstant.DATA_STR_2);
                    nodeLogService.ascyncPlayBackScript(runningCaseVo,n,type,"");
                });
            }
        } catch (Exception e) {
            log.error("下发脚本列表异常，原因:" + e.getMessage());
            throw new UnsupportedOperationException("下发脚本列表异常,原因:" + e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String playBackStartScript(String scriptCode) {
        try {
            AutoScript autoScript = this.getById(scriptCode);
            if (autoScript != null) {
                //根据脚本id查找测试用例信息
                RunningCaseVO runningCaseVo = autoRunningCaseService.getRunningCaseVoByCaseId(autoScript.getTestCaseId());
                //测试用例/脚本新增记录
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
                //TODO 测试阶段默认成功
                //autoScript.setState(CommonConstant.DATA_STR_3);
                if (nodeServiceExt.startScript(nodeStartScriptPara)) {
                    autoScript.setState(CommonConstant.DATA_STR_3);
                } else {
                    throw new UnsupportedOperationException("执行脚本：" + autoScript.getScriptName() + "失败");
                }
            }
            if (!this.updateById(autoScript)) {
                throw new UnsupportedOperationException("启动脚本-更新脚本状态失败");
            }
            return CommonConstant.DATA_STR_0;
        } catch (Exception e) {
            log.error("启动脚本异常，原因:" + e.getMessage());
            throw new UnsupportedOperationException("启动脚本异常,原因:" + e.getMessage());
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
            log.info("设置权重失败，原因是：" + e.getMessage());
            return false;
        }
    }
}
