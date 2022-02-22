package org.jeecg.modules.task.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.project.service.ProjectUserAssociationService;
import org.jeecg.modules.task.feign.IRunningUutRecordController;
import org.jeecg.modules.task.mapper.RunningCaseMapper;
import org.jeecg.modules.task.model.CaseTreeIdModel;
import org.jeecg.modules.task.vo.RunningCaseVO;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.task.entity.*;
import org.jeecg.modules.task.service.*;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 测试用例表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Api(tags="测试用例表")
@RestController
@RequestMapping("/task/runningCase")
@Slf4j
public class RunningCaseController extends JeecgController<RunningCase, IRunningCaseService> {
	@Autowired
	private IRunningCaseService runningCaseService;

	 @Autowired
	 private IRunningCaseStepService runningCaseStepService;

	 @Autowired
	 private IRunningCaseStepHistoryService runningCaseStepHistoryService;
	
	@Autowired
	private IRunningTaskService runningTaskService;

	@Autowired
	private IRunningCaseHistoryService runningCaseHistoryService;
	
	@Autowired
	private ProjectUserAssociationService projectUserAssociationService;
	
	@Autowired
	private ISysUserService  sysUserService;

	@Autowired
	private IRunningUutRecordController runningUutRecordController;

	@Autowired
	private RunningCaseMapper runningCaseMapper;

	@Value(value = "${jeecg.caseRecommend.caseRecommend-url}")
	private String caseRecommendUrl;

