package org.jeecg.modules.task.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.project.service.ProjectUserAssociationService;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.entity.RunningQuestionHistory;
import org.jeecg.modules.task.service.IRunningCaseService;
import org.jeecg.modules.task.service.IRunningQuestionHistoryService;
import org.jeecg.modules.task.service.IRunningQuestionManageService;
import org.jeecg.modules.task.service.IRunningQuestionService;
import org.jeecg.modules.task.vo.RunningQuestionManageVO;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 问题单管理
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
@Api(tags="问题单管理")
@RestController
@RequestMapping("/task/runningQuestionManage")
@Slf4j
public class RunningQuestionManageController{
	@Autowired
	private IRunningQuestionManageService runningQuestionManageService;
	@Autowired
	private IRunningCaseService runningCaseService;
	@Autowired
	private IRunningQuestionHistoryService historyService;
	@Autowired
	private IRunningQuestionService runningQuestionService;
	@Autowired
	private ProjectUserAssociationService projectUserAssociationService;
	@Autowired
	private ISysUserService  sysUserService;
	/**
	 * 分页列表查询
	 *
	 * @param
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "问题单管理-分页列表查询")
	@ApiOperation(value="问题单管理-分页列表查询", notes="问题单管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(
			//RunningQuestionHistory runningQuestionHistory,
			   @RequestParam(name="projectId", required = false) String projectId,
			   @RequestParam(name="questionCode", required = false) String questionCode,
			   @RequestParam(name="questionType", required = false) String questionType,
			   @RequestParam(name="questionLevel", required = false) String questionLevel,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<RunningQuestionManageVO> pageList = new Page<>(pageNo,pageSize);
		pageList = runningQuestionManageService.queryPageList(pageList,projectId,questionCode,questionType,questionLevel);
		log.info("查询当前页："+pageList.getCurrent());
		log.info("查询当前页数量："+pageList.getSize());
		log.info("查询结果数量："+pageList.getRecords().size());
		log.info("数据总数："+pageList.getTotal());
		return Result.ok(pageList);
	}



	
	
	/**
	 *   添加
	 *
	 * @param runningQuestion
	 * @return
	 */
	@AutoLog(value = "问题单管理-添加")
	@ApiOperation(value="问题单管理-添加", notes="问题单管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningQuestion runningQuestion) {
		String caseId= runningQuestion.getCaseName();
		if(caseId.isEmpty()) {
			return Result.error("添加失败！");
		}
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		runningQuestion.setCreateBy(sysUser.getUsername());
		runningQuestion.setCreateTime(new Date());
		runningQuestion.setDelFlag(0);
		runningQuestion.setCaseId(caseId);
		runningQuestionManageService.save(runningQuestion);
		// dq add 往操作历史表中插入记录
		insertHistory(runningQuestion,0,runningQuestion.getId());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningQuestion
	 * @return
	 */
	@AutoLog(value = "问题单管理-编辑")
	@ApiOperation(value="问题单管理-编辑", notes="问题单管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningQuestion runningQuestion) {
		String caseId= runningQuestion.getCaseName();
		if(caseId.isEmpty()) {
			return Result.error("编辑失败！");
		}
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		runningQuestion.setCaseId(caseId);
		runningQuestionManageService.updateById(runningQuestion);
		// dq add 往操作历史表中插入记录
		insertHistory(runningQuestion,1,runningQuestion.getId());
		return Result.ok("编辑成功！");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "问题单-通过id删除")
	@ApiOperation(value="问题单-通过id删除", notes="问题单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		RunningQuestion question = runningQuestionService.getById(id);
		question.setId(id);
		question.setDelFlag(1);
		runningQuestionManageService.updateById(question);
		// dq add 往操作历史表中插入记录
		insertHistory(question,2,question.getId());
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "问题单-批量删除")
	@ApiOperation(value="问题单-批量删除", notes="问题单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		//上面代码是物理删除,以下改为逻辑删除
		List<RunningQuestion> runningQuestionList = runningQuestionService.listByIds(Arrays.asList(ids.split(",")));
		for(RunningQuestion record : runningQuestionList)
		{
			record.setDelFlag(1);
			record.setUpdateTime(new Date());
			// dq add 增加操作记录
			insertHistory(record,2,record.getId());
		}
		runningQuestionService.updateBatchById(runningQuestionList);
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 
	 * @param id
	 * @return
	 */
	@AutoLog(value = "问题单-通过id查询")
	@ApiOperation(value="问题单-通过id查询", notes="问题单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {		
		RunningQuestion runningQuestion = runningQuestionManageService.getQuestionManageDataById(id);
		if(runningQuestion==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningQuestion);
	}
	
	
	/**
	 * 通过任务id查询测试用例名称
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "通过任务id查询测试用例id、名称")
	@ApiOperation(value="通过任务id查询测试用例id、名称", notes="通过任务id查询测试用例id、名称")
	@GetMapping(value = "/queryCaseNameByTaskId")
	public Result<?> queryCaseNameByTaskId(@RequestParam(name="taskId",required=true) String taskId) {
		//定义最终存放数据集合
		List<Map<String,Object>> resultList = new ArrayList<>();
		//根据任务id查询用例ID，和用例名称
		List<RunningCase> caseList = runningCaseService.getCaseNameByTaskId(taskId);
		if(caseList==null) {
			return Result.error("未找到对应数据");
		}
		for(int i=0;i<caseList.size();i++) {
			String value=caseList.get(i).getId();
			String label=caseList.get(i).getTestName();
			Map<String,Object> map = new HashMap<>(2000);
			map.put("label", label);
			map.put("value", value);
			resultList.add(map);
		}
		return Result.ok(resultList);
	}
	
	/**
	 * 根据测试用例ID查询报告人信息
	 *
	 * @param caseId
	 * @return projectName
	 */
	@AutoLog(value = "根据测试用例ID查询报告人信息")
	@ApiOperation(value="根据测试用例ID查询报告人信息", notes="根据测试用例ID查询报告人信息")
	@GetMapping(value = "/getReporterByCaseId")
	public Result<?> getReporterByCaseId(@RequestParam(name="caseId",required=true) String caseId) {
		//根据测试用例ID查询项目ID
		String projectId = runningQuestionManageService.getProjectIdByCaseId(caseId);
		if(projectId.isEmpty()) {
			return Result.error("未找到对应数据");
		}
		//定义最终存放数据集合
		List<Map<String,Object>> resultList = new ArrayList<>();
		//根据项目ID查询项目成员ID
		List<String> userIdList =  projectUserAssociationService.getUserIdsByProjectId(projectId);
		if(userIdList.isEmpty()) {
			return Result.error("未找到对应数据");
		}
		if(userIdList!=null) {
			for(String userId:userIdList) {
				//根据项目成员ID查询项目成员名称
				String realName = sysUserService.getRealNameById(userId);
				if(!realName.isEmpty()) {
					Map<String,Object> map = new HashMap<>(2000);
					map.put("label", realName);
					map.put("value", userId);
					resultList.add(map);
				}
			}
		}
		return Result.ok(resultList);
	}
	
	

    /**
    * 导出excel
    *
    * @param request
    * @param
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningQuestionManageVO runningQuestionManageVO) {
    	
    	LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
    	
    	//获取导出数据
    	List<RunningQuestionManageVO> pageList = new ArrayList<>();
    	pageList = runningQuestionManageService.getRunningQuestionManageData();
    	List<RunningQuestionManageVO> exportList = null;

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
        mv.addObject(NormalExcelConstants.CLASS, RunningQuestionManageVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("问题单管理表数据", "导出人:"+sysUser.getRealname(), "问题单管理表数据"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

	 /** dq add
	  * 向操作历史表中插入操作记录
	  * 参数:
	  * 	RunningQuestion originData: 新增和编辑操作时，会把最新数据存起来
	  * 	Integer opTye: 操作类型,0:新增  1:编辑 2:删除
	  * 	String mainId: 主表id
	  */
	 public void insertHistory(RunningQuestion originData, Integer opType, String mainId)
	 {
		 LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		 RunningQuestionHistory runningQuestionHistory = new RunningQuestionHistory();
		 int sign2=2;
		 if(opType == 0)
		 {
			 // 新增时把源数据在历史表中备份一个
			 BeanUtils.copyProperties(originData,runningQuestionHistory);
			 runningQuestionHistory.setSort(getMaxSortByMainId(mainId));
		 }
		 if(opType == sign2)
		 {
			 // 删除
			 BeanUtils.copyProperties(originData,runningQuestionHistory);
			 runningQuestionHistory.setSort(getMaxSortByMainId(mainId));
			 runningQuestionHistory.setDelFlag(1);
		 }
		 if(opType == 0 || opType == sign2)
		 {
			 runningQuestionHistory.setId(null);
			 runningQuestionHistory.setUpdateBy(sysUser.getUsername());
			 runningQuestionHistory.setOpType(opType);
			 runningQuestionHistory.setUpdateTime(new Date());
			 runningQuestionHistory.setMainId(mainId);
			 historyService.save(runningQuestionHistory);
		 }
		 if(opType == 1)
		 {
			 // 编辑操作单独处理,改过东西才会保存
			 List<RunningQuestionHistory> historyList = originData.getModifiedList();
			 if(historyList != null && historyList.size() > 0)
			 {
				 // 当前插入的sort
				 Long insertSort = getMaxSortByMainId(mainId);
//				 for(RunningQuestionHistory record : historyList)
//				 {
				 RunningQuestion runningQuestion=runningQuestionManageService.getById(mainId);
				 RunningQuestionHistory newEdit =new RunningQuestionHistory();
				 // 备份最新数据
				 BeanUtils.copyProperties(originData,newEdit);
				 newEdit.setId(null);
				 newEdit.setMainId(mainId);
				 newEdit.setCreateBy(runningQuestion.getCreateBy());
				 newEdit.setUpdateBy(sysUser.getUsername());
				 newEdit.setUpdateTime(new Date());
				 newEdit.setSort(insertSort);
				 newEdit.setOpType(1);

				 historyService.save(newEdit);
			 }
		 }
	 }

	 /** dq add
	  * 返回本次插入历史表中的sort值,同次操作sort值相同
	  */
	 public Long getMaxSortByMainId(String mainId)
	 {
		 QueryWrapper<RunningQuestionHistory> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("main_id",mainId);
		 queryWrapper.orderByDesc("create_time");
		 List<RunningQuestionHistory> list = historyService.list(queryWrapper);
		 if(list == null || list.size() == 0)
		 {
			 return 0L;
		 }
		 else
		 {
			 Long currentSort = list.get(0).getSort();
			 return currentSort + 1;
		 }
	 }

	 /** dq add
	  * 用例问题单操作历史记录查询
	  *
	  */
	 @AutoLog(value = "用例问题单操作历史查询")
	 @ApiOperation(value="用例问题单操作历史查询", notes="用例问题单操作历史查询")
	 @GetMapping(value = "/historyList")
	 public Result<?> queryPageList(RunningQuestionHistory runningQuestionHistory,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 Page page = new Page(pageNo, pageSize);
		 return Result.ok(runningQuestionService.getOperationHistoryList(page,runningQuestionHistory));
	 }

	 /** dq add
	  * 通过id查询用例问题单详情
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "问题单操作记录-通过id查询")
	 @ApiOperation(value="问题单操作记录-通过id查询", notes="问题单操作记录-通过id查询")
	 @GetMapping(value = "/queryHistoryById")
	 public Result<?> queryHistoryById(@RequestParam(name="id",required=true) String id) {
		 RunningQuestionHistory runningQuestionHistory = historyService.getById(id);
		 if(runningQuestionHistory==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(runningQuestionHistory);
	 }

}
