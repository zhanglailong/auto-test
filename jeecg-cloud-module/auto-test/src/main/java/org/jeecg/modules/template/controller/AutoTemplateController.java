package org.jeecg.modules.template.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.template.entity.AutoTemplate;
import org.jeecg.modules.template.entity.TemplateJson;
import org.jeecg.modules.template.service.IAutoTemplateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 模板列表
 * @Author: zll
 * @Date:   2021-11-02
 * @Version: V1.0
 */
@Api(tags="模板列表")
@RestController
@RequestMapping("/template/autoTemplate")
@Slf4j
public class AutoTemplateController extends JeecgController<AutoTemplate, IAutoTemplateService> {
	@Autowired
	private IAutoTemplateService autoTemplateService;
	
	/**
	 * 分页列表查询
	 *
	 * @param autoTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "模板列表-分页列表查询")
	@ApiOperation(value="模板列表-分页列表查询", notes="模板列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AutoTemplate autoTemplate,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AutoTemplate> queryWrapper = QueryGenerator.initQueryWrapper(autoTemplate, req.getParameterMap());
		queryWrapper.eq(CommonConstant.DATA_STRING_IDEL,CommonConstant.DATA_INT_IDEL_0);
		Page<AutoTemplate> page = new Page<AutoTemplate>(pageNo, pageSize);
		IPage<AutoTemplate> pageList = autoTemplateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param autoTemplate
	 * @return
	 */
	@AutoLog(value = "模板列表-添加")
	@ApiOperation(value="模板列表-添加", notes="模板列表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AutoTemplate autoTemplate) {
		autoTemplateService.save(autoTemplate);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jsonParam
	 * @return
	 */
	@AutoLog(value = "模板列表-编辑")
	@ApiOperation(value="模板列表-编辑", notes="模板列表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JSONObject jsonParam) {

		int result = 0;
		try {
			if (jsonParam != null && StringUtils.isNotEmpty(jsonParam.toString())) {
				TemplateJson templateJson = JSON.parseObject(jsonParam.toString(), TemplateJson.class);
				if (templateJson != null) {
					if (autoTemplateService.updateTemplate(templateJson)) {
						result = 1;
					}
				}
			}
		} catch (Exception e) {
			log.error("模板列表编辑异常：" + e.getMessage());
			return Result.error("模板列表编辑异常：" + e.getMessage());
		}

		if (result == 0) {
			return Result.error("模板编辑值异常，请重新发送");
		} else {
			return Result.OK("编辑成功！");
		}
	}
	 /**
	  *   添加模板
	  *
	  * @param jsonParam
	  * @return
	  */
	 @AutoLog(value = "固定，自定义模板-添加")
	 @ApiOperation(value="固定，自定义模板-添加", notes="固定，自定义模板-添加")
	 @PostMapping(value = "/addTemplate")
	 public Result<?> add(@RequestBody JSONObject jsonParam) {
		 int result = 0;
		 try {
			 if (jsonParam != null && StringUtils.isNotEmpty(jsonParam.toString())) {
				 TemplateJson templateJson = JSON.parseObject(jsonParam.toString(), TemplateJson.class);
				 if (templateJson != null) {
					 if (autoTemplateService.addTemplate(templateJson)) {
						 result = 1;
					 }
				 }
			 }
		 } catch (Exception e) {
			 log.error("模板列表异常：" + e.getMessage());
			 return Result.error("模板列表异常：" + e.getMessage());
		 }

		 if (result == 0) {
			 return Result.error("模板获取值异常，请重新发送");
		 } else {
			 return Result.OK("添加成功！");
		 }
	 }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "模板列表-通过id删除")
	@ApiOperation(value="模板列表-通过id删除", notes="模板列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			if (StringUtils.isEmpty(id)) {
				return Result.error("缺少参数");
			}
			autoTemplateService.removeById(id);
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
	@AutoLog(value = "模板列表-批量删除")
	@ApiOperation(value="模板列表-批量删除", notes="模板列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.autoTemplateService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "模板列表-通过id查询")
	@ApiOperation(value="模板列表-通过id查询", notes="模板列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AutoTemplate autoTemplate = autoTemplateService.getById(id);
		if(autoTemplate==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(autoTemplate);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param autoTemplate
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AutoTemplate autoTemplate) {
        return super.exportXls(request, autoTemplate, AutoTemplate.class, "模板列表");
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
        return super.importExcel(request, response, AutoTemplate.class);
    }

}