	/**
	 * 分页列表查询
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试用例表-分页列表查询")
	@ApiOperation(value="测试用例表-分页列表查询", notes="测试用例表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name="testTaskId", required = false) String testTaskId,
			   @RequestParam(name="testName", required = false) String testName,
			   @RequestParam(name="testCode", required = false) String testCode,
			   @RequestParam(name="testPerson", required = false) String testPerson,
			   @RequestParam(name="testDate", required = false) String testDate,
			   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
		Page<RunningCaseVO> pageList = new Page<RunningCaseVO>(pageNo, pageSize);
		pageList = runningCaseService.queryPageList(pageList,testTaskId,testName,testCode,testPerson,testDate);
//		// 当前用户是否是创建者
//		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
//		String username = sysUser.getUsername();
//		List<RunningCaseVO> runningCasList = pageList.getRecords();
//		for (RunningCaseVO runningCaseVO : runningCasList) {
//			if (username != null && !username.isEmpty() && username.equals(runningCaseVO.getCreateBy())) {
//				runningCaseVO.setCreaterFalg(true);
//			} else if ("admin".equals(username)) {
//				runningCaseVO.setCreaterFalg(true);
//			} else {
//				runningCaseVO.setCreaterFalg(false);
//			}
//		}
		log.info("查询当前页："+pageList.getCurrent());
		log.info("查询当前页数量："+pageList.getSize());
		log.info("查询结果数量："+pageList.getRecords().size());
		log.info("数据总数："+pageList.getTotal());		
		return Result.ok(pageList);
	}

	 /**
	  * 测试用例表-已归档测试用例列表查询
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "测试用例表-已归档测试用例列表查询")
	 @ApiOperation(value="测试用例表-已归档测试用例列表查询", notes="测试用例表-已归档测试用例列表查询")
	 @GetMapping(value = "/fileCaselist")
	 public Result<?> queryFileCasePageList(@RequestParam(name="testTaskId", required = false) String testTaskId,
									@RequestParam(name="testName", required = false) String testName,
									@RequestParam(name="testCode", required = false) String testCode,
									@RequestParam(name="testPerson", required = false) String testPerson,
									@RequestParam(name="testDate", required = false) String testDate,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
		 Page<RunningCaseVO> pageList = new Page<RunningCaseVO>(pageNo, pageSize);
		 pageList = runningCaseService.queryFileCasePageList(pageList,testTaskId,testName,testCode,testPerson,testDate);
//		// 当前用户是否是创建者
//		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
//		String username = sysUser.getUsername();
//		List<RunningCaseVO> runningCasList = pageList.getRecords();
//		for (RunningCaseVO runningCaseVO : runningCasList) {
//			if (username != null && !username.isEmpty() && username.equals(runningCaseVO.getCreateBy())) {
//				runningCaseVO.setCreaterFalg(true);
//			} else if ("admin".equals(username)) {
//				runningCaseVO.setCreaterFalg(true);
//			} else {
//				runningCaseVO.setCreaterFalg(false);
//			}
//		}
		 log.info("查询当前页："+pageList.getCurrent());
		 log.info("查询当前页数量："+pageList.getSize());
		 log.info("查询结果数量："+pageList.getRecords().size());
		 log.info("数据总数："+pageList.getTotal());
		 return Result.ok(pageList);
	 }

	/* *//**
	  * 根据用例名称模糊查询
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  *//*
	 @AutoLog(value = "测试用例表-根据用例名称模糊查询")
	 @ApiOperation(value="测试用例表-根据用例名称模糊查询", notes="测试用例表-根据用例名称模糊查询")
	 @GetMapping(value = "/listByTestName")
	 public Result<?> queryPageListByTestName(@RequestParam(name="testName", required = false) String testName,
											@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
											@RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
		 Page<RunningCaseVO> pageList = new Page<RunningCaseVO>(pageNo, pageSize);
//		 pageList = runningCaseService.queryPageListByTestName(pageList,testName);
		 if(testName != null){
			 // 使用测试用例推荐接口
//			 String result = HttpUtil.doPost("http://47.95.194.22:40000/search/?keyword=", testName);
			 Map<String, Object> params = new HashMap<>();
			 params.put("keyword", testName);
			 String result = HttpUtil.doPost(caseRecommendUrl + "/search" , params, null);
			 HashMap parseMap = JSONObject.parseObject(result, HashMap.class);
			 List<Map<String, Object>> listMap = (List<Map<String, Object>>) parseMap.get("casels");
			 List<RunningCaseVO> runningCaseVOList = new ArrayList<>();
			 for (int i = 0; i < listMap.size(); i++ ){
			 	RunningCaseVO runningCaseVO = new RunningCaseVO();
			 	runningCaseVO.setId((String) listMap.get(i).get("caseid"));
			 	runningCaseVO.setTestName((String) listMap.get(i).get("casename"));
			 	runningCaseVO.setTestInstructions((String) listMap.get(i).get("caseintro"));
			 	runningCaseVO.setTestVersion((String) listMap.get(i).get("targetname"));
//			 	String detailResult = HttpUtil.doGet("http://47.95.194.22:40000/detail", (String) listMap.get(i).get("caseid"));
				 Map<String,Object> map = new HashMap<>();
				 map.put("caseid", (String) listMap.get(i).get("caseid"));
				 String detailResult = HttpUtil.doGet(caseRecommendUrl + "/detail", map, null);
			 	HashMap<String, Object> detailMap = JSONObject.parseObject(detailResult, HashMap.class);
			 	if(detailMap != null){
					runningCaseVO.setTestInit((String) detailMap.get("测试用例初始化"));
					runningCaseVO.setTestCode((String) detailMap.get("测试用例标识"));
					runningCaseVO.setTestCriterion((String) detailMap.get("评估准则"));
					List<ArrayList> caseStepList = ((JSONArray) detailMap.get("测试过程")).toJavaList(ArrayList.class);
					List<RunningCaseStep> runningCaseStepList = new ArrayList<>();
					if( caseStepList != null && caseStepList.size() > 1 ){
						for(int j = 1; j < caseStepList.size(); j++){
							RunningCaseStep runningCaseStep = new RunningCaseStep();
							runningCaseStep.setStepId((String) caseStepList.get(j).get(0));
							runningCaseStep.setOperatingInstructions((String) caseStepList.get(j).get(1));
							runningCaseStep.setExpectResult((String) caseStepList.get(j).get(2));
							runningCaseStepList.add(runningCaseStep);
						}
					}
					runningCaseVO.setStepList(runningCaseStepList);
					runningCaseVO.setTestOver((String) detailMap.get("终止条件"));
					runningCaseVO.setTestConstraint((String) detailMap.get("前提与约束"));
				}
				runningCaseVOList.add(runningCaseVO);
			 }
			 pageList = pageList.setRecords(runningCaseVOList);
		 }

//		 log.info("查询当前页："+pageList.getCurrent());
//		 log.info("查询当前页数量："+pageList.getSize());
//		 log.info("查询结果数量："+pageList.getRecords().size());
//		 log.info("数据总数："+pageList.getTotal());
		 return Result.ok(pageList);
	 }*/
	
	@GetMapping("count")
	public Integer count(String row,String val) {
		QueryWrapper<RunningCase> wrapper = new QueryWrapper<>();
		wrapper.eq(row, val);
		return runningCaseService.count(wrapper);
	}
	
