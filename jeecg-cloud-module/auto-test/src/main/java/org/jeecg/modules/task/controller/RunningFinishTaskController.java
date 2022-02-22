package org.jeecg.modules.task.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.task.entity.RunningFinishTask;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.service.IRunningFinishTaskService;
import org.jeecg.modules.task.service.IRunningTaskService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 完成任务表
 * @Author: jeecg-boot
 * @Date:   2021-02-22
 * @Version: V1.0
 */
@Api(tags="完成任务表")
@RestController
@RequestMapping("/task/runningFinishTask")
@Slf4j
public class RunningFinishTaskController extends JeecgController<RunningFinishTask, IRunningFinishTaskService> {
	@Autowired
	private IRunningFinishTaskService runningFinishTaskService;
	@Autowired
	private IRunningTaskService runningTaskService;
	
	/**
	 *   添加
	 *
	 * @param runningFinishTask
	 * @return
	 */
	@AutoLog(value = "完成任务表-添加")
	@ApiOperation(value="完成任务表-添加", notes="完成任务表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningFinishTask runningFinishTask) {
		//自动添加信息
		
		
		String taskId = runningFinishTask.getTaskId();
		//查询任务数据是否存在
		RunningTask runningTask = runningTaskService.getById(taskId);
		if(runningTask!=null) {
			//更新任务状态为-完成任务
			runningTask.setTaskStatus("1");
			runningTaskService.updateById(runningTask);
		}else{
			return Result.error("未找到任务");
		}

		return Result.ok("添加成功！");
	}

}
