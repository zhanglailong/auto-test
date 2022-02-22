package org.jeecg.modules.task.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.entity.RunningTaskHistory;
import org.jeecg.modules.task.entity.RunningTaskReport;
import org.jeecg.modules.task.service.IRunningTaskHistoryService;
import org.jeecg.modules.task.service.IRunningTaskReportService;
import org.jeecg.modules.task.service.IRunningTaskService;
import org.jeecg.modules.task.vo.RunningTaskVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 大连任务管理
 * @Author: jeecg-boot
 * @Date: 2020-12-25
 * @Version: V1.0
 */
@Api(tags = "大连任务管理")
@RestController
@RequestMapping("/task/runningTask")
@Slf4j
public class RunningTaskController extends JeecgController<RunningTask, IRunningTaskService> {
	@Autowired
	private IRunningTaskService runningTaskService;

	@Autowired
	private IRunningTaskHistoryService runningTaskHistoryService;

	@Value(value = "${jeecg.path.upload}")
	private String uploadpath;


	/**
	 * 本地：local minio：minio 阿里：alioss
	 */
	@Value(value = "${jeecg.uploadType}")
	private String uploadType;

	/**
	 * 系统桶名
	 */
	@Value(value = "${jeecg.minio.bucket-system}")
	private String customBucket;

	/**
	 * 测试报告保存路径
	 */
	@Value(value = "${jeecg.minio.test_report_path}")
	private String testReportPath;

	@Autowired
	private IRunningTaskReportService runningTaskReportService;

	/**
	 * 分页列表查询
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "任务管理-分页列表查询")
	@ApiOperation(value = "任务管理-分页列表查询", notes = "任务管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name = "projectId", required = false) String projectId,
			@RequestParam(name = "taskName", required = false) String taskName,
			@RequestParam(name = "priority", required = false) String priority,
			@RequestParam(name = "taskCode", required = false) String taskCode,
			@RequestParam(name = "taskSoftName", required = false) String taskSoftName,
			@RequestParam(name = "projectIdCookie", required = false) String projectIdCookie,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize, HttpServletRequest req) {
		Page<RunningTaskVO> pageList = new Page<RunningTaskVO>(pageNo, pageSize);
		if(!StringUtils.isEmpty(projectIdCookie)){
			pageList = runningTaskService.queryPageList(pageList, projectIdCookie, taskName, priority, taskCode, taskSoftName);
		}
		log.info("查询当前页：" + pageList.getCurrent());
		log.info("查询当前页数量：" + pageList.getSize());
		log.info("查询结果数量：" + pageList.getRecords().size());
		log.info("数据总数：" + pageList.getTotal());
		return Result.ok(pageList);
	}

	/**
	 * 归档管理-测试项归档列表查询
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "归档管理-测试项归档列表查询")
	@ApiOperation(value = "归档管理-测试项归档列表查询", notes = "归档管理-测试项归档列表查询")
	@GetMapping(value = "/fileTaskList")
	public Result<?> queryFileTaskPageList(@RequestParam(name = "projectId", required = false) String projectId,
								   @RequestParam(name = "taskName", required = false) String taskName,
								   @RequestParam(name = "priority", required = false) String priority,
								   @RequestParam(name = "taskCode", required = false) String taskCode,
								   @RequestParam(name = "taskSoftName", required = false) String taskSoftName,
								   @RequestParam(name = "projectIdCookie", required = false) String projectIdCookie,
								   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								   @RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize, HttpServletRequest req) {
		Page<RunningTaskVO> pageList = new Page<RunningTaskVO>(pageNo, pageSize);
		pageList = runningTaskService.queryFileTaskPageList(pageList, projectId, taskName, priority, taskCode, taskSoftName);
		log.info("查询当前页：" + pageList.getCurrent());
		log.info("查询当前页数量：" + pageList.getSize());
		log.info("查询结果数量：" + pageList.getRecords().size());
		log.info("数据总数：" + pageList.getTotal());
		return Result.ok(pageList);
	}

	/*@AutoLog(value = "任务管理-开始任务")
	@ApiOperation(value = "任务管理-开始任务")
	@GetMapping(value = "/startTask")
	public synchronized Result<?> startTask(StcpTestQueue task) {

		// 通过taskType获取绑定的客户端
		QueryWrapper<RunningToolsList> wrapper = new QueryWrapper<>();
		wrapper.eq("tools_code", task.getTaskType());
		String bindIp = runningToolsListService.getOne(wrapper).getToolsLocation();
		task.setClientIp(bindIp);
		task.setTaskUrl(uploadpath + File.separator + task.getTaskUrl());

		// 判断客户端是否在线
		if (!SocketHandler.getSocketMap().containsKey(bindIp)) {
			return Result.error("客户端不在线！");
		}

		QueryWrapper<StcpTestQueue> stcpwrapper = new QueryWrapper<>();
		stcpwrapper.eq("task_type", task.getTaskType());
		stcpwrapper.eq("client_ip", task.getClientIp());
		stcpwrapper.eq("status", 1);
		// 若该客户端不存在任务，通知客户端进行扫描
		if (queService.count(stcpwrapper) == 0) {
			task.setStatus(1);
			queService.save(task);
			SocketHandler.write(task.getClientIp(), JSONObject.toJSONString(task));
		} else {
			task.setStatus(0);
			queService.save(task);
		}

		return Result.ok();
	}*/

