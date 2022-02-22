package org.jeecg.modules.parameter.controller;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.parameter.entity.ScriptParameter;
import org.jeecg.modules.parameter.service.IScriptParameterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;


/**
 * @author yeyl
 */
@Api(tags="脚本参数")
@RestController
@RequestMapping("/parameter/scriptParameter")
@Slf4j
public class ScriptParameterController extends JeecgController<ScriptParameter, IScriptParameterService> {
	@Resource
	private IScriptParameterService scriptParameterService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scriptParameter scriptParameter
	 * @param pageNo 第几页
	 * @param pageSize 页数
	 * @param req 请求
	 * @return pageList
	 */
	@AutoLog(value = "脚本参数-分页列表查询")
	@ApiOperation(value="脚本参数-分页列表查询", notes="脚本参数-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ScriptParameter scriptParameter,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ScriptParameter> queryWrapper = QueryGenerator.initQueryWrapper(scriptParameter, req.getParameterMap());
		Page<ScriptParameter> page = new Page<>(pageNo, pageSize);
		IPage<ScriptParameter> pageList = scriptParameterService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scriptParameter scriptParameter
	 * @return 成功或者失败
	 */
	@AutoLog(value = "脚本参数-添加")
	@ApiOperation(value="脚本参数-添加", notes="脚本参数-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ScriptParameter scriptParameter) {
		scriptParameterService.save(scriptParameter);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scriptParameter scriptParameter
	 * @return 成功或者失败
	 */
	@AutoLog(value = "脚本参数-编辑")
	@ApiOperation(value="脚本参数-编辑", notes="脚本参数-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ScriptParameter scriptParameter) {
		scriptParameterService.updateById(scriptParameter);
		return Result.OK("编辑成功!");
	}

	 /**
	  *   通过id删除
	  *
	  * @param id id
	  * @return 成功或者异常
	  */
	 @AutoLog(value = "脚本参数-通过id删除")
	 @ApiOperation(value="脚本参数-通过id删除", notes="脚本参数-通过id删除")
	 @DeleteMapping(value = "/delete")
	 public Result<?> delete(@RequestParam(name="id") String id) {
		 try {
			 if(scriptParameterService.removeById(id)){
				 return Result.OK("脚本参数删除成功!");
			 }
			 return Result.error("删除失败!");
		 } catch (Exception e) {
			 log.error("删除异常"+e.getMessage());
			 return Result.error(e.getMessage());
		 }
	 }




	 /**
	  *  批量删除
	  *
	  * @param ids 字符串ids
	  * @return 成功或者异常
	  */
	 @AutoLog(value = "脚本参数-批量删除")
	 @ApiOperation(value="脚本参数-批量删除", notes="脚本参数-批量删除")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<?> deleteBatch(@RequestParam(name="ids") String ids) {
		 try {
			 List<String> stringList = Arrays.asList(ids.split(","));
			 if(scriptParameterService.removeByIds(stringList)){
				 return Result.OK("脚本参数-批量删除成功!");
			 }
			 return Result.error("脚本参数-批量删除失败!");
		 } catch (Exception e) {
			 log.error("批量删除异常"+e.getMessage());
			 return Result.error(e.getMessage());
		 }
	 }

	
	/**
	 * 通过id查询
	 *
	 * @param id id
	 * @return ScriptParameter
	 */
	@AutoLog(value = "脚本参数-通过id查询")
	@ApiOperation(value="脚本参数-通过id查询", notes="脚本参数-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id") String id) {
		ScriptParameter scriptParameter = scriptParameterService.getById(id);
		if(scriptParameter==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scriptParameter);
	}

	/**
	 * 通过脚本id查询
	 * @param scriptId scriptId
	 * @return ScriptParameter
	 */
	@AutoLog(value = "脚本参数-通过脚本id查询")
	@ApiOperation(value="脚本参数-通过脚本id查询", notes="脚本参数-通过脚本id查询")
	@GetMapping(value = "/queryByScriptId")
	public Result<?> queryByScriptId(@RequestParam(name="scriptId") String scriptId) {
		try {
			if (StringUtils.isEmpty(scriptId)){
				return Result.error("缺少脚本Id");
			}
			QueryWrapper<ScriptParameter> queryWrapper=new QueryWrapper<>();
			queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
			queryWrapper.eq(CommonConstant.STRING_SCRIPT_ID, scriptId);
			List<ScriptParameter> list = scriptParameterService.list(queryWrapper);
			return Result.OK(list);
		} catch (Exception e) {
			log.error("通过脚本Id查询异常"+e.getMessage());
			return Result.error(e.getMessage());
		}
	}


	/**
    * 导出excel
    *
    * @param request request
    * @param scriptParameter  scriptParameter
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ScriptParameter scriptParameter) {
        return super.exportXls(request, scriptParameter, ScriptParameter.class, "脚本参数");
    }

    /**
      * 通过excel导入数据
    *
    * @param request request
    * @param response response
    * @return 成功或者失败
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ScriptParameter.class);
    }


	/**
	 * 脚本参数编辑或者添加
	 * @param scriptParameters 参数
	 * @return 成功或者异常
	 */
	@AutoLog(value = "脚本参数-添加或者修改")
	@ApiOperation(value="脚本参数-添加或者修改", notes="脚本参数-添加或者修改")
	@PostMapping(value = "/addOrEditParameter")
	public Result<?> addOrEditParameter(@RequestBody List<ScriptParameter> scriptParameters) {
		try {
			if (scriptParameters == null||scriptParameters.size()==0){
				return Result.error("脚本参数不能为空!");
			}
			scriptParameterService.addOrEditParameter(scriptParameters);
			return Result.OK("保存成功！");
		} catch (Exception e) {
			log.error("脚本参数保存异常:"+e.getMessage());
			return Result.error("脚本参数保存异常:"+e.getMessage());
		}
	}
}
