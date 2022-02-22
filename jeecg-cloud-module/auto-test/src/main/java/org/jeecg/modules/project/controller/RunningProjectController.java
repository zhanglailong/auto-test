package org.jeecg.modules.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.project.entity.*;
import org.jeecg.modules.project.service.*;
import org.jeecg.modules.project.vo.RunningProjectInfo;
import org.jeecg.modules.system.entity.SysSecretKey;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysSecretKeyService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.util.SecurityUtil;
import org.jeecg.modules.uut.entity.RunningUutList;
import org.jeecg.modules.uut.entity.RunningUutListUser;
import org.jeecg.modules.uut.service.IRunningUutListService;
import org.jeecg.modules.uut.service.IRunningUutListUserService;
import org.jeecg.modules.uut.service.IRunningUutVersionService;
import org.jeecg.modules.uut.vo.RunningUutListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date: 2020-12-23
 * @Version: V1.0
 */
@Api(tags = "项目管理")
@RestController
@RequestMapping("/project/runningProject")
@Slf4j
public class RunningProjectController extends JeecgController<RunningProject, IRunningProjectService> {
	@Autowired
	private IRunningProjectService runningProjectService;
	@Autowired
	private IRunningProjectTurnService runningProjectTurnService;
	@Autowired
	private IRunningProjectTurnVersionService runningProjectTurnVersionService;
	@Autowired
	private IRunningProjectHistoryService historyService;
	@Autowired
	private ISysSecretKeyService sysSecretKeyService;
	@Autowired
	private IRunningUutListService runningUutListService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private IRunningUutListUserService runningUutListUserService;
	@Autowired
	private IRunningUutVersionService runningUutVersionService;