	/**
	 * 添加
	 *
	 * @param runningTask
	 * @return
	 */
	@AutoLog(value = "任务管理-添加")
	@ApiOperation(value = "任务管理-添加", notes = "任务管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningTask runningTask) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		runningTask.setCreateBy(sysUser.getUsername());
		runningTask.setCreateTime(new Date());
		runningTask.setDelFlag(0);
		runningTask.setFileStatus(0);
		runningTaskService.save(runningTask);

		// 添加历史记录
		insertHistory(runningTask,"0",runningTask.getId());
		//addHistory(sysUser, runningTask, "新增");

		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param runningTask
	 * @return
	 */
	@AutoLog(value = "任务管理-编辑")
	@ApiOperation(value = "任务管理-编辑", notes = "任务管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningTask runningTask) {
		// 获取当前用户
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		runningTaskService.updateById(runningTask);
//		runningTaskService.UPDATE
		// 添加历史记录
		if(runningTask.getIsModified()){
			insertHistory(runningTask,"1",runningTask.getId());
		}
		//addHistory(sysUser, runningTask, "修改");

		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务管理-通过id删除")
	@ApiOperation(value = "任务管理-通过id删除", notes = "任务管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		// 删除前获取对象
		RunningTask runningTask = runningTaskService.getById(id);
		// 逻辑删除任务信息，任务下的所有测试用例信息、问题信息
		runningTaskService.updateTask(id);
		// 获取当前用户
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		// 添加历史记录
		insertHistory(runningTask,"2",runningTask.getId());
		//addHistory(sysUser, runningTask, "删除");

		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "任务管理-批量删除")
	@ApiOperation(value = "任务管理-批量删除", notes = "任务管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		List<RunningTask> runningTasks=runningTaskService.listByIds(Arrays.asList(ids.split(",")));
		for (RunningTask record: runningTasks){
			record.setDelFlag(1);
			record.setUpdateTime(new Date());
			insertHistory(record,"2",record.getId());
		}
		runningTaskService.updateBatchById(runningTasks);
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务管理-通过id查询")
	@ApiOperation(value = "任务管理-通过id查询", notes = "任务管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		RunningTask runningTask = runningTaskService.getById(id);
		if (runningTask == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningTask);
	}

	/**
	 * 通过任务id查询采集结果页访问URL
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务管理-通过id查询")
	@ApiOperation(value = "任务管理-通过id查询", notes = "任务管理-通过id查询")
	@GetMapping(value = "/queryCJUrlBytaskId")
	public Result<?> queryCJUrlBytaskId(String id) {
		Map urlMap = new HashMap<>(2000);
		String sjwjurl = runningTaskService.getCjUrlByTaskId(id);
		if (sjwjurl == null) {
			return Result.error("未找到对应数据");
		}
		urlMap.put("sjwjurl", sjwjurl);
		return Result.ok(urlMap);
	}

	/**
	 * 通过项目id查询项目组成员名称
	 *
	 * @param projectId
	 * @return name
	 */
	@AutoLog(value = "通过项目id查询项目组成员名称")
	@ApiOperation(value = "通过项目id查询项目组成员名称", notes = "通过项目id查询项目组成员名称")
	@GetMapping(value = "/queryPersonByProjectId")
	public Result<?> queryPersonByProjectId(@RequestParam(name = "projectId", required = false) String projectId,HttpServletRequest req) {
		// 定义最终存放数据集合
		List<Map<String, Object>> resultList = new ArrayList<>();
		//根据项目ID查询项uut_list_id
		List<String> uutListIds = runningTaskService.getUutListId(projectId);
		List<String> userIds = new ArrayList<>();
		if(uutListIds != null){
			for(String uutListId : uutListIds){
				//多数据源,根据uut_list_id查询user_id
				List<String> userId = runningTaskService.getUserId(uutListId);
				if(userId != null){
					for(String s : userId){
						userIds.add(s);
					}
				}
			}
		}

		if(userIds.isEmpty()){
			return Result.error("未找到对应数据");
		}

		if(userIds != null){
			for(String userId : userIds){
				String realName = runningTaskService.getUserRealName(userId);
				if(!realName.isEmpty()){
					Map<String, Object> map = new HashMap<>(2000);
					map.put("label", realName);
					map.put("value", userId);
					resultList.add(map);
				}
			}
		}

//		List<String> userIdList = projectUserAssociationService.getUserIdsByProjectId(projectId);
//		if (userIdList.isEmpty()) {
//			return Result.error("未找到对应数据");
//		}
//
//		if (userIdList != null) {
//			for (String userId : userIdList) {
//				// 根据项目成员ID查询项目成员名称
//				String realName = sysUserService.getRealNameById(userId);
//				if (!realName.isEmpty()) {
//					Map<String, Object> map = new HashMap<>(2000);
//					map.put("label", realName);
//					map.put("value", userId);
//					resultList.add(map);
//				}
//			}
//		}

		return Result.ok(resultList);
	}
	
	@GetMapping("/getOne")
	public Result<?> getOne(String id){
		return Result.ok(runningTaskService.getById(id));
	}

	/**
	 * 根据项目id查询任务信息
	 *
	 * @param projectId
	 * @return 任务信息
	 */
	@AutoLog(value = "根据项目id查询任务信息")
	@ApiOperation(value = "根据项目id查询任务信息", notes = "根据项目id查询任务信息")
	@GetMapping(value = "/queryTaskInfoByProjectId")
	public Result<?> queryTaskInfoByProjectId(@RequestParam(name = "projectId",required = false) String projectId) {
		/*String publicKey = jsonObject.getString("publicKey");
		String token = jsonObject.getString("token");
		String projectId = jsonObject.getString("projectId");
		Optional<SysSecretKey> sysSecretKey = sysSecretKeyService.lambdaQuery()
				.eq(SysSecretKey::getPublicKey, publicKey).eq(SysSecretKey::getToken, token).oneOpt();
		if (!sysSecretKey.isPresent()) {
			return Result.error("密钥或token不正确！");
		}*/

		List<RunningTask> list = runningTaskService.getTaskInfoByProjectId(projectId);
		/*List<Map<String, Object>> resList = new ArrayList<>();
		if (list == null || list.isEmpty()) {
			String taskInfo = JSON.toJSONString(resList);
			String secret = SecurityUtil.jiami(taskInfo);
			return Result.ok(secret);
		}
		for (int i = 0; i < list.size(); i++) {
			String taskId = list.get(i).getId();
			String taskName = list.get(i).getTaskName();
			String taskCode = list.get(i).getTaskCode();
			Map<String, Object> map = new HashMap<>(2000);
			map.put("taskId", taskId);
			map.put("taskName", taskName);
			map.put("taskCode", taskCode);
			resList.add(map);
		}
		// List转json
		String taskInfo = JSON.toJSONString(resList);
		String secret = SecurityUtil.jiami(taskInfo);*/
		return Result.OK(list);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param runningTask
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, RunningTask runningTask) {
		return super.exportXls(request, runningTask, RunningTask.class, "任务管理");
	}

//    /**
//      * 通过excel导入数据
//    *
//    * @param request
//    * @param response
//    * @return
//    */
//    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
//    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//        return super.importExcel(request, response, RunningTask.class);
//    }
	
	/**
	 * 添加历史操作记录

	 */
	public void insertHistory(RunningTask originData, String operationType, String mainId)
	{
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		RunningTaskHistory runningTaskHistory = new RunningTaskHistory();
		String add="0";
		String delete="2";
		String edit="1";
		if(add.equals(operationType))
		{
			// 新增时把源数据在历史表中备份一个
			BeanUtils.copyProperties(originData,runningTaskHistory);
		}
		if(delete.equals(operationType))
		{
			// 删除
			BeanUtils.copyProperties(originData,runningTaskHistory);
		}
		if(add.equals(operationType) || delete.equals(operationType))
		{
			runningTaskHistory.setId(null);
			runningTaskHistory.setReviser(sysUser.getUsername());
			runningTaskHistory.setUpdateBy(sysUser.getUsername());
			runningTaskHistory.setOperationType(operationType);
			runningTaskHistory.setUpdateTime(new Date());
			runningTaskHistory.setTaskId(mainId);
			runningTaskHistoryService.save(runningTaskHistory);
		}
		if(edit.equals(operationType))
		{
				//Integer insertSort = getMaxSortByTaskId(mainId); // 当前插入的sort
				RunningTaskHistory newEdit=new RunningTaskHistory();
				BeanUtils.copyProperties(originData,newEdit);
				newEdit.setId(null);
				newEdit.setTaskId(mainId);
				newEdit.setReviser(sysUser.getUsername());
				newEdit.setUpdateBy(sysUser.getUsername());
				newEdit.setUpdateTime(new Date());
				newEdit.setOperationType(operationType);
				//数据库存入当前编辑的记录值
				runningTaskHistoryService.save(newEdit);
		}
	}
	/**
	 * 上传测试报告
	 *
	 * @param files
	 * @param taskId
	 * @param caseId
	 */
	@PostMapping(value = "/uploadTestReport")
	public Result<?> uploadTestReport(@RequestParam List<MultipartFile> files, @RequestParam String taskId,
			@RequestParam String caseId) {
		if (files == null || StringUtils.isEmpty(taskId) || StringUtils.isEmpty(caseId)) {
			return Result.error("请求参数不能为空");
		}
		// 文件上传
		List<String> savePathList = new ArrayList<>();
		for (MultipartFile file : files) {
			String savePath = CommonUtils.upload(file, testReportPath, uploadType, customBucket);
			if (StringUtils.isEmpty(savePath)) {
				return Result.error("测试报告上传失败!");
			}
			savePathList.add(savePath);
			log.info("测试报告上传路径是：" + savePath);
		}
		// 重新上传，则删除原记录，重新插入新记录(如果是多个测试报告，则需要全部重新上传)
		runningTaskReportService.lambdaUpdate().set(RunningTaskReport::getDelFlag, CommonConstant.DEL_FLAG_1)
				.eq(RunningTaskReport::getTaskId, taskId).eq(RunningTaskReport::getCaseId, caseId).update();
		// 保存文件上传信息
		for (String savePath : savePathList) {
			RunningTaskReport runningTaskReport = new RunningTaskReport();
			runningTaskReport.setTaskId(taskId);
			runningTaskReport.setCaseId(caseId);
			runningTaskReport.setReportUrl(savePath);
			runningTaskReport.setDelFlag(CommonConstant.DEL_FLAG_0);
			runningTaskReportService.save(runningTaskReport);
		}
		return Result.ok();
	}


	/**
	 * 根据turnId查询turnNum
	 * @param turnId
	 * @return turnNum
	 */
	@AutoLog(value = "根据turnId查询turnNum")
	@ApiOperation(value = "根据turnId查询turnNum", notes = "根据turnId查询turnNum")
	@GetMapping(value = "/queryTurnNum")
	public String queryTurnNum(@RequestParam(name = "turnId", required = true) String turnId,HttpServletRequest req) {
		String turnNum = runningTaskService.getTurnNum(turnId);
		return turnNum;
	}

}
