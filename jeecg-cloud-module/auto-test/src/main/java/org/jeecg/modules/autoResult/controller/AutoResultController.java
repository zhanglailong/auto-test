package org.jeecg.modules.autoResult.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.autoResult.entity.AutoResult;
import org.jeecg.modules.autoResult.entity.AutoResultVo;
import org.jeecg.modules.autoResult.service.IAutoResultService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.file.controller.FileController;
import org.jeecg.modules.fileUpAndDownload.UpDownFileController;
import org.jeecg.modules.plan.entity.AutoPlan;
import org.jeecg.modules.plan.service.IAutoPlanService;
import org.jeecg.modules.project.service.IAutoProjectService;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.jeecg.modules.service.IpfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 测试结果管理
 * @Author: jeecg-boot
 * @Date: 2021-08-17
 * @Version: V1.0
 */
@Api(tags = "测试结果管理")
@RestController
@RequestMapping("/autoResult")
@Slf4j
public class AutoResultController extends JeecgController<AutoResult, IAutoResultService> {
    @Autowired
    private IAutoResultService autoResultService;
    @Autowired
    private IAutoScriptService autoScriptService;
    @Autowired
    private IAutoProjectService autoProjectService;
    @Autowired
    private IAutoPlanService autoPlanService;
    @Autowired
    private FileController fileController;
    @Resource
    IpfsService ipfsService;


