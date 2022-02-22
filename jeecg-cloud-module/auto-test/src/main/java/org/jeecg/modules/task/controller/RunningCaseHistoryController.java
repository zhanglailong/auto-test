package org.jeecg.modules.task.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.task.entity.RunningCaseHistory;
import org.jeecg.modules.task.service.IRunningCaseHistoryService;

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
 * @Description: running_case_history
 * @Author: jeecg-boot
 * @Date:   2021-03-24
 * @Version: V1.0
 */
@Api(tags="running_case_history")
@RestController
@RequestMapping("/task/runningCaseHistory")
@Slf4j
public class RunningCaseHistoryController extends JeecgController<RunningCaseHistory, IRunningCaseHistoryService> {
	@Autowired
	private IRunningCaseHistoryService runningCaseHistoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param runningCaseHistory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
//	@AutoLog(value = "running_case_history-分页列表查询")
//	@ApiOperation(value="running_case_history-分页列表查询", notes="running_case_history-分页列表查询")
//	@GetMapping(value = "/list")
//	public Result<?> queryPageList(RunningCaseHistory runningCaseHistory,
//								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
//								   HttpServletRequest req) {
//		QueryWrapper<RunningCaseHistory> queryWrapper = QueryGenerator.initQueryWrapper(runningCaseHistory, req.getParameterMap());
//		Page<RunningCaseHistory> page = new Page<RunningCaseHistory>(pageNo, pageSize);
//		IPage<RunningCaseHistory> pageList = runningCaseHistoryService.page(page, queryWrapper);
//		return Result.ok(pageList);
//	}
//
	/**
	 *   添加
	 *
	 * @param runningCaseHistory
	 * @return
	 */
	@AutoLog(value = "running_case_history-添加")
	@ApiOperation(value="running_case_history-添加", notes="running_case_history-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningCaseHistory runningCaseHistory) {
		runningCaseHistoryService.save(runningCaseHistory);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningCaseHistory
	 * @return
	 */
	@AutoLog(value = "running_case_history-编辑")
	@ApiOperation(value="running_case_history-编辑", notes="running_case_history-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningCaseHistory runningCaseHistory) {
		runningCaseHistoryService.updateById(runningCaseHistory);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "running_case_history-通过id删除")
	@ApiOperation(value="running_case_history-通过id删除", notes="running_case_history-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningCaseHistoryService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "running_case_history-批量删除")
	@ApiOperation(value="running_case_history-批量删除", notes="running_case_history-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningCaseHistoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "running_case_history-通过id查询")
	@ApiOperation(value="running_case_history-通过id查询", notes="running_case_history-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningCaseHistory runningCaseHistory = runningCaseHistoryService.getById(id);
		if(runningCaseHistory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningCaseHistory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningCaseHistory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningCaseHistory runningCaseHistory) {
        return super.exportXls(request, runningCaseHistory, RunningCaseHistory.class, "running_case_history");
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
        return super.importExcel(request, response, RunningCaseHistory.class);
    }

	 /*@AutoLog(value = "running_case_history-分页列表查询")
	 @ApiOperation(value="running_case_history-分页列表查询", notes="running_case_history-分页列表查询")
	 @GetMapping(value = "/history")
	 public Result<?> queryHistoryList(RunningCaseHistory runningCaseHistory,
									   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									   HttpServletRequest req) {
		 Page<RunningCaseHistory> page = new Page<RunningCaseHistory>(pageNo, pageSize);
		 IPage<Map<String,Object>> pageList = runningCaseHistoryService.getOperationHistoryList(page, runningCaseHistory);
		 return Result.ok(pageList);
	 }*/
	 @AutoLog(value = "running_case_history-分页列表查询")
	 @ApiOperation(value="running_case_history-分页列表查询", notes="running_case_history-分页列表查询")
	 @GetMapping(value = "/historyList")
	 public Result<?> queryHistoryList(RunningCaseHistory runningCaseHistory,
	                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
	                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
	                                   HttpServletRequest req) {
		 Page<RunningCaseHistory> page = new Page<RunningCaseHistory>(pageNo, pageSize);
		 IPage<RunningCaseHistory> pageList = runningCaseHistoryService.queryHistoryListData(page,runningCaseHistory);
		 return Result.ok(pageList);
	 }
}
