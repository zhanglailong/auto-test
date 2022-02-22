package org.jeecg.modules.scriptandplan.controller;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.scriptandplan.entity.AutoScriptPlan;
import org.jeecg.modules.scriptandplan.service.IAutoScriptPlanService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 方案脚本对应表
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
@Api(tags="方案脚本对应表")
@RestController
@RequestMapping("/scriptandplan/autoScriptPlan")
@Slf4j
public class AutoScriptPlanController extends JeecgController<AutoScriptPlan, IAutoScriptPlanService> {
	@Autowired
	private IAutoScriptPlanService autoScriptPlanService;
	
	/**
	 * 分页列表查询
	 *
	 * @param autoScriptPlan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "方案脚本对应表及方案绑定的脚本-分页列表查询")
	@ApiOperation(value="方案脚本对应表及方案绑定的脚本-分页列表查询", notes="方案脚本对应表及方案绑定的脚本-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AutoScriptPlan autoScriptPlan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AutoScriptPlan> queryWrapper = QueryGenerator.initQueryWrapper(autoScriptPlan, req.getParameterMap());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		queryWrapper.getExpression().getOrderBy().remove(0);
		queryWrapper.orderByAsc(CommonConstant.DATA_STRING_SORT);
		queryWrapper.orderByAsc(CommonConstant.DATA_STRING_WEIGHT);
		queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
		queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
		if (StringUtils.isNotBlank(autoScriptPlan.getTestCaseId())) {
			queryWrapper.eq(CommonConstant.DATA_STRING_TEST_CASE_ID, autoScriptPlan.getTestCaseId());
		}
		Page<AutoScriptPlan> page = new Page<AutoScriptPlan>(pageNo, pageSize);
		IPage<AutoScriptPlan> pageList = autoScriptPlanService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param autoScriptPlan
	 * @return
	 */
	@AutoLog(value = "方案脚本对应表-添加")
	@ApiOperation(value="方案脚本对应表-添加", notes="方案脚本对应表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AutoScriptPlan autoScriptPlan) {
		autoScriptPlanService.save(autoScriptPlan);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param autoScriptPlan
	 * @return
	 */
	@AutoLog(value = "方案脚本对应表-编辑")
	@ApiOperation(value="方案脚本对应表-编辑", notes="方案脚本对应表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AutoScriptPlan autoScriptPlan) {
		autoScriptPlanService.updateById(autoScriptPlan);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "方案脚本对应表-通过id删除")
	@ApiOperation(value="方案脚本对应表-通过id删除", notes="方案脚本对应表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			if(StringUtils.isEmpty(id)){
				return  Result.error("参数不能为空");
			}
			autoScriptPlanService.delete(id);
			return Result.OK("删除成功!");
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "方案脚本对应表-批量删除")
	@ApiOperation(value="方案脚本对应表-批量删除", notes="方案脚本对应表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.autoScriptPlanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "方案脚本对应表-通过id查询")
	@ApiOperation(value="方案脚本对应表-通过id查询", notes="方案脚本对应表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AutoScriptPlan autoScriptPlan = autoScriptPlanService.getById(id);
		if(autoScriptPlan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(autoScriptPlan);
	}


	 /**
	  * 绑定脚本
	  *
	  * @param testCaseId    测试用例id
	  * @param scriptIds 绑定的脚本ids
	  * @return
	  */
	 @AutoLog(value = "方案中绑定脚本")
	 @ApiOperation(value = "方案中绑定脚本", notes = "方案中绑定脚本")
	 @GetMapping(value = "/bindingScript")
	 public Result<?> bindingScript(
			 @RequestParam(name = "testCaseId", required = true) String testCaseId,
			 @RequestParam(name = "scriptIds", required = true) String scriptIds) {

		 if (autoScriptPlanService.bindingScript(testCaseId, scriptIds)) {
			 return Result.OK("方案绑定脚本成功");
		 } else {
			 return Result.error("方案绑定脚本失败");
		 }
	 }


	 /**
	  * 脚本的上移
	  */
	 @AutoLog(value = "脚本的上移")
	 @ApiOperation(value = "脚本的上移", notes = "脚本的上移")
	 @PostMapping(value = "/scriptUp")
	 public Result<?> scriptUp(@RequestBody AutoScriptPlan autoScriptPlan) {
		 try {
			 autoScriptPlanService.scriptUp(autoScriptPlan);
			 return Result.OK("脚本上移成功");
		 } catch (Exception e) {
			 return Result.error("脚本上移失败，原因是  "+e.getMessage());
		 }
	 }

	 /**
	  * 脚本的下移
	  */
	 @AutoLog(value = "脚本的下移")
	 @ApiOperation(value = "脚本的下移", notes = "脚本的下移")
	 @PostMapping(value = "/scriptDown")
	 public Result<?> scriptDown(@RequestBody AutoScriptPlan autoScriptPlan) {
		 try {
			 autoScriptPlanService.scriptdown(autoScriptPlan);
			 return Result.OK("脚本下移成功");
		 } catch (Exception e) {
			 return Result.error("脚本上下移失败，原因是  "+e.getMessage());
		 }
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param autoScriptPlan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AutoScriptPlan autoScriptPlan) {
        return super.exportXls(request, autoScriptPlan, AutoScriptPlan.class, "方案脚本对应表");
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
        return super.importExcel(request, response, AutoScriptPlan.class);
    }

}
