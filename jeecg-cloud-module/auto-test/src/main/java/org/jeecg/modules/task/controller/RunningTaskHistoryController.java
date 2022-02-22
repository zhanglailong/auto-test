package org.jeecg.modules.task.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.task.entity.RunningTaskHistory;
import org.jeecg.modules.task.service.IRunningTaskHistoryService;

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
 * @Description: 任务管理历史记录
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Api(tags="任务管理历史记录")
@RestController
@RequestMapping("/running/task/runningTaskHistory")
@Slf4j
public class RunningTaskHistoryController extends JeecgController<RunningTaskHistory, IRunningTaskHistoryService> {
	@Autowired
	private IRunningTaskHistoryService runningTaskHistoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param runningTaskHistory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "任务管理历史记录-分页列表查询")
	@ApiOperation(value="任务管理历史记录-分页列表查询", notes="任务管理历史记录-分页列表查询")
	@GetMapping(value = "/historyList")
	public Result<?> queryPageList(RunningTaskHistory runningTaskHistory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<RunningTaskHistory> page = new Page<RunningTaskHistory>(pageNo, pageSize);
		return Result.ok(runningTaskHistoryService.getOperationHistoryList(page,runningTaskHistory));
	}
	
	/**
	 *   添加
	 *
	 * @param runningTaskHistory
	 * @return
	 */
	@AutoLog(value = "任务管理历史记录-添加")
	@ApiOperation(value="任务管理历史记录-添加", notes="任务管理历史记录-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningTaskHistory runningTaskHistory) {
		runningTaskHistoryService.save(runningTaskHistory);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningTaskHistory
	 * @return
	 */
	@AutoLog(value = "任务管理历史记录-编辑")
	@ApiOperation(value="任务管理历史记录-编辑", notes="任务管理历史记录-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningTaskHistory runningTaskHistory) {
		runningTaskHistoryService.updateById(runningTaskHistory);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务管理历史记录-通过id删除")
	@ApiOperation(value="任务管理历史记录-通过id删除", notes="任务管理历史记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningTaskHistoryService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "任务管理历史记录-批量删除")
	@ApiOperation(value="任务管理历史记录-批量删除", notes="任务管理历史记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningTaskHistoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务管理历史记录-通过id查询")
	@ApiOperation(value="任务管理历史记录-通过id查询", notes="任务管理历史记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningTaskHistory runningTaskHistory = runningTaskHistoryService.getById(id);
		if(runningTaskHistory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningTaskHistory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningTaskHistory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningTaskHistory runningTaskHistory) {
        return super.exportXls(request, runningTaskHistory, RunningTaskHistory.class, "任务管理历史记录");
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
        return super.importExcel(request, response, RunningTaskHistory.class);
    }

}
