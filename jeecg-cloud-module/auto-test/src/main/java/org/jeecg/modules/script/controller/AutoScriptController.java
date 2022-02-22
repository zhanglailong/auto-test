package org.jeecg.modules.script.controller;

import java.io.File;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.config.SnowflakeConfig;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.FileUtils;
import org.jeecg.modules.common.IscTools;
import org.jeecg.modules.file.service.FileService;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.entity.AutoScriptVo;
import org.jeecg.modules.script.entity.ReturnUpload;
import org.jeecg.modules.script.entity.ScriptViewVo;
import org.jeecg.modules.script.service.IAutoScriptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.scriptandplan.entity.AutoScriptPlan;
import org.jeecg.modules.service.IpfsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static org.jeecg.modules.common.FileUtils.*;

/**
 * @Description: 脚本管理
 * @Author: zll
 * @Date: 2021-08-17
 * @Version: V1.0
 */
@Api(tags = "脚本管理")
@RestController
@RequestMapping("/script/autoScript")
@Slf4j
public class AutoScriptController extends JeecgController<AutoScript, IAutoScriptService> {
    @Autowired
    private IAutoScriptService autoScriptService;
    @Resource
    SnowflakeConfig snowflakeConfig;
    @Resource
    FileService fileService;
    @Resource
    IpfsService ipfsService;


    /**
     * 分页列表查询,以及查看方案下的脚本
     *
     * @param autoScript
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "脚本管理及节点下的脚本-分页列表查询")
    @ApiOperation(value = "脚本管理及方案下和节点下脚本-分页列表查询", notes = "脚本管理及方案下和节点下的脚本-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AutoScript autoScript,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<AutoScript> queryWrapper = QueryGenerator.initQueryWrapper(autoScript, req.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.getExpression().getOrderBy().remove(0);
        queryWrapper.orderByAsc(CommonConstant.DATA_STRING_SORT);
        queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        if (StringUtils.isNotBlank(autoScript.getRecordNodeId())) {
            queryWrapper.eq(CommonConstant.DATA_STRING_NODEID, autoScript.getRecordNodeId());
        }
        Page<AutoScript> page = new Page<AutoScript>(pageNo, pageSize);
        IPage<AutoScript> pageList = autoScriptService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 分页列表查询,以及查看方案下的脚本
     *
     * @param autoScript
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "脚本管理及节点下的脚本-分页列表查询")
    @ApiOperation(value = "脚本管理及方案下和节点下脚本-分页列表查询", notes = "脚本管理及方案下和节点下的脚本-分页列表查询")
    @GetMapping(value = "/bindList")
    public Result<?> queryBindPageList(AutoScript autoScript,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        QueryWrapper<AutoScript> queryWrapper = QueryGenerator.initQueryWrapper(autoScript, req.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        queryWrapper.eq(CommonConstant.DATA_STRING_TEST_CASE_ID, CommonConstant.DATA_INT_IDEL_0);
        Page<AutoScript> page = new Page<AutoScript>(pageNo, pageSize);
        IPage<AutoScript> pageList = autoScriptService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 导入
     *
     * @param autoScript
     * @return
     */
    @AutoLog(value = "脚本管理-导入")
    @ApiOperation(value = "脚本管理-导入", notes = "脚本管理-导入")
    @PostMapping(value = "/inport")
    public Result<?> inportScript(@RequestBody AutoScriptVo autoScript) {
        try {
            if (Objects.isNull(autoScript)) {
                return Result.error("对象不能为空");
            }
            if (autoScriptService.inportScript(autoScript)) {
                return Result.OK("导入成功！");
            }
        } catch (Exception e) {
            log.error("导入失败" + e.getMessage());
            return Result.error(e.getMessage());
        }
        return Result.error("导入失败");
    }