	/**
	 *   添加
	 *
	 * @param runningCase
	 * @return
	 */
	@AutoLog(value = "测试用例表-添加")
	@ApiOperation(value="测试用例表-添加", notes="测试用例表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningCase runningCase) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		runningCase.setCreateBy(sysUser.getUsername());
		runningCase.setCreateTime(new Date());
//		RunningUutList runningUutList =
//				runningCaseService.getUutListByTaskId(runningCase.getTestTaskId());
//		if(null != runningUutList){
//			runningCase.setTestVersion(runningUutList.getUutVersion());
//		}
		runningCase.setTestVersion(runningCaseService.getUutVersionId(runningCase.getTurnVersion()));
		runningCase.setFileStatus(0);
		runningCaseService.save(runningCase);
		/**循环存值*/
		for (int i = 0;i < runningCase.getStepList().size();i++){
			RunningCaseStep runningCaseStep = new RunningCaseStep();
			runningCaseStep.setCaseId(runningCase.getId());
			runningCaseStep.setStepId(runningCase.getStepList().get(i).getStepId());
			runningCaseStep.setOperatingInstructions(runningCase.getStepList().get(i).getOperatingInstructions());
			runningCaseStep.setExpectResult(runningCase.getStepList().get(i).getExpectResult());
			runningCaseStep.setTestResult(runningCase.getStepList().get(i).getTestResult());
			runningCaseStep.setTurnId(runningCase.getTurnId());
			runningCaseStepService.save(runningCaseStep);
		}
		String copy="copy";
		if(copy.equals(runningCase.getOpFlag())){
			//复制添加历史记录
			insertHistory(runningCase,"3",runningCase.getId());
			for (int i = 0;i < runningCase.getStepList().size();i++){
				RunningCaseStepHistory runningCaseStepHistory = new RunningCaseStepHistory();
				runningCaseStepHistory.setCaseId(runningCase.getId());
				runningCaseStepHistory.setStepId(runningCase.getStepList().get(i).getStepId());
				runningCaseStepHistory.setOperatingInstructions(runningCase.getStepList().get(i).getOperatingInstructions());
				runningCaseStepHistory.setExpectResult(runningCase.getStepList().get(i).getExpectResult());
				runningCaseStepHistory.setTestResult(runningCase.getStepList().get(i).getTestResult());
				runningCaseStepHistory.setTurnId(runningCase.getTurnId());
				runningCaseStepHistory.setOpType("3");
				runningCaseStepHistoryService.save(runningCaseStepHistory);
			}
		}else{
			//新增添加历史记录
			insertHistory(runningCase,"0",runningCase.getId());
			for (int i = 0;i < runningCase.getStepList().size();i++){
				RunningCaseStepHistory runningCaseStepHistory=new RunningCaseStepHistory();
				runningCaseStepHistory.setCaseId(runningCase.getId());
				runningCaseStepHistory.setStepId(runningCase.getStepList().get(i).getStepId());
				runningCaseStepHistory.setOperatingInstructions(runningCase.getStepList().get(i).getOperatingInstructions());
				runningCaseStepHistory.setExpectResult(runningCase.getStepList().get(i).getExpectResult());
				runningCaseStepHistory.setTestResult(runningCase.getStepList().get(i).getTestResult());
				runningCaseStepHistory.setTurnId(runningCase.getTurnId());
				runningCaseStepHistory.setOpType("0");
				runningCaseStepHistoryService.save(runningCaseStepHistory);
			}
		}
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningCase
	 * @return
	 */
	@AutoLog(value = "测试用例表-编辑")
	@ApiOperation(value="测试用例表-编辑", notes="测试用例表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningCase runningCase) {
		runningCase.getStepList().forEach(i->i.setCaseId(runningCase.getId()));
		runningCaseService.updateById(runningCase);
		runningCaseStepService.deleteByCaseId(runningCase.getId());
		/**循环存值*/
		for (int i = 0;i < runningCase.getStepList().size();i++){
			RunningCaseStep runningCaseStep = new RunningCaseStep();
			runningCaseStep.setCaseId(runningCase.getId());
			runningCaseStep.setStepId(runningCase.getStepList().get(i).getStepId());
			runningCaseStep.setOperatingInstructions(runningCase.getStepList().get(i).getOperatingInstructions());
			runningCaseStep.setExpectResult(runningCase.getStepList().get(i).getExpectResult());
			runningCaseStep.setTestResult(runningCase.getStepList().get(i).getTestResult());
			runningCaseStep.setTurnId(runningCase.getTurnId());
			runningCaseStepService.save(runningCaseStep);
		}

		/*修改添加历史记录*/
		if (runningCase.getIsModified()){
			String caseHistoryId = insertHistory(runningCase,"1",runningCase.getId());
			for (int i = 0;i < runningCase.getStepList().size();i++){
				RunningCaseStepHistory runningCaseStepHistory=new RunningCaseStepHistory();
				runningCaseStepHistory.setCaseId(caseHistoryId);
				runningCaseStepHistory.setStepId(runningCase.getStepList().get(i).getStepId());
				runningCaseStepHistory.setOperatingInstructions(runningCase.getStepList().get(i).getOperatingInstructions());
				runningCaseStepHistory.setExpectResult(runningCase.getStepList().get(i).getExpectResult());
				runningCaseStepHistory.setTestResult(runningCase.getStepList().get(i).getTestResult());
				runningCaseStepHistory.setTurnId(runningCase.getTurnId());
				runningCaseStepHistory.setOpType("1");
				runningCaseStepHistoryService.save(runningCaseStepHistory);
			}
		}
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试用例表-通过id删除")
	@ApiOperation(value="测试用例表-通过id删除", notes="测试用例表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
//		//逻辑删除测试用例信息，测试用例下面的问题信息
//		//删除时添加历史记录

		RunningCase runningCase = runningCaseService.getById(id);
		runningCase.setId(id);
		runningCase.setDelFlag(1);
		runningCaseService.updateById(runningCase);
		insertHistory(runningCase,"2",runningCase.getId());
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试用例表-批量删除")
	@ApiOperation(value="测试用例表-批量删除", notes="测试用例表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {

		List<RunningCase> runningCaseList = runningCaseService.listByIds(Arrays.asList(ids.split(",")));
		for (RunningCase runningCase:runningCaseList){
			runningCase.setDelFlag(1);
			runningCase.setUpdateTime(new Date());
			insertHistory(runningCase,"2",runningCase.getId());
		}
		runningCaseService.updateBatchById(runningCaseList);
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试用例表-通过id查询")
	@ApiOperation(value="测试用例表-通过id查询", notes="测试用例表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningCase runningCase = runningCaseService.getById(id);
		if(runningCase==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningCase);
	}
	
	
	/**
	 * 通过任务id查询项目id
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "测试任务表-通过id查询project_id")
	@ApiOperation(value="测试任务表-通过id查询project_id", notes="测试任务表-通过id查询project_id")
	@GetMapping(value = "/getProjectIdByTaskId")
	public Result<?> getProjectIdByTaskId(@RequestParam(name="taskId",required=true) String taskId) {
		RunningTask runningTask = runningTaskService.getById(taskId);
		
		if(runningTask==null) {
			return Result.error("未找到对应数据");
		}
		String projectId =runningTask.getProjectId();
		return Result.ok(projectId);
	}

	 /**
	  *  用例复用，查询复用所需数据
	  */
	 @AutoLog(value = "测试用例表-通过id查询")
	 @ApiOperation(value="测试用例表-通过id查询", notes="测试用例表-通过id查询")
	 @GetMapping(value = "/queryTestCopyDadaById")
	 public Result<?> queryTestCopyDadaById(@RequestParam(name="caseId",required=false) String caseId) {
		 List<RunningCase> caseList  = runningCaseService.getTestCopyDataById(caseId);
		 for(int i = 0; i < caseList.size(); i++){
		 	runningCaseService.save(caseList.get(i));
		 	if(caseList.get(i).getStepList() != null){
				for(int j = 0; j < caseList.get(i).getStepList().size(); j++){
					RunningCaseStep runningCaseStep = new RunningCaseStep();
					runningCaseStep.setCaseId(caseList.get(i).getId());
					runningCaseStep.setStepId(caseList.get(i).getStepList().get(j).getStepId());
					runningCaseStep.setOperatingInstructions(caseList.get(i).getStepList().get(j).getOperatingInstructions());
					runningCaseStep.setExpectResult(caseList.get(i).getStepList().get(j).getExpectResult());
					runningCaseStep.setTestResult(caseList.get(i).getStepList().get(j).getTestResult());
					runningCaseStep.setTurnId(caseList.get(i).getTurnId());
					runningCaseStepService.save(runningCaseStep);
				}
			}
		 }
		 if(caseList==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok("复用成功!");
	 }
	
	
	/**
	 * 复制按钮查询，查询复制所需数据
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试用例表-通过id查询不包含主键的唯一一条数据")
	@ApiOperation(value="测试用例表-通过id查询不包含主键的唯一一条数据", notes="测试用例表-通过id查询不包含主键的唯一一条数据")
	@GetMapping(value = "/queryCopyDataById")
	public Result<?> queryCopyDataById(@RequestParam(name="id",required=true) String id) {
		List<RunningCase> caseList  = runningCaseService.getCopyDataById(id);
//		List<RunningCase> caseList = runningCaseService.getTestCopyDataById(id);
		if(caseList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(caseList);
	}
	
	
	/**
	 * 查询测试用例类型树
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试用例表-通过id查询")
	@ApiOperation(value="测试用例表-通过id查询", notes="测试用例表-通过id查询")
	@GetMapping(value = "/queryTestCase")
	public Result<?> queryTestCase(@RequestParam(name="id",required=true) String id) {
		Result result = new Result();
		List<CaseTreeIdModel> list=null;
		//根据任务id,查询被测对象体系id
		String templateId=runningCaseService.getTestTemplateById(id);
		if(!templateId.isEmpty()) {
			//查询树形所需数据	
			list  = runningCaseService.queryTreeList(templateId);
			result.setResult(list);
		    result.setSuccess(true);
		}
		if(list==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(result);
	}
	
	
	/**
	 * 根据任务ID查询测试成员
	 *
	 * @param taskId
	 * @return projectName
	 */
	@AutoLog(value = "根据任务ID查询测试成员")
	@ApiOperation(value="根据任务ID查询测试成员", notes="根据任务ID查询测试成员")
	@GetMapping(value = "/getPersonDataByTaskId")
	public Result<?> getPersonDataByTaskId(@RequestParam(name="taskId",required=true) String taskId) {
		//根据任务ID查询项目ID
		RunningTask projectIds = runningCaseService.getProjectIdByTaskId(taskId);
		String projectId= projectIds.getProjectId();
		if(projectId==null) {
			return Result.error("未找到对应数据");
		}
		//定义最终存放数据集合
		List<Map<String,Object>> resultList = new ArrayList<>();

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

//		//根据项目ID查询项目成员ID
//		List<String> userIdList =  projectUserAssociationService.getUserIdsByProjectId(projectId);
//		if(userIdList.isEmpty()) {
//			return Result.error("未找到对应数据");
//		}
//		if(userIdList!=null) {
//			for(String userId:userIdList) {
//				//根据项目成员ID查询项目成员名称
//				String realName = sysUserService.getRealNameById(userId);
//				if(!realName.isEmpty()) {
//					Map<String,Object> map = new HashMap<>(2000);
//					map.put("label", realName);
//					map.put("value", userId);
//					resultList.add(map);
//				}
//			}
//		}
		return Result.ok(resultList);
	}
    /**
    * 导出excel
    *
    * @param request
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningCaseVO runningCaseVO) {
	    
    	LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
    	//获取导出数据
    	List<RunningCaseVO> pageList = new ArrayList<>();
    	String testTaskId =request.getParameter("testTaskId");
    	pageList = runningCaseService.getRunningCaseData1(testTaskId);
    	List<RunningCaseVO> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }
  
        //AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "测试用例列表");
        mv.addObject(NormalExcelConstants.CLASS, RunningCaseVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("测试用例表", "导出人:"+sysUser.getRealname(), "测试用例表数据"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
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
        return super.importExcel(request, response, RunningCase.class);
    }

	 /**
	  * 通过任务主键获取排序
	  *
	  * @param testTaskId wu
	  * @return wu
	  */
	 public Integer getMaxSortByTaskId(String testTaskId) {
		 QueryWrapper<RunningCaseHistory> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("test_task_id", testTaskId);
		 queryWrapper.orderByDesc("create_time");
		 List<RunningCaseHistory> list = runningCaseHistoryService.list(queryWrapper);
		 if (list == null || list.size() == 0) {
			 return 0;
		 } else {
			 Integer currentSort = list.get(0).getSort();
			 return currentSort + 1;
		 }
	 }

	 /**
	  * word导入
	  * @param multfile
	  * @return
	  */
	 @RequestMapping(value = "/importWord", method = RequestMethod.POST)
	 public Result<?> importWord(@RequestParam("file") MultipartFile multfile,@RequestParam(name="testTaskId",required=true) String testTaskId) {
	 	runningCaseService.importWordToDocx(multfile,testTaskId);
		 return Result.ok("导入成功");
	 }
	 public String insertHistory(RunningCase originData, String opType, String mainId)
	 {
		 LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		 RunningCaseHistory runningCaseHistory=new RunningCaseHistory();
		 String sign0="0";
		 String sign1="1";
		 String sign2="2";
		 String sign3="3";
		 String caseHistoryId = null;
		 if(sign0.equals(opType)||sign3.equals(opType))
		 {
			 // 新增时把源数据在历史表中备份一个
			 BeanUtils.copyProperties(originData,runningCaseHistory);
		 }
		 if(sign2.equals(opType))
		 {
			 // 删除
			 BeanUtils.copyProperties(originData,runningCaseHistory);
			 runningCaseHistory.setDelFlag(1);
		 }
		 if(sign0.equals(opType) || sign2.equals(opType)||sign3.equals(opType))
		 {
			 runningCaseHistory.setId(null);
			 runningCaseHistory.setTestTaskId(originData.getTestTaskId());
			 runningCaseHistory.setUpdateBy(sysUser.getId());
			 runningCaseHistory.setOperationType(opType);
			 runningCaseHistory.setUpdateTime(new Date());
			 runningCaseHistory.setMainId(mainId);
			 runningCaseHistory.setTestVersion(originData.getTestVersion());
			 runningCaseHistoryService.save(runningCaseHistory);
			 caseHistoryId = runningCaseHistory.getId();
		 }
		 if(sign1.equals(opType))
		 {
			 // 编辑操作单独处理,改过东西才会保存
			 RunningCaseHistory newEdit =new RunningCaseHistory();
			 // 备份最新数据
			 BeanUtils.copyProperties(originData,newEdit);
			 newEdit.setId(null);
			 runningCaseHistory.setTestTaskId(originData.getTestTaskId());
			 newEdit.setMainId(mainId);
			 newEdit.setCreateBy(originData.getCreateBy());
			 newEdit.setUpdateBy(sysUser.getId());
			 newEdit.setUpdateTime(new Date());
			 newEdit.setOperationType(opType);
			 newEdit.setTestVersion(originData.getTestVersion());
			 runningCaseHistoryService.save(newEdit);
			 caseHistoryId = newEdit.getId();
		 }
		 return caseHistoryId;
	 }


	 /**
	  *  根据项目id查询该项目下所有测试用例
	  *
	  * @param projectId
	  * @return
	  */
	 @AutoLog(value = "测试用例表-通过项目id查询")
	 @ApiOperation(value="测试用例表-通过项目id查询", notes="测试用例表-通过项目id查询")
	 @GetMapping(value = "/queryListByProjectId")
	 public Result<?> queryListByProjectId(@RequestParam(name="projectId",required=true) String projectId,
										   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
										   @RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize, HttpServletRequest req) {
		 Page<RunningCaseVO> pageList = new Page<RunningCaseVO>(pageNo, pageSize);
		 pageList = runningCaseService.getListByProjectId(pageList, projectId);
		 log.info("查询当前页：" + pageList.getCurrent());
		 log.info("查询当前页数量：" + pageList.getSize());
		 log.info("查询结果数量：" + pageList.getRecords().size());
		 log.info("数据总数：" + pageList.getTotal());

		 return Result.ok(pageList);
	 }

	 /**
	  * 测试用例表、测试项表-通过测试用例id查询
	  * @param caseId
	  * @param testTaskId
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "测试用例表、测试项表-通过测试用例id查询")
	 @ApiOperation(value = "测试用例表、测试项表-通过测试用例id查询",notes = "测试用例表、测试项表-通过测试用例id查询")
	 @GetMapping(value = "/insertDataByCaseId")
	 public Result<?> insertDataByCaseId(@RequestParam(name = "caseId",required = true) String caseId,
										 @RequestParam(name = "testTaskId",required = true) String testTaskId,
										 HttpServletRequest req){
		 List<RunningCase> runningCaseList = runningCaseService.getTestCopyDataById(caseId);

		 if(runningCaseList != null && runningCaseList.size() > 0){
		 	for(int i = 0; i < runningCaseList.size(); i++ ){
		 		RunningCase runningCase = new RunningCase();
		 		runningCase.setTestTaskId(testTaskId);
		 		runningCase.setTestName(runningCaseList.get(i).getTestName());
		 		runningCase.setTestCode(runningCaseList.get(i).getTestCode()+"(1)");
		 		runningCase.setTestRelationship(runningCaseList.get(i).getTestRelationship());
		 		runningCase.setTestType(runningCaseList.get(i).getTestType());
		 		runningCase.setTestInstructions(runningCaseList.get(i).getTestInstructions());
		 		runningCase.setTestConstraint(runningCaseList.get(i).getTestConstraint());
		 		runningCase.setTestOver(runningCaseList.get(i).getTestOver());
		 		runningCase.setTestProcess(runningCaseList.get(i).getTestProcess());
		 		runningCase.setTestCriterion(runningCaseList.get(i).getTestCriterion());
		 		runningCaseService.save(runningCase);
			}
		 }

		 if(runningCaseList != null){
		 	return Result.ok("用例复用失败!");
		 }

		 return Result.ok("用例复用成功！");
	 }

	 /**
	  * 根据测试用例id查询uutListId
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "被测对象属性表-通过id查询")
	 @ApiOperation(value="被测对象属性表-通过id查询", notes="被测对象属性表-通过id查询")
	 @GetMapping(value = "/queryUutListByCaseId")
	 public Result<?> queryUutListByCaseId(@RequestParam(name="id",required=true) String id) {
	 	String uutListId = runningCaseService.getUutListIdByCaseId(id);
	 	return runningUutRecordController.queryByIdFeign(uutListId);
	 }

	 /**
	  * 测试用例表、测试过程表-批量添加用例、测试步骤
	  * @param records
	  * @return
	  */
	 @AutoLog(value = "测试用例表、测试过程表-批量添加用例、测试步骤")
	 @ApiOperation(value = "测试用例表、测试过程表-批量添加用例、测试步骤",notes = "测试用例表、测试过程表-批量添加用例、测试步骤")
	 @PostMapping(value = "/insertRecommendCaseBatch")
	 public Result<?> insertRecommendCaseBatch(@RequestBody Map<String, Object> records,
										  HttpServletRequest req){
	 	LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

	 	String taskId = (String) records.get("taskId");
	 	List<LinkedHashMap<String, Object>> runningCaseVOList = (List<LinkedHashMap<String, Object>>) records.get("records");
	 	QueryWrapper queryWrapper = new QueryWrapper();
	 	queryWrapper.eq("id", taskId);
	 	RunningTask runningTask = runningTaskService.getOne(queryWrapper);

		 if(runningCaseVOList.size() > 0 && runningCaseVOList != null){
		 	for(int i = 0; i < runningCaseVOList.size(); i++) {
				// 向测试用例表插入数据
				RunningCase runningCase = new RunningCase();
				String testCode = this.getTestCode((String) runningCaseVOList.get(i).get("testCode"));
				runningCase.setTestTaskId(taskId);
				runningCase.setTestName((String) runningCaseVOList.get(i).get("testName"));
				runningCase.setTestCode(testCode);
				runningCase.setTestInstructions((String) runningCaseVOList.get(i).get("testInstructions"));
				runningCase.setTestInit((String) runningCaseVOList.get(i).get("testInit"));
				runningCase.setTestConstraint((String) runningCaseVOList.get(i).get("testConstraint"));
				runningCase.setTestOver((String) runningCaseVOList.get(i).get("testOver"));
				runningCase.setTestCriterion((String) runningCaseVOList.get(i).get("testCriterion"));
				runningCase.setTestVersion((String) runningCaseVOList.get(i).get("testVersion"));
				runningCase.setCreateBy(sysUser.getUsername());
				runningCase.setCreateTime(new Date());
				runningCase.setTurnId(runningTask.getTurnId());
				runningCase.setTurnVersion(runningTask.getTurnVersion());
				runningCase.setFileStatus(0);
				runningCaseService.save(runningCase);

				// 向测试用例历史表插入数据
				RunningCaseHistory runningCaseHistory = new RunningCaseHistory();
				runningCaseHistory.setTestTaskId(taskId);
				runningCaseHistory.setTestName((String) runningCaseVOList.get(i).get("testName"));
				runningCaseHistory.setTestCode(testCode);
				runningCaseHistory.setTestInstructions((String) runningCaseVOList.get(i).get("testInstructions"));
				runningCaseHistory.setTestInit((String) runningCaseVOList.get(i).get("testInit"));
				runningCaseHistory.setTestConstraint((String) runningCaseVOList.get(i).get("testConstraint"));
				runningCaseHistory.setTestOver((String) runningCaseVOList.get(i).get("testOver"));
				runningCaseHistory.setTestCriterion((String) runningCaseVOList.get(i).get("testCriterion"));
				runningCaseHistory.setTestVersion((String) runningCaseVOList.get(i).get("testVersion"));
				runningCaseHistory.setCreateBy(sysUser.getUsername());
				runningCaseHistory.setCreateTime(new Date());
				runningCaseHistory.setTurnId(runningTask.getTurnId());
				runningCaseHistory.setOperationType("0");
				runningCaseHistory.setMainId(runningCaseService.getidByCaseCode(testCode, taskId));
				runningCaseHistoryService.save(runningCaseHistory);

				List<LinkedHashMap<String, Object>> caseStepList = (List<LinkedHashMap<String, Object>>) runningCaseVOList.get(i).get("stepList");
				// 判断该用例下有无测试步骤
				if (caseStepList.size() > 0 && caseStepList != null) {
					for (int j = 0; j < caseStepList.size(); j++) {
						// 向测试步骤表插入数据
						RunningCaseStep runningCaseStep = new RunningCaseStep();
						runningCaseStep.setCaseId(runningCaseService.getidByCaseCode(testCode, taskId));
						runningCaseStep.setStepId((String) caseStepList.get(j).get("stepId"));
						runningCaseStep.setOperatingInstructions((String) caseStepList.get(j).get("operatingInstructions"));
						runningCaseStep.setExpectResult((String) caseStepList.get(j).get("expectResult"));
						runningCaseStep.setTurnId(runningTask.getTurnId());
						runningCaseStepService.save(runningCaseStep);

						// 向测试步骤历史表插入数据
						RunningCaseStepHistory runningCaseStepHistory = new RunningCaseStepHistory();
						runningCaseStepHistory.setCaseId(runningCaseService.getidByCaseCode(testCode, taskId));
						runningCaseStepHistory.setStepId((String) caseStepList.get(j).get("stepId"));
						runningCaseStepHistory.setOperatingInstructions((String) caseStepList.get(j).get("operatingInstructions"));
						runningCaseStepHistory.setExpectResult((String) caseStepList.get(j).get("expectResult"));
						runningCaseStepHistory.setTurnId(runningTask.getTurnId());
						runningCaseStepHistory.setOpType("0");
						runningCaseStepHistoryService.save(runningCaseStepHistory);
					}
				}
			}
		}else {
			return Result.ok("用例复用失败!");
		}

		 return Result.ok("用例添加成功！");
	 }

	 /**
	  * 测试用例表、测试过程表-添加用例、测试步骤
	  * @param records
	  * @return
	  */
	 @AutoLog(value = "测试用例表、测试过程表-添加用例、测试步骤")
	 @ApiOperation(value = "测试用例表、测试过程表-添加用例、测试步骤",notes = "测试用例表、测试过程表-添加用例、测试步骤")
	 @PostMapping(value = "/insertRecommendCase")
	 public Result<?> insertRecommendCase(@RequestBody Map<String, Object> records,
										  HttpServletRequest req){
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 String taskId = (String) records.get("taskId");
		 LinkedHashMap<String, Object> runningCaseVOList = (LinkedHashMap<String, Object>) records.get("records");
		 QueryWrapper queryWrapper = new QueryWrapper();
		 queryWrapper.eq("id", taskId);
		 RunningTask runningTask = runningTaskService.getOne(queryWrapper);

		 // 向测试用例表插入数据
		 RunningCase runningCase = new RunningCase();
		 String testCode = this.getTestCode((String) runningCaseVOList.get("testCode"));
		 runningCase.setTestTaskId(taskId);
		 runningCase.setTestName((String) runningCaseVOList.get("testName"));
		 runningCase.setTestCode(testCode);
		 runningCase.setTestInstructions((String) runningCaseVOList.get("testInstructions"));
		 runningCase.setTestInit((String) runningCaseVOList.get("testInit"));
		 runningCase.setTestConstraint((String) runningCaseVOList.get("testConstraint"));
		 runningCase.setTestOver((String) runningCaseVOList.get("testOver"));
		 runningCase.setTestCriterion((String) runningCaseVOList.get("testCriterion"));
		 runningCase.setTestVersion((String) runningCaseVOList.get("testVersion"));
		 runningCase.setCreateBy(sysUser.getUsername());
		 runningCase.setCreateTime(new Date());
		 runningCase.setTurnId(runningTask.getTurnId());
		 runningCase.setTurnVersion(runningTask.getTurnVersion());
		 runningCase.setFileStatus(0);
		 runningCaseService.save(runningCase);

		 // 向测试用例历史表插入数据
		 RunningCaseHistory runningCaseHistory = new RunningCaseHistory();
		 runningCaseHistory.setTestTaskId(taskId);
		 runningCaseHistory.setTestName((String) runningCaseVOList.get("testName"));
		 runningCaseHistory.setTestCode(testCode);
		 runningCaseHistory.setTestInstructions((String) runningCaseVOList.get("testInstructions"));
		 runningCaseHistory.setTestInit((String) runningCaseVOList.get("testInit"));
		 runningCaseHistory.setTestConstraint((String) runningCaseVOList.get("testConstraint"));
		 runningCaseHistory.setTestOver((String) runningCaseVOList.get("testOver"));
		 runningCaseHistory.setTestCriterion((String) runningCaseVOList.get("testCriterion"));
		 runningCaseHistory.setTestVersion((String) runningCaseVOList.get("testVersion"));
		 runningCaseHistory.setCreateBy(sysUser.getUsername());
		 runningCaseHistory.setCreateTime(new Date());
		 runningCaseHistory.setTurnId(runningTask.getTurnId());
		 runningCaseHistory.setOperationType("0");
		 runningCaseHistory.setMainId(runningCaseService.getidByCaseCode(testCode, taskId));
		 runningCaseHistoryService.save(runningCaseHistory);

		 List<LinkedHashMap<String, Object>> caseStepList = (List<LinkedHashMap<String, Object>>) runningCaseVOList.get("stepList");
		 // 判断该用例下有无测试步骤
		 if (caseStepList.size() > 0 && caseStepList != null) {
			 for (int j = 0; j < caseStepList.size(); j++) {
				 // 向测试步骤表插入数据
				 RunningCaseStep runningCaseStep = new RunningCaseStep();
				 runningCaseStep.setCaseId(runningCaseService.getidByCaseCode(testCode, taskId));
				 runningCaseStep.setStepId((String) caseStepList.get(j).get("stepId"));
				 runningCaseStep.setOperatingInstructions((String) caseStepList.get(j).get("operatingInstructions"));
				 runningCaseStep.setExpectResult((String) caseStepList.get(j).get("expectResult"));
				 runningCaseStep.setTurnId(runningTask.getTurnId());
				 runningCaseStepService.save(runningCaseStep);

				 // 向测试步骤历史表插入数据
				 RunningCaseStepHistory runningCaseStepHistory = new RunningCaseStepHistory();
				 runningCaseStepHistory.setCaseId(runningCaseService.getidByCaseCode(testCode, taskId));
				 runningCaseStepHistory.setStepId((String) caseStepList.get(j).get("stepId"));
				 runningCaseStepHistory.setOperatingInstructions((String) caseStepList.get(j).get("operatingInstructions"));
				 runningCaseStepHistory.setExpectResult((String) caseStepList.get(j).get("expectResult"));
				 runningCaseStepHistory.setTurnId(runningTask.getTurnId());
				 runningCaseStepHistory.setOpType("0");
				 runningCaseStepHistoryService.save(runningCaseStepHistory);
			 }
		 }
		 return Result.ok("用例添加成功！");
	 }

	 public String getTestCode(String testCode){

	 	String targetTestcode = testCode;
		Integer num = runningCaseService.getNumOfTestCode(targetTestcode);

		if(num >= 1){
			targetTestcode+="(1)";
			return getTestCode(targetTestcode);
		}else {
			return targetTestcode;
		}

	 };

	 /**
	  * 根据测试用例id查询被测对象版本号
	  * @param caseId
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "查询被测对象版本号")
	 @ApiOperation(value="查询被测对象版本号", notes="查询被测对象版本号")
	 @GetMapping(value = "/queryTurnVersionId")
	 public Result<?> queryTurnVersionId(@RequestParam(name = "caseId",required = false) String caseId,
										 HttpServletRequest req) {

		 RunningCase runningCase = runningCaseService.getById(caseId);
		 String version = runningCase.getTestVersion();
		 return Result.ok(version);
	 }

 }
