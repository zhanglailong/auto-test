package org.jeecg.modules.runscriptresult.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.runscriptresult.entity.RunScriptResult;
import org.jeecg.modules.runscriptresult.service.IRunScriptResultService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 运行脚本记录下脚本结果
 * @Author: jeecg-boot
 * @Date:   2021-12-13
 * @Version: V1.0
 */
@Api(tags="运行脚本记录下脚本结果")
@RestController
@RequestMapping("/runscriptresult/runScriptResult")
@Slf4j
public class RunScriptResultController extends JeecgController<RunScriptResult, IRunScriptResultService> {
	@Autowired
	private IRunScriptResultService runScriptResultService;
	
	/**
	 * 分页列表查询
	 *
	 * @param runScriptResult
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "运行脚本记录下脚本结果-分页列表查询")
	@ApiOperation(value="运行脚本记录下脚本结果-分页列表查询", notes="运行脚本记录下脚本结果-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunScriptResult runScriptResult,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunScriptResult> queryWrapper = QueryGenerator.initQueryWrapper(runScriptResult, req.getParameterMap());
		queryWrapper.eq(CommonConstant.DATA_STRING_IDEL,CommonConstant.DATA_INT_IDEL_0);
		if(StringUtils.isNotBlank(runScriptResult.getScriptRecordId())){
			queryWrapper.eq(CommonConstant.SCRIPT_RECORD_ID,runScriptResult.getScriptRecordId());
		}
		Page<RunScriptResult> page = new Page<RunScriptResult>(pageNo, pageSize);
		IPage<RunScriptResult> pageList = runScriptResultService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param runScriptResult
	 * @return
	 */
	@AutoLog(value = "运行脚本记录下脚本结果-添加")
	@ApiOperation(value="运行脚本记录下脚本结果-添加", notes="运行脚本记录下脚本结果-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunScriptResult runScriptResult) {
		runScriptResultService.save(runScriptResult);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runScriptResult
	 * @return
	 */
	@AutoLog(value = "运行脚本记录下脚本结果-编辑")
	@ApiOperation(value="运行脚本记录下脚本结果-编辑", notes="运行脚本记录下脚本结果-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunScriptResult runScriptResult) {
		runScriptResultService.updateById(runScriptResult);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运行脚本记录下脚本结果-通过id删除")
	@ApiOperation(value="运行脚本记录下脚本结果-通过id删除", notes="运行脚本记录下脚本结果-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runScriptResultService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "运行脚本记录下脚本结果-批量删除")
	@ApiOperation(value="运行脚本记录下脚本结果-批量删除", notes="运行脚本记录下脚本结果-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runScriptResultService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运行脚本记录下脚本结果-通过id查询")
	@ApiOperation(value="运行脚本记录下脚本结果-通过id查询", notes="运行脚本记录下脚本结果-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunScriptResult runScriptResult = runScriptResultService.getById(id);
		if(runScriptResult==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(runScriptResult);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runScriptResult
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunScriptResult runScriptResult) {
        return super.exportXls(request, runScriptResult, RunScriptResult.class, "运行脚本记录下脚本结果");
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
        return super.importExcel(request, response, RunScriptResult.class);
    }

}