	/**
	 * 分页列表查询
	 *
	 * @param
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目管理-分页列表查询")
	@ApiOperation(value = "项目管理-分页列表查询", notes = "项目管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name = "projectName", required = false) String projectName,
	                               @RequestParam(name = "projectCode", required = false) String projectCode,
	                               @RequestParam(name = "createTime", required = false) String createTime,
	                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
	                               @RequestParam(name = "projectIdCookie", required = false) String projectIdCookie,
	                               @RequestParam(name = "userIdCookie", required = false) String userIdCookie,
	                               HttpServletRequest req) {
		Page<RunningProjectInfo> pageList = new Page<RunningProjectInfo>(pageNo, pageSize);

		pageList = runningProjectService.queryPageList(pageList, projectName, projectCode, createTime, projectIdCookie);
		List<RunningProjectInfo> runningProjectInfoList = new ArrayList<>();
		for (RunningProjectInfo runningProjectInfo: pageList.getRecords()) {
			RunningUutList runningUutList = runningUutListService.getById(runningProjectInfo.getUutListId());
			// 获取jeecg-boot-uut数据库running_uut_version表被测对象对应最高版本
			String version = runningUutListService.queryUutVersionById(runningProjectInfo.getUutListId());
			if(null != runningUutList){
				runningProjectInfo.setUutListId(runningUutList.getId());
				runningProjectInfo.setUutName(runningUutList.getUutName());
				runningProjectInfo.setUutCode(runningUutList.getUutCode());
				runningProjectInfo.setUutType(runningUutList.getUutType());
//				runningProjectInfo.setUutVersion(runningUutList.getUutVersion());
				runningProjectInfo.setUutVersion(version);
				runningProjectInfo.setUutFile(runningUutList.getUutFile());
				runningProjectInfo.setUutOtherFile(runningUutList.getUutOtherFile());
			}
			runningProjectInfoList.add(runningProjectInfo);
		}
		pageList.setRecords(runningProjectInfoList);
		log.info("查询当前页：" + pageList.getCurrent());
		log.info("查询当前页数量：" + pageList.getSize());
		log.info("查询结果数量：" + pageList.getRecords().size());
		log.info("数据总数：" + pageList.getTotal());
		return Result.ok(pageList);
	}

	/**
	 * 归档管理-测试项目归档列表查询
	 *
	 * @param
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "归档管理-测试项目归档列表查询")
	@ApiOperation(value = "归档管理-测试项目归档列表查询", notes = "归档管理-测试项目归档列表查询")
	@GetMapping(value = "/fileProjectlist")
	public Result<?> queryFileProjectPageList(@RequestParam(name = "projectName", required = false) String projectName,
								   @RequestParam(name = "projectCode", required = false) String projectCode,
								   @RequestParam(name = "createTime", required = false) String createTime,
								   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
								   @RequestParam(name = "projectIdCookie", required = false) String projectIdCookie,
								   @RequestParam(name = "userIdCookie", required = false) String userIdCookie,
								   HttpServletRequest req) {
		Page<RunningProjectInfo> pageList = new Page<RunningProjectInfo>(pageNo, pageSize);
		//当前用户角色
		List<String> roles = sysUserService.getRole(userIdCookie);
		if (roles.contains("admin")){
			pageList = runningProjectService.queryFileProjectPageList(pageList, projectName, projectCode, createTime, projectIdCookie);
		}else{
			if(!StringUtils.isEmpty(projectIdCookie)){
				pageList = runningProjectService.queryFileProjectPageList(pageList, projectName, projectCode, createTime, projectIdCookie);
			}
		}
		List<RunningProjectInfo> runningProjectInfoList = new ArrayList<>();
		for (RunningProjectInfo runningProjectInfo: pageList.getRecords()) {
			RunningUutList runningUutList = runningUutListService.getById(runningProjectInfo.getUutListId());
			// 获取jeecg-boot-uut数据库running_uut_version表被测对象对应最高版本
			String version = runningUutListService.queryUutVersionById(runningProjectInfo.getUutListId());
			if(null != runningUutList){
				runningProjectInfo.setUutListId(runningUutList.getId());
				runningProjectInfo.setUutName(runningUutList.getUutName());
				runningProjectInfo.setUutCode(runningUutList.getUutCode());
				runningProjectInfo.setUutType(runningUutList.getUutType());
//				runningProjectInfo.setUutVersion(runningUutList.getUutVersion());
				runningProjectInfo.setUutVersion(version);
				runningProjectInfo.setUutFile(runningUutList.getUutFile());
				runningProjectInfo.setUutOtherFile(runningUutList.getUutOtherFile());
			}
			runningProjectInfoList.add(runningProjectInfo);
		}
		pageList.setRecords(runningProjectInfoList);
		log.info("查询当前页：" + pageList.getCurrent());
		log.info("查询当前页数量：" + pageList.getSize());
		log.info("查询结果数量：" + pageList.getRecords().size());
		log.info("数据总数：" + pageList.getTotal());
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param runningProject
	 * @return
	 */
	@AutoLog(value = "项目管理-添加")
	@ApiOperation(value = "项目管理-添加", notes = "项目管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningProject runningProject) {

		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		runningProject.setCreateBy(sysUser.getUsername());
		runningProject.setUpdateBy(sysUser.getUsername());
		runningProject.setCreateTime(new Date());
		runningProject.setFileStatus(0);
		runningProjectService.save(runningProject);

		// dq add 往操作历史表中插入记录
		insertHistory(runningProject, 0, runningProject.getId());
		/*轮次存值*/
		for (Map<String,String> map : runningProject.getTurnList()) {
			RunningProjectTurn runningProjectTurn = new RunningProjectTurn();
			runningProjectTurn.setProjectId(runningProject.getId());
			runningProjectTurn.setTurnNum(map.get("turnNum"));
			runningProjectTurnService.save(runningProjectTurn);
			for (String string : map.get("versionStr").split(",")) {
				RunningProjectTurnVersion version = new RunningProjectTurnVersion();
				version.setVersionId(string);
				version.setTurnId(runningProjectTurn.getId());
				runningProjectTurnVersionService.save(version);
			}
		}
		/*//存入分析结果表
		EvalAnalysisResult evalAnalysisResult = new EvalAnalysisResult(runningProject.getUutListId(), runningProject.getId(), 0);
		evalAnalysisResultService.save(evalAnalysisResult);*/
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param runningProject
	 * @return
	 */
	@AutoLog(value = "项目管理-编辑")
	@ApiOperation(value = "项目管理-编辑", notes = "项目管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningProject runningProject) {
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		runningProject.setUpdateBy(sysUser.getUsername());
		runningProject.setUpdateTime(new Date());
		runningProjectService.updateById(runningProject);
		// 修改轮次信息
		List<Map<String, String>> turnList = runningProject.getTurnList();
		if(turnList.size() > 0 && turnList != null){
			for(Map<String, String> map : turnList){
				QueryWrapper<RunningProjectTurn> turnQueryWrapper = new QueryWrapper<>();
				turnQueryWrapper.eq("turn_num", map.get("turnNum")).eq("project_id", runningProject.getId());
				RunningProjectTurn runningProjectTurnServiceOne = runningProjectTurnService.getOne(turnQueryWrapper);
				if(runningProjectTurnServiceOne != null){
					RunningProjectTurn runningProjectTurn = new RunningProjectTurn();
					runningProjectTurn.setProjectId(runningProject.getId());
					runningProjectTurn.setTurnNum(map.get("turnNum"));
					runningProjectTurn.setComment(map.get("comment"));
					runningProjectTurnService.updateById(runningProjectTurn);
					String turnId = runningProjectTurnServiceOne.getId();
					List<String> versionList = Arrays.asList(map.get("versionStr").split(","));
					if(versionList.size() > 0 && versionList != null){
						for(String version : versionList){
							QueryWrapper<RunningProjectTurnVersion> versionQueryWrapper = new QueryWrapper<>();
							versionQueryWrapper.eq("turn_id", turnId);
							RunningProjectTurnVersion runningProjectTurnVersionServiceOne = runningProjectTurnVersionService.getOne(versionQueryWrapper);
							if(runningProjectTurnVersionServiceOne != null){
								runningProjectTurnVersionServiceOne.setVersionId(version);
								runningProjectTurnVersionService.updateById(runningProjectTurnVersionServiceOne);
							}
						}
					}
				}else {
					RunningProjectTurn runningProjectTurn = new RunningProjectTurn();
					runningProjectTurn.setProjectId(runningProject.getId());
					runningProjectTurn.setTurnNum(map.get("turnNum"));
					runningProjectTurn.setComment(map.get("comment"));
					runningProjectTurnService.save(runningProjectTurn);
					String turnId = runningProjectTurn.getId();
					List<String> versionList = Arrays.asList(map.get("versionStr").split(","));
					if(versionList.size() > 0 && versionList != null){
						for(String version : versionList){
							RunningProjectTurnVersion runningProjectTurnVersion = new RunningProjectTurnVersion();
							runningProjectTurnVersion.setTurnId(turnId);
							runningProjectTurnVersion.setVersionId(version);
							runningProjectTurnVersionService.save(runningProjectTurnVersion);
						}
					}
				}
			}
		}


		// dq add 往操作历史表中插入记录
		insertHistory(runningProject, 1, runningProject.getId());
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id逻辑删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目管理-通过id逻辑删除")
	@ApiOperation(value = "项目管理-通过id逻辑删除", notes = "项目管理-通过id逻辑删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		// 逻辑删除项目信息、项目下的任务信息、测试用例信息、问题信息
		// dq add 往操作历史表中插入记录
		RunningProject record=runningProjectService.getById(id);
		record.setDelFlag(1);
		runningProjectService.updateById(record);
		insertHistory(record, 2, id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目管理-批量删除")
	@ApiOperation(value = "项目管理-批量删除", notes = "项目管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		List<RunningProject> runningProjects=runningProjectService.listByIds(Arrays.asList(ids.split(",")));
		for (RunningProject record :runningProjects){
			record.setDelFlag(1);
			insertHistory(record,2,record.getId());
		}
		runningProjectService.updateBatchById(runningProjects);
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目管理-通过id查询")
	@ApiOperation(value = "项目管理-通过id查询", notes = "项目管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		List<RunningProjectInfo> projectList = runningProjectService.getListDataById(id);
		List<RunningProjectInfo> runningProjectInfoList = new ArrayList<>();
		for (RunningProjectInfo runningProjectInfo: projectList) {
			RunningUutList runningUutList = runningUutListService.getById(runningProjectInfo.getUutListId());
			// 获取jeecg-boot-uut数据库running_uut_version表被测对象对应最高版本
			String version = runningUutListService.queryUutVersionById(runningProjectInfo.getUutListId());
			if(null != runningUutList){
				runningProjectInfo.setUutListId(runningUutList.getId());
				runningProjectInfo.setUutName(runningUutList.getUutName());
				runningProjectInfo.setUutCode(runningUutList.getUutCode());
				runningProjectInfo.setUutType(runningUutList.getUutType());
//				runningProjectInfo.setUutVersion(runningUutList.getUutVersion());
				runningProjectInfo.setUutVersion(version);
				runningProjectInfo.setUutFile(runningUutList.getUutFile());
				runningProjectInfo.setUutOtherFile(runningUutList.getUutOtherFile());
			}
			runningProjectInfoList.add(runningProjectInfo);
		}
		if (runningProjectInfoList.size() <= 0) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningProjectInfoList);
	}

	/**
	 * 获取总项目数
	 *
	 * @return 总项目数
	 */
	@AutoLog(value = "获取总项目数")
	@ApiOperation(value = "项目管理-获取总项目数", notes = "项目管理-获取总项目数")
	@GetMapping(value = "/getProjectNums")
	public Result<?> getProjectNums() {
		Integer pNums = runningProjectService.getProjectNums();
		if (pNums == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pNums);
	}

	/**
	 * 完成项目
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "项目表-更新项目状态")
	@ApiOperation(value = "完成项目", notes = "项目表-更新项目状态")
	@PostMapping(value = "/updateFinishStatus")
	public Result<?> finishProject(@RequestBody RunningProject runningProject) {
		// 更新项目状态为-完成项目
		RunningProject project1 = new RunningProject();
		project1.setId(runningProject.getId());
		project1.setFinishStatus(1);
		runningProjectService.updateById(project1);
		return Result.ok("更新成功！");
	}

	/**
	 * 获取已出库项目信息接口
	 *
	 * @return 项目信息
	 */
	@RequestMapping(value = "/getProjectInfo", method = RequestMethod.POST)
	public Result<?> getProjectInfo(@RequestBody JSONObject jsonObject) {
		String publicKey = jsonObject.getString("publicKey");
		String token = jsonObject.getString("token");
		Optional<SysSecretKey> sysSecretKey = sysSecretKeyService.lambdaQuery()
				.eq(SysSecretKey::getPublicKey, publicKey).eq(SysSecretKey::getToken, token).oneOpt();
		if (!sysSecretKey.isPresent()) {
			return Result.error("密钥或token不正确！");
		}
		List<RunningProject> list = runningProjectService.getProjectInfo();
		List<Map<String, Object>> resList = new ArrayList<>();
		if (list == null || list.isEmpty()) {
			String projectInfo = JSON.toJSONString(resList);
			String secret = SecurityUtil.jiami(projectInfo);
			return Result.ok(secret);
		}
		for (int i = 0; i < list.size(); i++) {
			String projectId = list.get(i).getId();
			String projectName = list.get(i).getProjectName();
			String projectCode = list.get(i).getProjectCode();
			Map<String, Object> map = new HashMap<>(2000);
			map.put("projectId", projectId);
			map.put("projectName", projectName);
			map.put("projectCode", projectCode);
			resList.add(map);
		}
		// List转json
		String projectInfo = JSON.toJSONString(resList);
		String secret = SecurityUtil.jiami(projectInfo);
		return Result.ok(secret);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param runningProject
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, RunningProject runningProject) {
		return super.exportXls(request, runningProject, RunningProject.class, "项目管理");
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
		return super.importExcel(request, response, RunningProject.class);
	}

	/**
	 * dq add 根据项目id查询 任务数量 测试用例数量 问题数量
	 * @param queryParams
	 * @return 没有返回值
	 */
	@RequestMapping(value = "/getRelatedCount", method = RequestMethod.POST)
	public Result<?> getRelatedCount(@RequestBody Map<String, Object> queryParams) {
		String projectId = (String) queryParams.get("projectId");
		ArrayList<Map<String, Object>> returnList = new ArrayList<>();
		returnList.add(runningProjectService.getRelatedCount(projectId));
		return Result.ok(returnList);
	}

	/**
	 * dq add 向操作历史表中插入操作记录 参数: RunningProject originData: 新增和编辑操作时，会把最新数据存起来
	 * Integer opTye: 操作类型,0:新增 1:编辑 2:删除 String mainId: 主表id
	 */
	public void insertHistory(RunningProject originData, Integer opType, String mainId) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		RunningProjectHistory runningProjectHistory = new RunningProjectHistory();
		int sign2=2;
		if (opType == 0) {
			// 新增时把源数据在历史表中备份一个
			BeanUtils.copyProperties(originData, runningProjectHistory);
			runningProjectHistory.setSort(getMaxSortByMainId(mainId));
		}
		if (opType == sign2) {
			// 删除
			runningProjectHistory.setSort(getMaxSortByMainId(mainId));
			runningProjectHistory.setDelFlag(1);
		}
		if (opType == 0 || opType == sign2) {
			runningProjectHistory.setId(null);
			runningProjectHistory.setUpdateBy(sysUser.getUsername());
			runningProjectHistory.setOpType(opType);
			runningProjectHistory.setMainId(mainId);
			runningProjectHistory.setUpdateTime(new Date());
			historyService.save(runningProjectHistory);
		}
		if (opType == 1) {
			// 编辑操作单独处理,改过东西才会保存
			List<RunningProjectHistory> historyList = originData.getModifiedList();
			if (historyList != null && historyList.size() > 0) {
				// 当前插入的sort
				Long insertSort = getMaxSortByMainId(mainId);
				RunningProjectHistory record=new RunningProjectHistory();
				// 备份最新数据
				BeanUtils.copyProperties(originData, record);
				record.setId(null);
				record.setMainId(mainId);
				record.setUpdateBy(sysUser.getUsername());
				record.setSort(insertSort);
				record.setUpdateTime(new Date());
				record.setOpType(1);
			}
			historyService.saveBatch(historyList);
		}
	}

	/**
	 * dq add 返回本次插入历史表中的sort值,同次操作sort值相同
	 */
	public Long getMaxSortByMainId(String mainId) {
		QueryWrapper<RunningProjectHistory> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("main_id", mainId);
		queryWrapper.orderByDesc("create_time");
		List<RunningProjectHistory> list = historyService.list(queryWrapper);
		if (list == null || list.size() == 0) {
			return 0L;
		} else {
			Long currentSort = list.get(0).getSort();
			return currentSort + 1;
		}
	}

	/**
	 * dq add 操作历史记录查询
	 *
	 */
	@AutoLog(value = "操作历史查询")
	@ApiOperation(value = "操作历史查询", notes = "操作历史查询")
	@GetMapping(value = "/historyList")
	public Result<?> queryPageList(RunningProjectHistory runningProjectHistory,
	                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
	                               HttpServletRequest req) {
		Page page = new Page(pageNo, pageSize);
		return Result.ok(runningProjectService.getOperationHistoryList(page, runningProjectHistory));
	}

	/**
	 * dq add 通过id查询项目详情
	 *
	 * @param id id
	 * @return 没有返回值
	 */
	@AutoLog(value = "操作记录-通过id查询")
	@ApiOperation(value = "操作记录-通过id查询", notes = "操作记录-通过id查询")
	@GetMapping(value = "/queryHistoryById")
	public Result<?> queryHistoryById(@RequestParam(name = "id", required = true) String id) {
		RunningProjectHistory runningProjectHistory = historyService.getById(id);
		if (runningProjectHistory == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningProjectHistory);
	}

	@AutoLog(value = "导航栏按钮查询")
	@ApiOperation(value = "导航栏按钮查询", notes = "导航栏按钮查询")
	@GetMapping(value = "/projectList")
	public Result<?> projectPageList(@RequestParam(name = "projectName", required = false) String projectName,
	                                 @RequestParam(name = "projectCode", required = false) String projectCode,
	                                 @RequestParam(name = "createTime", required = false) String createTime,
	                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
	                                 HttpServletRequest req) {
		Page<RunningProjectInfo> pageList = new Page<>(pageNo, pageSize);
		pageList = runningProjectService.queryPageList(pageList, projectName, projectCode,createTime,"");
		log.info("查询当前页：" + pageList.getCurrent());
		log.info("查询当前页数量：" + pageList.getSize());
		log.info("查询结果数量：" + pageList.getRecords().size());
		log.info("数据总数：" + pageList.getTotal());
		return Result.ok(pageList);
	}

	@AutoLog(value = "导航栏赋值")
	@ApiOperation(value = "导航栏赋值", notes = "导航栏赋值")
	@GetMapping(value = "/getProjectName")
	public String getProjectName(@RequestParam(name = "projectId") String projectId) {
		return runningProjectService.getProjectName(projectId);
	}

	@AutoLog(value = "根据项目名称查询项目id")
	@ApiOperation(value = "根据项目名称查询项目id", notes = "根据项目名称查询项目id")
	@GetMapping(value = "/getProjectId")
	public Result<?> getProjectId(@RequestParam(name = "projectCode") String projectCode) {
		return Result.ok(runningProjectService.getProjectId(projectCode));
	}


	@AutoLog(value = "获取轮次")
	@ApiOperation(value = "获取轮次", notes = "获取轮次")
	@GetMapping(value = "/getTurn")
	public List<RunningProjectTurn> getTurn(@RequestParam(name = "projectId", required = false) String projectId) {
		List<RunningProjectTurn> result = null;
		if(!StringUtils.isEmpty(projectId)){
			result = new ArrayList<>();
			List<RunningProjectTurn> runningProjectTurnList = runningProjectService.getTurn(projectId);
			for (RunningProjectTurn runningProjectTurn : runningProjectTurnList) {
				List<RunningProjectTurnVersion> uutTurnVersion = runningProjectTurn.getUutTurnVersion();
				StringBuilder stringBuffer = new StringBuilder("");
				for (RunningProjectTurnVersion runningProjectTurnVersion : uutTurnVersion) {
					if("".equals(stringBuffer.toString())){
						stringBuffer.append(runningProjectTurnVersion.getVersionId());
					}else{
						stringBuffer.append("," + runningProjectTurnVersion.getVersionId());
					}
				}
				runningProjectTurn.setVersionStr(stringBuffer.toString());
				result.add(runningProjectTurn);
			}
		}
		return result;
	}

	@AutoLog(value = "获取轮次")
	@ApiOperation(value = "获取轮次", notes = "获取轮次")
	@GetMapping(value = "/getTurnId")
	public Result<List<DictModel>> getTurnId(@RequestParam(name = "projectId", required = false) String projectId,
							   @RequestParam(name = "turnId", required = false) String turnId) {
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		List<DictModel> ls = null;
		try {
			ls = runningProjectService.getOptionByCondition(projectId, turnId);
			result.setSuccess(true);
			result.setResult(ls);
		} catch (Exception e) {
			result.error500("操作失败");
			return result;
		}
		return result;
	}

	@AutoLog(value = "通过taskId获取轮次")
	@ApiOperation(value = "通过taskId获取轮次", notes = "通过taskId获取轮次")
	@GetMapping(value = "/getTurnIdByTaskId")
	public Result<List<DictModel>> getTurnIdByTaskId(@RequestParam(name = "taskId", required = false) String taskId,
											 @RequestParam(name = "turnId", required = false) String turnId) {
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		List<DictModel> ls = null;
		try {
			ls = runningProjectService.getOptionByConditionByTaskId(taskId, turnId);
			result.setSuccess(true);
			result.setResult(ls);
		} catch (Exception e) {
			result.error500("操作失败");
			return result;
		}
		return result;
	}

	@AutoLog(value = "获取轮次")
	@ApiOperation(value = "获取轮次", notes = "获取轮次")
	@GetMapping(value = "/getAllVersion")
	public List<Map<String, String>> getAllVersion(@RequestParam(name = "uutId",required = false) String uutId) {
		return runningProjectService.getAllVersion(uutId);
	}

	@AutoLog(value = "获取测试版本id")
	@ApiOperation(value = "获取测试版本id" , notes = "获取测试版本id")
	@GetMapping(value = "/getProjectTurnVersionId")
	public Result<?> getProjectTurnVersionId(@RequestParam(name = "turnId" , required = true) String turnId){

		// 定义最终存放数据集合
		List<Map<String, Object>> resultList = new ArrayList<>();

		List<String> versionIds =  runningProjectTurnVersionService.getProjectTurnVersionId(turnId);

		if (versionIds.isEmpty()) {
			return Result.error("未找到对应数据");
		}
		if(!versionIds.isEmpty()){
			for(String versionId : versionIds){
				// 根据轮次版本id查询轮次版本
				String realVersion = runningUutVersionService.getProjectTurnVersion(versionId);
				if(realVersion == null){
					return Result.error("未找到对应数据");
				}else {
					Map<String,Object> map = new HashMap<>(2000);
					map.put("label", realVersion);
					map.put("value", versionId);
					resultList.add(map);
				}
			}
		}
		return Result.ok(resultList);
	}



	/**
	 * 通过id查询
	 *
	 * @param id id
	 * @return 没有返回值
	 */
	@AutoLog(value = "被测对象列表-通过id查询")
	@ApiOperation(value="被测对象列表-通过id查询", notes="被测对象列表-通过id查询")
	@GetMapping(value = "/queryByUutId")
	public Result<?> queryByUutId(@RequestParam(name="id",required=true) String id) {
		RunningUutListVo runningUutListVo = runningUutListService.findUniqueVoBy("id", id);
		if(runningUutListVo == null) {
			return Result.error("未找到对应数据");
		}
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		runningUutListVo.setApplierDictText(sysUser.getRealname());
		runningUutListVo.setApplier(sysUser.getUsername());
		runningUutListVo.setRunningManager(null);
		return Result.ok(runningUutListVo);
	}

	/**
	 * 通过id查询被测对象最高版本
	 *
	 * @param id id
	 * @return 没有返回值
	 */
	@AutoLog(value = "被测对象列表-通过id查询被测对象最高版本")
	@ApiOperation(value="被测对象列表-通过id查询被测对象最高版本", notes="被测对象列表-通过id查询被测对象最高版本")
	@GetMapping(value = "/queryUutVersionById")
	public String queryUutVersionById(@RequestParam(name="id",required=true) String id) {
		String version = runningUutListService.queryUutVersionById(id);
		return version;
	}

	/**
	 * 获取用户列表数据
	 * @param user 用户名
	 * @param pageNo 页数
	 * @param pageSize 总页数
	 * @param req true
	 * @return 没有返回值
	 */
	@PermissionData(pageComponent = "system/UserList")
	@RequestMapping(value = "/getProjectUser", method = RequestMethod.GET)
	public Result<IPage<SysUser>> queryPageList(SysUser user, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
	                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
	                                            @RequestParam(name="projectId", required = false) String projectId,
	                                            @RequestParam(name="userName", required = false) String userName,
	                                            @RequestParam(name="uutListId", required = false) String uutListId,HttpServletRequest req) {
		Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
		Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
		Page<RunningUutListUser> runningUutListUserList = new Page<RunningUutListUser>(pageNo, pageSize);
		if(!StringUtils.isEmpty(uutListId)){
			QueryWrapper<RunningUutListUser> userQueryWrapper = QueryGenerator.initQueryWrapper(new RunningUutListUser(), null);
			userQueryWrapper.in("uut_id", uutListId);
			List<String> userIds = runningProjectService.getUserIdList(userName);
			userQueryWrapper.in("user_id", userIds);
			runningUutListUserList = runningUutListUserService.page(runningUutListUserList, userQueryWrapper);
		}
		if(!StringUtils.isEmpty(projectId)){
			QueryWrapper<RunningUutListUser> userQueryWrapper = QueryGenerator.initQueryWrapper(new RunningUutListUser(), null);
			if(StringUtils.isEmpty(uutListId)){
				RunningProject runningProject = runningProjectService.getById(projectId);
				userQueryWrapper.eq("uut_id", runningProject.getUutListId());
			}else {
				userQueryWrapper.eq("uut_id", uutListId);
			}
			List<String> userIds = runningProjectService.getUserIdList(userName);
			userQueryWrapper.in("user_id", userIds);
			runningUutListUserList = runningUutListUserService.page(runningUutListUserList, userQueryWrapper);
		}
		List<SysUser> record = new ArrayList<>();
		if(runningUutListUserList.getSize() > 0){
			for (RunningUutListUser runningUutListUser : runningUutListUserList.getRecords()) {
				SysUser sysUser = sysUserService.getById(runningUutListUser.getUserId());
				record.add(sysUser);
			}
		}
		page.setRecords(record);
//		QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
		//TODO 外部模拟登陆临时账号，列表不显示
//		queryWrapper.ne("username","_reserve_user_external");
//
//		IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);

		//批量查询用户的所属部门
		//step.1 先拿到全部的 useids
		//step.2 通过 useids，一次性查询用户的所属部门名字
		List<String> userIds = page.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
		if(userIds != null && userIds.size()>0){
			Map<String,String>  useDepNames = sysUserService.getDepNamesByUserIds(userIds);
			page.getRecords().forEach(item->{
				item.setOrgCodeTxt(useDepNames.get(item.getId()));
			});
		}
		result.setSuccess(true);
		result.setResult(page);
		log.info(page.toString());
		return result;
	}

	/**
	 * 获取用户列表数据
	 * @param params true
	 * @return 没有返回值
	 */
	@AutoLog(value = "通过id查询角色")
	@ApiOperation(value = "通过id查询角色", notes = "通过id查询角色")
	@RequestMapping(value = "/getSysUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getSysUser(@RequestBody Map<String, Object> params) {
		StringBuilder sysUserName = new StringBuilder("");
		for (String userId: (List<String>) params.get("sysUserId")) {
			if (StringUtils.isEmpty(sysUserName.toString())) {
				sysUserName.append(sysUserService.getRealNameById(userId));
			} else {
				sysUserName.append("," + sysUserService.getRealNameById(userId));
			}
		}
		return sysUserName.toString();
	}

	/**
	 * 通过projectId、roleId查询用户
	 *
	 *
	 * */
	@AutoLog(value = "查询角色名称、用户名称")
	@ApiOperation(value = "查询角色名称、用户名称", notes = "查询角色名称、用户名称")
	@GetMapping(value = "/getRoleToUser")
	public Result<?> getRoleToUser(@RequestParam(name="projectId", required = true)String projectId) {
		List<Map<String,String>> getRoleToUser = runningProjectService.getRoleToUser(projectId);
		return Result.ok(getRoleToUser);
	}

	/**
	 * 获取被测对象名称,id
	 * @return Result<List<DictModel>>
	 */
	@AutoLog(value = "获取被测对象名称,id")
	@ApiOperation(value = "获取被测对象名称,id", notes = "获取被测对象名称,id")
	@GetMapping(value = "/getUutNameByStatus")
	public Result<List<DictModel>> getUutNameByStatus() {
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		List<DictModel> ls = null;
		try {
			ls = runningProjectService.getUutNameByStatus();
			result.setSuccess(true);
			result.setResult(ls);
		} catch (Exception e) {
			result.error500("操作失败");
			return result;
		}
		return result;
	}


}