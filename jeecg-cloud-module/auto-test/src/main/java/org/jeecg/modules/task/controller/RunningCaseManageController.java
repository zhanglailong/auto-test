package org.jeecg.modules.task.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.project.service.ProjectUserAssociationService;
import org.jeecg.modules.task.model.CaseTreeIdModel;
import org.jeecg.modules.task.service.IRunningCaseHistoryService;
import org.jeecg.modules.task.service.IRunningCaseManageService;
import org.jeecg.modules.task.service.IRunningTaskService;
import org.jeecg.modules.task.vo.RunningCaseManageVO;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningCaseHistory;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 测试用例管理
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Api(tags="测试用例管理")
@RestController
@RequestMapping("/task/runningCaseManage")
@Slf4j
public class RunningCaseManageController extends JeecgController<RunningCase, IRunningCaseManageService> {
	@Autowired
	private IRunningCaseManageService runningCaseManageService;

	@Autowired
	private IRunningCaseHistoryService runningCaseHistoryService;
	
	@Autowired
	private ProjectUserAssociationService projectUserAssociationService;
	
	@Autowired
	private ISysUserService  sysUserService;

	@Autowired
	private IRunningTaskService runningTaskService;
	
	/**
	 * 分页列表查询
	 *
	 * @param
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试用例表-分页列表查询")
	@ApiOperation(value="测试用例表-分页列表查询", notes="测试用例表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name="testTaskId", required = false) String testTaskId,
			   @RequestParam(name="projectId", required = false) String projectId,
			   @RequestParam(name="testName", required = false) String testName,
			   @RequestParam(name="testCode", required = false) String testCode,
			   @RequestParam(name="testPerson", required = false) String testPerson,
			   @RequestParam(name="testDate", required = false) String testDate,
			   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			   HttpServletRequest req) {
		Page<RunningCaseManageVO> pageList = new Page<RunningCaseManageVO>(pageNo, pageSize);
		pageList = runningCaseManageService.queryPageList(pageList,testTaskId,projectId,testName,testCode,testPerson,testDate);
		log.info("查询当前页："+pageList.getCurrent());
		log.info("查询当前页数量："+pageList.getSize());
		log.info("查询结果数量："+pageList.getRecords().size());
		log.info("数据总数："+pageList.getTotal());		
		return Result.ok(pageList);
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
		runningCaseManageService.save(runningCase);
		String copy="copy";
		if(copy.equals(runningCase.getOpFlag())){
			//复制添加历史记录
			insertHistory(runningCase,"3",runningCase.getId());
		}else {
			//新增添加历史记录
			insertHistory(runningCase,"0",runningCase.getId());
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
		runningCaseManageService.updateById(runningCase);
		//修改添加历史记录
		if (runningCase.getIsModified()){
		insertHistory(runningCase,"1",runningCase.getId());
		}
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id逻辑删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试用例表-通过id逻辑删除")
	@ApiOperation(value="测试用例表-通过id逻辑删除", notes="测试用例表-通过id逻辑删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		RunningCase runningCase=runningCaseManageService.getById(id);
		runningCase.setId(id);
		runningCase.setDelFlag(1);
		runningCaseManageService.updateById(runningCase);
		insertHistory(runningCase,"2",runningCase.getId());
		//删除时添加历史记录
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
		List<RunningCase> runningCaseList=runningCaseManageService.listByIds(Arrays.asList(ids.split(",")));
		for (RunningCase runningCase:runningCaseList){
			runningCase.setDelFlag(1);
			runningCase.setUpdateTime(new Date());
			insertHistory(runningCase,"2",runningCase.getId());
		}
		runningCaseManageService.updateBatchById(runningCaseList);
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
		RunningCase runningCase = runningCaseManageService.getById(id);
		if(runningCase==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningCase);
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
		List<RunningCase> caseList  = runningCaseManageService.getCopyDataById(id);
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
		String templateId=runningCaseManageService.getTestTemplateById(id);
		if(!templateId.isEmpty()) {
			//查询树形所需数据	
			list  = runningCaseManageService.queryTreeList(templateId);
			result.setResult(list);
		    result.setSuccess(true);
		}
		if(list==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(result);
	}
	
	/**
	 * 根据任务ID查询项目ID、项目名称
	 *
	 * @param taskId
	 * @return projectName
	 */
	@AutoLog(value = "根据任务ID查询项目ID、项目名称")
	@ApiOperation(value="根据任务ID查询项目ID、项目名称", notes="根据任务ID查询项目ID、项目名称")
	@GetMapping(value = "/queryProjectNameByTaskId")
	public Result<?> getProjectNameByTaskId(@RequestParam(name="taskId",required=true) String taskId) {
		RunningProject runningProject = runningCaseManageService.getProjectNameByTaskId(taskId);
		if(runningProject==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningProject);
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
		RunningTask projectIds = runningCaseManageService.getProjectIdByTaskId(taskId);
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
    * @param
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningCaseManageVO runningCaseManageVO) {
	
    	LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
    	//获取导出数据
    	List<RunningCaseManageVO> pageList = new ArrayList<>();
    	pageList = runningCaseManageService.getRunningCaseManageData1();
    	List<RunningCaseManageVO> exportList = null;

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
        mv.addObject(NormalExcelConstants.FILE_NAME, "问题单管理列表");
        mv.addObject(NormalExcelConstants.CLASS, RunningCaseManageVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("测试用例管理数据", "导出人:"+sysUser.getRealname(), "测试用例管理数据"));
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
	  * 插入历史记录
	  */
	 public void insertHistory(RunningCase originData, String opType, String mainId)
	 {
		 LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		 RunningCaseHistory runningCaseHistory=new RunningCaseHistory();
		 String sign0="0";
		 String sign1="1";
		 String sign2="2";
		 String sign3="3";
		 if(sign0.equals(opType) || sign3.equals(opType))
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
		 if(sign0.equals(opType) || sign2.equals(opType) || sign3.equals(opType))
		 {
		 	 runningCaseHistory.setId(null);
			 runningCaseHistory.setTestTaskId(originData.getTestTaskId());
			 runningCaseHistory.setUpdateBy(sysUser.getId());
			 runningCaseHistory.setOperationType(opType);
			 runningCaseHistory.setUpdateTime(new Date());
			 runningCaseHistory.setMainId(mainId);
			 runningCaseHistoryService.save(runningCaseHistory);
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
				 runningCaseHistoryService.save(newEdit);
//			 }
		 }
	 }

 }