    @AutoLog(value = "脚本管理-添加")
    @ApiOperation(value = "脚本管理-添加", notes = "脚本管理-添加")
    @PostMapping(value = "/add")
    public Result<?> addScript(@RequestBody AutoScriptVo autoScript) {
        try {
            if (Objects.isNull(autoScript)) {
                return Result.error("对象不能为空");
            }
            if (autoScriptService.addScript(autoScript)) {
                return Result.OK("添加成功！");
            }
        } catch (Exception e) {
            log.error("添加失败" + e.getMessage());
            return Result.error(e.getMessage());
        }
        return Result.error("添加失败");
    }


    /**
     * 编辑
     *
     * @param autoScript
     * @return
     */
    @AutoLog(value = "脚本管理-编辑")
    @ApiOperation(value = "脚本管理-编辑", notes = "脚本管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AutoScriptVo autoScript) {
        try {
            if (StringUtils.isEmpty(autoScript.getId())) {
                return Result.OK("缺少参数");
            }
            if (autoScriptService.edit(autoScript)) {
                return Result.OK("修改成功!");
            }
            return Result.error("修改失败");
        } catch (Exception e) {
            return Result.error("修改失败异常:" + e.getMessage());
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "脚本管理-通过id删除")
    @ApiOperation(value = "脚本管理-通过id删除", notes = "脚本管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                return Result.error("缺少参数");
            }
            autoScriptService.delete(id);
            return Result.OK("删除成功!");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "脚本管理-批量删除")
    @ApiOperation(value = "脚本管理-批量删除", notes = "脚本管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.autoScriptService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "脚本管理-通过id查询")
    @ApiOperation(value = "脚本管理-通过id查询", notes = "脚本管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AutoScript autoScript = autoScriptService.getById(id);
        if (autoScript == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(autoScript);
    }

    /**
     * 绑定脚本的树结构
     */
    @AutoLog(value = "绑定脚本的树结构")
    @ApiOperation(value = "绑定脚本的树结构", notes = "绑定脚本的树结构")
    @GetMapping(value = "/queryTreeScript")
    public Result<?> treeScript() {
        try {
            List<AutoScript> treeScriptList = autoScriptService.treeScriptList();
            if (CollectionUtils.isEmpty(treeScriptList)) {
                return Result.error("暂未查到数据");
            }
            return Result.OK(treeScriptList);
        } catch (Exception e) {
            log.error("查询失败" + e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 脚本的上移
     */
    @AutoLog(value = "脚本的上移")
    @ApiOperation(value = "脚本的上移", notes = "脚本的上移")
    @PostMapping(value = "/scriptUp")
    public Result<?> scriptUp(@RequestBody AutoScript autoScript) {
        try {
            autoScriptService.scriptUp(autoScript);
            return Result.OK("脚本上移成功");
        } catch (Exception e) {
            return Result.error("脚本上移失败，原因是  " + e.getMessage());
        }
    }

    /**
     * 脚本的下移
     */
    @AutoLog(value = "脚本的下移")
    @ApiOperation(value = "脚本的下移", notes = "脚本的下移")
    @PostMapping(value = "/scriptDown")
    public Result<?> scriptDown(@RequestBody AutoScript autoScript) {
        try {
            autoScriptService.scriptdown(autoScript);
            return Result.OK("脚本下移成功");
        } catch (Exception e) {
            return Result.error("脚本上下移失败，原因是  " + e.getMessage());
        }
    }

    /**
     * 导出excel
     *
     * @param request
     * @param autoScript
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AutoScript autoScript) {
        return super.exportXls(request, autoScript, AutoScript.class, "脚本管理");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AutoScript.class);
    }


    @AutoLog(value = "脚本文本列表保存")
    @ApiOperation(value = "脚本文本列表保存", notes = "脚本文本列表保存")
    @PostMapping(value = "/saveScriptText")
    public Result<?> saveScriptText(@RequestParam(name = "scriptId") String scriptId, @RequestBody List<ScriptViewVo> scriptViewVos) {
        try {
            if (scriptId == null || scriptViewVos == null || scriptViewVos.size() == 0) {
                return Result.error("脚本id不能为空,或者可视化文本列表不可以为空");
            }
            if (autoScriptService.saveScriptText(scriptViewVos, scriptId)) {
                return Result.OK("脚本文本保存成功！");
            }
        } catch (Exception e) {
            log.error("脚本文本保存异常:" + e.getMessage());
            return Result.error("脚本文本保存异常:" + e.getMessage());
        }
        return Result.error("脚本文本保存失败!");
    }


    @AutoLog(value = "获取脚本可视化文件")
    @ApiOperation(value = "获取脚本可视化文件", notes = "获取脚本可视化文件")
    @PostMapping(value = "/getViewContentByScriptId")
    public Result<?> getViewContentByScriptId(@RequestParam(name = "scriptId") String scriptId) {
        try {
            if (StringUtils.isEmpty(scriptId)) {
                return Result.error("缺少脚本Id");
            }
            AutoScript autoScriptServiceById = autoScriptService.getById(scriptId);
            if(StringUtils.isBlank(autoScriptServiceById.getScriptContent())){
                return Result.error("该可视化文件未上传");
            }
            List<ScriptViewVo> viewVos = autoScriptService.getViewContentByScriptId(scriptId);
            return Result.OK(viewVos);
        } catch (Exception e) {
            log.error("获取脚本可视化文件里面的内容异常" + e.getMessage());
            return Result.error("获取脚本可视化文件里面的内容异常" + e.getMessage());
        }
    }

    /**
     * 脚本的拷贝
     *
     * @param autoScript 脚本对象
     * @return 成功或者失败
     */
    @AutoLog(value = "脚本拷贝-添加")
    @ApiOperation(value = "脚本拷贝-添加", notes = "脚本拷贝-添加")
    @PostMapping(value = "/copy")
    public Result<?> copy(@RequestBody AutoScript autoScript) {
        try {
            AutoScript script = autoScriptService.getById(autoScript.getId());
            if (StringUtils.isBlank(autoScript.getId())
                    || script == null
                    || StringUtils.isBlank(script.getScriptContent())
                    || StringUtils.isBlank(script.getScriptName())
            ) {
                return Result.error("脚本相关信息不能为空！");
            }
            autoScriptService.copyScript(autoScript, script);
            return Result.OK("拷贝成功！");
        } catch (Exception e) {
            log.error("拷贝失败" + e.getMessage());
            return Result.error("拷贝失败" + e.getMessage());
        }
    }


    @AutoLog(value = "分布式文件上传")
    @ApiOperation(value = "分布式文件上传", notes = "分布式文件上传")
    @ResponseBody
    @PostMapping("/fileUpload")
    public Result<?> scriptUpload(@RequestParam("file") MultipartFile file, @RequestParam("filePathId") long filePathId) {
        long snowflakeId;
        if (filePathId > 0) {
            snowflakeId = filePathId;
        } else {
            snowflakeId = snowflakeConfig.snowflakeId();
        }
        //雪花路径
        String snowflakeDirectoryPath = File.separator + "home" + File.separator + snowflakeId;
        if (IscTools.isWindowsOS()) {
            snowflakeDirectoryPath = "C:" + snowflakeDirectoryPath;
        }
        try {
            //获取上传文件名称
            String originalFilename = file.getOriginalFilename();
            if (org.apache.commons.lang.StringUtils.isNotBlank(originalFilename)) {
                //分割文件名成名称加后缀
                String[] fileName = originalFilename.split(fileDot);
                if (fileName.length >= 1) {
                    String path = snowflakeDirectoryPath + File.separator + fileName[0] + "." + fileName[1];
                    File upFile = new File(path);
                    createFile(upFile);
                    file.transferTo(upFile);
                    return Result.OK(fileService.inportFileAndUnCompress(upFile, snowflakeId));
                }
            }
        } catch (Exception e) {
            log.error("文件上传异常" + e.getMessage());
            return Result.error("上传异常： " + e.getMessage());
        } finally {
            //删除本地雪花算法下的整个文件夹
            File snowFolder = new File(snowflakeDirectoryPath);
            System.gc();
            log.info(deleteFile(snowFolder) ? "成功" + "删除" + snowFolder.getName() + "文件夹或者文件" : "失败" + "删除" + snowFolder.getName() + "文件夹或者文件");

        }
        return Result.error("上传失败");
    }

    /**
     * ipfs脚本文件导出
     */
    @AutoLog(value = "ipfs脚本文件导出")
    @ApiOperation(value = "ipfs脚本文件导出", notes = "ipfs脚本文件导出")
    @GetMapping(value = "/export")
    public Result<?> export(HttpServletResponse response,
                            @RequestParam("fileHash") String fileHash,
                            @RequestParam("scriptName") String scriptName) {
        try {
            if (org.apache.commons.lang.StringUtils.isEmpty(fileHash)) {
                return Result.error("文件id不能为空!");
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(scriptName)) {
                return Result.error("文件名称不能为空!");
            }
            String hash = ipfsService.filesStatHash("/home/" + fileHash + "/" + scriptName);
            if (org.apache.commons.lang.StringUtils.isEmpty(hash)) {
                return Result.error("服务器未找到当前脚本文件!");
            }
            Object filesLs = ipfsService.filesLs("/home/" + fileHash + "/" + scriptName);
            List<JSONObject> objectList = JSONObject.parseObject(JSON.toJSONString(filesLs), new TypeReference<List<JSONObject>>() {});
            if(CollectionUtils.isEmpty(objectList)){
                throw new UnsupportedOperationException(scriptName + "脚本文件没有上传！");
            }
            String downloadUrl = ipfsService.download("/" + fileHash, hash);
            if (org.apache.commons.lang.StringUtils.isNotBlank(downloadUrl)) {
                //压缩文件
                FileUtils.compress(downloadUrl, CommonConstant.DATA_FORMAT_ZIP);
                //删除下载的文件夹
                deleteLocalFile(fileHash);
                if (org.apache.commons.lang.StringUtils.isNotBlank(downloadUrl)) {
                    //下载压缩包
                    FileUtils.scriptDown(response, fileHash);
                    //scriptDownload(fileHash,response);
                    return Result.OK("文件下载成功，路径是：" + downloadUrl);
                }
            }
        } catch (Exception e) {
            log.error(fileHash + "文件下载异常" + e.getMessage());
            return Result.error(fileHash + "文件下载异常" + e.getMessage());
        }
        return Result.error(fileHash + "文件不存在");
    }

    /**
     * 绑定脚本
     *
     * @param testCaseId 用例id
     * @param scriptIds  绑定的脚本ids
     * @return
     */
    @AutoLog(value = "方案中绑定脚本")
    @ApiOperation(value = "方案中绑定脚本", notes = "方案中绑定脚本")
    @GetMapping(value = "/bindingScript")
    public Result<?> bindingScript(
            @RequestParam(name = "testCaseId", required = true) String testCaseId,
            @RequestParam(name = "scriptIds", required = true) String scriptIds) {

        String result = autoScriptService.bindingScript(testCaseId, scriptIds);
        if (result.contains(CommonConstant.IS_SUCCESS)) {
            return Result.OK(result);
        } else {
            return Result.error(result);
        }
    }

    /**
     * 用例下的脚本-解除绑定
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用例下的脚本-解除绑定")
    @ApiOperation(value = "用例下的脚本-解除绑定", notes = "用例下的脚本-解除绑定")
    @DeleteMapping(value = "/removeBind")
    public Result<?> noBindingScript(@RequestParam(name = "id", required = true) String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                return Result.error("参数不能为空");
            }
            autoScriptService.removeBind(id);
            return Result.OK("解除绑定成功!");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 设置权重
     *
     * @param autoScript
     * @return
     */
    @AutoLog(value = "设置权重-编辑")
    @ApiOperation(value = "设置权重-编辑", notes = "设置权重-编辑")
    @PutMapping(value = "/setSort")
    public Result<?> setSort(@RequestBody AutoScriptVo autoScript) {
        if (StringUtils.isEmpty(autoScript.getId())) {
            return Result.OK("缺少参数");
        }
        if (autoScriptService.setSort(autoScript)) {
            return Result.OK("设置权重成功");
        }
        return Result.error("设置权重失败");
    }
}