    /**
     * 分页列表查询
     *
     * @param autoResult
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "测试结果管理-分页列表查询")
    @ApiOperation(value = "测试结果管理-分页列表查询", notes = "测试结果管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AutoResult autoResult,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<AutoResult> queryWrapper = QueryGenerator.initQueryWrapper(autoResult, req.getParameterMap());
        queryWrapper.getExpression().getOrderBy().remove(0);
        queryWrapper.orderByDesc(CommonConstant.DATA_START_TIME);
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL,CommonConstant.DATA_INT_IDEL_0);
        if(StringUtils.isNotBlank(autoResult.getTestCaseId())){
            queryWrapper.eq(CommonConstant.DATA_STRING_TEST_CASE_ID,autoResult.getTestCaseId());
        }
        if(StringUtils.isNotBlank(autoResult.getTestItemId())){
            queryWrapper.eq(CommonConstant.DATA_STRING_TEST_ITEM_ID,autoResult.getTestItemId());
        }
        if(StringUtils.isNotBlank(autoResult.getProjectId())){
            queryWrapper.eq(CommonConstant.DATA_STRING_PROJECT_ID,autoResult.getProjectId());
        }
        Page<AutoResult> page = new Page<AutoResult>(pageNo, pageSize);
        IPage<AutoResult> pageList = autoResultService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param autoResult
     * @return
     */
    @AutoLog(value = "测试结果管理-添加")
    @ApiOperation(value = "测试结果管理-添加", notes = "测试结果管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AutoResult autoResult) {
        autoResultService.save(autoResult);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param autoResult
     * @return
     */
    @AutoLog(value = "测试结果管理-编辑")
    @ApiOperation(value = "测试结果管理-编辑", notes = "测试结果管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AutoResult autoResult) {
        autoResultService.updateById(autoResult);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试结果管理-通过id删除")
    @ApiOperation(value = "测试结果管理-通过id删除", notes = "测试结果管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        autoResultService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "测试结果管理-批量删除")
    @ApiOperation(value = "测试结果管理-批量删除", notes = "测试结果管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.autoResultService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试结果管理-通过id查询")
    @ApiOperation(value = "测试结果管理-通过id查询", notes = "测试结果管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AutoResult autoResult = autoResultService.getById(id);
        if (autoResult == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(autoResult);
    }
    /**
     * 通过方案id查询总状态
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试结果管理-通过方案id查询总状态")
    @ApiOperation(value = "测试结果管理-通过方案id查询总状态", notes = "测试结果管理-通过方案id查询总状态")
    @GetMapping(value = "/queryStateByPlanId")
    public Result<?> queryStateByPlanId(@RequestParam(name = "id", required = true) String id) {
        QueryWrapper<AutoScript> queryWrapper =new QueryWrapper();
        queryWrapper.eq("plan_id",id);
        List<AutoScript> autoScripts = autoScriptService.list(queryWrapper);
        for (int i=0;i<autoScripts.size();i++){
            AutoScript autoScript = autoScripts.get(i);
            String autoScriptId = autoScript.getId();
            QueryWrapper<AutoResult> queryWrapperResult=new QueryWrapper<AutoResult>();
            queryWrapperResult.eq("auto_script_id",autoScriptId);
            AutoResult autoResult = autoResultService.getOne(queryWrapperResult);
            if (autoResult == null) {
                return Result.error("未找到对应数据");
            }
            if (autoResult.getState()==1){
                return Result.OK(autoResult.getState());
            }
        }
        return Result.OK(0);
    }
    /**
     * 通过方案id查询总结果
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试结果管理-通过方案id查询总结果")
    @ApiOperation(value = "测试结果管理-通过方案id查询总结果", notes = "测试结果管理-通过方案id查询总结果")
    @GetMapping(value = "/queryResultByPlanId")
    public Result<?> queryResultByPlanId(@RequestParam(name = "id", required = true) String id) {
        QueryWrapper<AutoScript> queryWrapper =new QueryWrapper();
        queryWrapper.eq("plan_id",id);
        List<AutoScript> autoScripts = autoScriptService.list(queryWrapper);
        for (int i=0;i<autoScripts.size();i++){
            AutoScript autoScript = autoScripts.get(i);
            String autoScriptId = autoScript.getId();
            QueryWrapper<AutoResult> queryWrapperResult=new QueryWrapper<AutoResult>();
            queryWrapperResult.eq("auto_script_id",autoScriptId);
            AutoResult autoResult = autoResultService.getOne(queryWrapperResult);
            if (autoResult == null) {
                return Result.error("未找到对应数据");
            }
            if (autoResult.getResult()==1){
                return Result.OK(autoResult.getResult());
            }
        }
        return Result.OK(0);
    }
//    /**
//     * 通过方案id下载全部
//     *
//     * @param id
//     * @return
//     */
//    @AutoLog(value = "测试结果管理-通过方案id下载全部")
//    @ApiOperation(value = "测试结果管理-通过方案id下载全部", notes = "测试结果管理-通过方案id下载全部")
//    @GetMapping(value = "/allDownload")
//    public Result<?> allDownload(@RequestParam(name = "id", required = true) String id) {
//        QueryWrapper<AutoScript> queryWrapper =new QueryWrapper();
//        queryWrapper.eq("plan_id",id);
//        List<AutoScript> autoScripts = autoScriptService.list(queryWrapper);
//        for (int i=0;i<autoScripts.size();i++){
//            AutoScript autoScript = autoScripts.get(i);
//            String autoScriptId = autoScript.getId();
//            QueryWrapper<AutoResult> queryWrapperResult=new QueryWrapper<AutoResult>();
//            queryWrapperResult.eq("auto_script_id",autoScriptId);
//            AutoResult autoResult = autoResultService.getOne(queryWrapperResult);
//            if (autoResult == null) {
//                return Result.error("未找到对应数据");
//            }
//            fileController.download(autoResult.getReport());
//        }
//        return Result.OK("全部下载完成");
//    }
    /**
     * 通过方案id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试结果管理-通过方案id查询")
    @ApiOperation(value = "测试结果管理-通过方案id查询", notes = "测试结果管理-通过方案id查询")
    @GetMapping(value = "/queryByPlanId")
    public Result<?> queryByPlanId(@RequestParam(name = "id", required = true) String id) {
        QueryWrapper<AutoScript> queryWrapper =new QueryWrapper();
        queryWrapper.eq("plan_id",id);
        List<AutoScript> autoScripts = autoScriptService.list(queryWrapper);
        List<AutoResultVo> autoResultVolist=new ArrayList<>();
        for (int i=0;i<autoScripts.size();i++){
            AutoResultVo autoResultVo=new AutoResultVo();
            AutoScript autoScript = autoScripts.get(i);
            String autoScriptId = autoScript.getId();
            QueryWrapper<AutoResult> queryWrapperResult=new QueryWrapper<AutoResult>();
            queryWrapperResult.eq("auto_script_id",autoScriptId);
            List<AutoResult> autoResultList = autoResultService.list(queryWrapperResult);
            for (i=0;i<autoResultList.size();i++){
                AutoResult autoResult = autoResultList.get(i);
                autoResultVo.setAutoResultId(autoResult.getId());
                autoResultVo.setCreateBy(autoResult.getCreateBy());
                autoResultVo.getCreateTime(autoResult.getCreateTime());
                autoResultVo.getUpdateBy(autoResult.getUpdateBy());
                autoResultVo.getUpdateTime(autoResult.getUpdateTime());
                autoResultVo.getState(autoResult.getResult());
                autoResultVo.getResult(autoResult.getResult());
                autoResultVo.setProjectId(autoResult.getProjectId());
                autoResultVo.setProjectName(autoResult.getProjectName());
                autoResultVo.setNodeName(autoScript.getRecordNodeName());
                autoResultVo.setRecordNodeId(autoScript.getRecordNodeId());
                autoResultVolist.add(autoResultVo);
            }
        }
        return Result.OK(autoResultVolist);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param autoResult
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AutoResult autoResult) {
        return super.exportXls(request, autoResult, AutoResult.class, "测试结果管理");
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
        return super.importExcel(request, response, AutoResult.class);
    }
}
