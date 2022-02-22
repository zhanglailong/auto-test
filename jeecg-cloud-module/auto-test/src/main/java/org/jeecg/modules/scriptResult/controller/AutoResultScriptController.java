package org.jeecg.modules.scriptResult.controller;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.ipfs.api.IPFS;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.autoResult.entity.AutoResult;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.FileUtils;
import org.jeecg.modules.runscriptresult.entity.RunScriptResult;
import org.jeecg.modules.runscriptresult.service.IRunScriptResultService;
import org.jeecg.modules.scriptResult.entity.AutoResultScript;
import org.jeecg.modules.scriptResult.service.IAutoResultScriptService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.service.IpfsService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static org.jeecg.modules.common.FileUtils.deleteLocalFile;

/**
 * @Description: 方案下脚本报告
 * @Author: jeecg-boot
 * @Date: 2021-09-02
 * @Version: V1.0
 */
@Api(tags = "方案下脚本报告")
@RestController
@RequestMapping("/scriptResult/autoResultScript")
@Slf4j
public class AutoResultScriptController extends JeecgController<AutoResultScript, IAutoResultScriptService> {
    @Autowired
    private IAutoResultScriptService autoResultScriptService;
    @Resource
    private IRunScriptResultService runScriptResultService;
	@Resource
	IpfsService ipfsService;
    @Value(value = "${ipfs.queryUrl}")
    private String ipfsQueryUrl;


    /**
     * 分页列表查询
     *
     * @param autoResultScript
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "方案下脚本报告-分页列表查询")
    @ApiOperation(value = "方案下脚本报告-分页列表查询", notes = "方案下脚本报告-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AutoResultScript autoResultScript,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<AutoResultScript> queryWrapper = QueryGenerator.initQueryWrapper(autoResultScript, req.getParameterMap());
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        if (StringUtils.isNotBlank(autoResultScript.getPlanResultId())) {
            queryWrapper.eq(CommonConstant.DATA_STRING_PLAN_RESULT_ID, autoResultScript.getPlanResultId());
        }
        Page<AutoResultScript> page = new Page<AutoResultScript>(pageNo, pageSize);
        IPage<AutoResultScript> pageList = autoResultScriptService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param autoResultScript
     * @return
     */
    @AutoLog(value = "方案下脚本报告-添加")
    @ApiOperation(value = "方案下脚本报告-添加", notes = "方案下脚本报告-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AutoResultScript autoResultScript) {
        autoResultScriptService.save(autoResultScript);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param autoResultScript
     * @return
     */
    @AutoLog(value = "方案下脚本报告-编辑")
    @ApiOperation(value = "方案下脚本报告-编辑", notes = "方案下脚本报告-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AutoResultScript autoResultScript) {
        autoResultScriptService.updateById(autoResultScript);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "方案下脚本报告-通过id删除")
    @ApiOperation(value = "方案下脚本报告-通过id删除", notes = "方案下脚本报告-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        autoResultScriptService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "方案下脚本报告-批量删除")
    @ApiOperation(value = "方案下脚本报告-批量删除", notes = "方案下脚本报告-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.autoResultScriptService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "方案下脚本报告-通过id查询")
    @ApiOperation(value = "方案下脚本报告-通过id查询", notes = "方案下脚本报告-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AutoResultScript autoResultScript = autoResultScriptService.getById(id);
        if (autoResultScript == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(autoResultScript);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param autoResultScript
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AutoResultScript autoResultScript) {
        return super.exportXls(request, autoResultScript, AutoResultScript.class, "方案下脚本报告");
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
        return super.importExcel(request, response, AutoResultScript.class);
    }

	/**
	 * 方案报告下脚本执行的报告下载
	 */
	@AutoLog(value = "方案报告下脚本执行的报告下载")
	@ApiOperation(value = "方案报告下脚本执行的报告下载", notes = "方案报告下脚本执行的报告下载")
	@GetMapping(value = "/planReportDownload")
	public Result<?> planReportDownload(HttpServletResponse response,@RequestParam(name="id") String id) {

//        try {
//            if (StringUtils.isEmpty(id)){
//                return Result.error("缺少Id");
//            }
//            AutoResultScript autoResultScript = autoResultScriptService.getById(id);
//            if(StringUtils.isBlank(autoResultScript.getReport())){
//                return Result.error("该脚本没有测试结果");
//            }
//
//            String snowflakeDirectoryPath =  "/home/" + autoResultScript.getReport()+"/"+autoResultScript.getAutoScriptName();
//            Object filesLs = ipfsService.filesLs(snowflakeDirectoryPath);
//            List<JSONObject> objectList = JSONObject.parseObject(JSON.toJSONString(filesLs), new TypeReference<List<JSONObject>>() {});
//            String reportPath= snowflakeDirectoryPath+"/"+objectList.get(0).get("Name").toString();
//            String fileFormat = reportPath.substring(reportPath.indexOf(".")+CommonConstant.DATA_INT_IDEL_1, reportPath.length());
//            String reportHash = ipfsService.filesStatHash(reportPath);
//            String fileUrl=ipfsQueryUrl+reportHash+"_"+fileFormat;
//            return Result.OK(fileUrl);
//        } catch (Exception e) {
//            log.error("查看结果结果失败"+e.getMessage());
//            return Result.error("查看结果结果失败 "+e.getMessage());
//        }

        //   }
        String fileHash = null;
        try {
            String scriptName = "";
            AutoResultScript autoResultScript = autoResultScriptService.getById(id);
            if(Objects.isNull(autoResultScript)){
                RunScriptResult runScriptResult = runScriptResultService.getById(id);
                scriptName = runScriptResult.getAutoScriptName();
                fileHash = runScriptResult.getReport();
            }else {
                fileHash = autoResultScript.getReport();
                scriptName = autoResultScript.getAutoScriptName();
            }
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
            String downloadUrl = ipfsService.download("/" + fileHash, hash);
            if (StringUtils.isNotBlank(downloadUrl)) {
                //压缩文件
                FileUtils.compress(downloadUrl, CommonConstant.DATA_FORMAT_ZIP);
                //删除下载的文件夹
                deleteLocalFile(fileHash);
                if (StringUtils.isNotBlank(downloadUrl)) {
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
}
