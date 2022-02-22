package org.jeecg.modules.autoNodes.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.autoNodes.entity.AutoNodes;
import org.jeecg.modules.autoNodes.service.IAutoNodesService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.common.CommonConstant;
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
 * @Description: 节点管理
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Api(tags="节点管理")
@RestController
@RequestMapping("/autoNodes")
@Slf4j
public class AutoNodesController extends JeecgController<AutoNodes, IAutoNodesService> {
	@Autowired
	private IAutoNodesService autoNodesService;
	
	/**
	 * 分页列表查询
	 *
	 * @param autoNodes
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "节点管理-分页列表查询")
	@ApiOperation(value="节点管理-分页列表查询", notes="节点管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AutoNodes autoNodes,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AutoNodes> queryWrapper = QueryGenerator.initQueryWrapper(autoNodes, req.getParameterMap());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
		queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
		Page<AutoNodes> page = new Page<AutoNodes>(pageNo, pageSize);
		IPage<AutoNodes> pageList = autoNodesService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param autoNodes
	 * @return
	 */
	@AutoLog(value = "节点管理-添加")
	@ApiOperation(value="节点管理-添加", notes="节点管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AutoNodes autoNodes) {

		try {
			if (autoNodes == null) {
				return Result.error("对象不能为空");
			}
			autoNodes.setIdel(CommonConstant.DATA_INT_IDEL_0);
			autoNodes.setState(CommonConstant.DATA_INT_STATE_0);
			autoNodesService.save(autoNodes);
			return Result.OK("添加成功！");
		} catch (Exception e) {
			log.error("添加失败" + e.getMessage());
			return Result.error(e.getMessage());
		}
	}
	
	/**
	 *  编辑
	 *
	 * @param autoNodes
	 * @return
	 */
	@AutoLog(value = "节点管理-编辑")
	@ApiOperation(value="节点管理-编辑", notes="节点管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AutoNodes autoNodes) {
		if(autoNodesService.updateNode(autoNodes)){
			return Result.OK("编辑成功");
		}
		return Result.error("编辑失败!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "节点管理-通过id删除")
	@ApiOperation(value="节点管理-通过id删除", notes="节点管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {

		try {
			if (StringUtils.isEmpty(id)) {
				return Result.error("缺少参数");
			}
			//autoNodesService.delete(id);
			autoNodesService.removeById(id);
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
	@AutoLog(value = "节点管理-批量删除")
	@ApiOperation(value="节点管理-批量删除", notes="节点管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.autoNodesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "节点管理-通过id查询")
	@ApiOperation(value="节点管理-通过id查询", notes="节点管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AutoNodes autoNodes = autoNodesService.getById(id);
		if(autoNodes==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(autoNodes);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param autoNodes
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AutoNodes autoNodes) {
        return super.exportXls(request, autoNodes, AutoNodes.class, "节点管理");
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
        return super.importExcel(request, response, AutoNodes.class);
    }

}
