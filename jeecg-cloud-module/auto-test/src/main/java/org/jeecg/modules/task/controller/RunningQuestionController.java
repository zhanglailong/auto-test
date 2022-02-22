package org.jeecg.modules.task.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningQuestion;
import org.jeecg.modules.task.entity.RunningQuestionHistory;
import org.jeecg.modules.task.service.IRunningCaseService;
import org.jeecg.modules.task.service.IRunningQuestionHistoryService;
import org.jeecg.modules.task.service.IRunningQuestionService;
import org.jeecg.modules.task.vo.RunningQuestionVO;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 问题单
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
@Api(tags="问题单")
@RestController
@RequestMapping("/task/runningQuestion")
@Slf4j
public class RunningQuestionController extends JeecgController<RunningQuestion, IRunningQuestionService> {
	@Autowired
	private IRunningQuestionService runningQuestionService;
	@Autowired
	private IRunningQuestionHistoryService historyService;
	@Autowired
	private IRunningCaseService runningCaseService;
	/**
	 * 分页列表查询
	 *
	 * @param
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试用例问题单-分页列表查询")
	@ApiOperation(value="测试用例问题单-分页列表查询", notes="测试用例问题单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name="caseId", required = false) String caseId,
			   @RequestParam(name="questionCode", required = false) String questionCode,
			   @RequestParam(name="questionType", required = false) String questionType,
			   @RequestParam(name="questionLevel", required = false) String questionLevel,
			   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			   HttpServletRequest req) {
		Page<RunningQuestionVO> pageList = new Page<>(pageNo, pageSize);
		pageList = runningQuestionService.queryPageList(pageList,caseId,questionCode,questionType,questionLevel);
		// 当前用户是否是创建者
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		String username = sysUser.getUsername();
		List<RunningQuestionVO> runningQuestionList = pageList.getRecords();
		for (RunningQuestionVO runningQuestionVO : runningQuestionList) {
			if (username != null && !username.isEmpty() && username.equals(runningQuestionVO.getCreateBy())) {
				runningQuestionVO.setCreaterFalg(true);
			} else if ("admin".equals(username)) {
				runningQuestionVO.setCreaterFalg(true);
			} else {
				runningQuestionVO.setCreaterFalg(false);
			}
		}
		log.info("查询当前页："+pageList.getCurrent());
		log.info("查询当前页数量："+pageList.getSize());
		log.info("查询结果数量："+pageList.getRecords().size());
		log.info("数据总数："+pageList.getTotal());		
		
		return Result.ok(pageList);
	}

	 /**
	  * 分页列表查询已归档问题单
	  *
	  * @param
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "测试用例问题单-分页列表查询")
	 @ApiOperation(value="测试用例问题单-分页列表查询", notes="测试用例问题单-分页列表查询")
	 @GetMapping(value = "/fileQuestionlist")
	 public Result<?> queryFileQuestionPageList(@RequestParam(name="caseId", required = false) String caseId,
									@RequestParam(name="questionCode", required = false) String questionCode,
									@RequestParam(name="questionType", required = false) String questionType,
									@RequestParam(name="questionLevel", required = false) String questionLevel,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 Page<RunningQuestionVO> pageList = new Page<>(pageNo, pageSize);
		 pageList = runningQuestionService.queryFileQuestionPageList(pageList,caseId,questionCode,questionType,questionLevel);
		 // 当前用户是否是创建者
		 LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		 String username = sysUser.getUsername();
		 List<RunningQuestionVO> runningQuestionList = pageList.getRecords();
		 for (RunningQuestionVO runningQuestionVO : runningQuestionList) {
			 if (username != null && !username.isEmpty() && username.equals(runningQuestionVO.getCreateBy())) {
				 runningQuestionVO.setCreaterFalg(true);
			 } else if ("admin".equals(username)) {
				 runningQuestionVO.setCreaterFalg(true);
			 } else {
				 runningQuestionVO.setCreaterFalg(false);
			 }
		 }
		 log.info("查询当前页："+pageList.getCurrent());
		 log.info("查询当前页数量："+pageList.getSize());
		 log.info("查询结果数量："+pageList.getRecords().size());
		 log.info("数据总数："+pageList.getTotal());

		 return Result.ok(pageList);
	 }
	
	/**
	 * 通过测试用例ID查询项目ID
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "通过测试用例ID查询项目ID")
	@ApiOperation(value="通过测试用例ID查询项目ID", notes="通过测试用例ID查询项目ID")
	@GetMapping(value = "/getProjectIdBycaseId")
	public Result<?> getProjectIdByTaskId(@RequestParam(name="caseId",required=true) String caseId) {
	
		String projectId = runningQuestionService.getProjectIdByCaseId(caseId);
		
		if(projectId==null) {
			return Result.error("未找到对应数据");
		}
	
		return Result.ok(projectId);
	}
	
	
	
	
	/**
	 *   添加
	 *
	 * @param runningQuestion
	 * @return
	 */
	@AutoLog(value = "问题单-添加")
	@ApiOperation(value="问题单-添加", notes="问题单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningQuestion runningQuestion) {
		String caseId= runningQuestion.getCaseId();
		String caseStepId = runningQuestion.getCaseStepId();
		if(caseId.isEmpty()) {
			return Result.error("添加失败！");
		}
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		runningQuestion.setCreateBy(sysUser.getUsername());
		runningQuestion.setCreateTime(new Date());
		runningQuestion.setDelFlag(0);
		runningQuestion.setFileStatus(0);
		runningQuestion.setCaseId(caseId);
		runningQuestion.setCaseStepId(caseStepId);
		runningQuestion.setUpdateBy(sysUser.getUsername());
		RunningCase runningCase= runningCaseService.getById(caseId);
		runningQuestion.setQuestionVersion(runningCase.getTestVersion());
		runningQuestionService.save(runningQuestion);
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
	@AutoLog(value = "问题单-编辑")
	@ApiOperation(value="问题单-编辑", notes="问题单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningQuestion runningQuestion) {
		String caseId= runningQuestion.getCaseId();
		if(caseId.isEmpty()) {
			return Result.error("编辑失败！");
		}
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		runningQuestionService.updateById(runningQuestion);
		runningQuestion.setCaseId(caseId);
		runningQuestion.setUpdateBy(sysUser.getUsername());
		// dq add 往操作历史表中插入记录
		insertHistory(runningQuestion,1,runningQuestion.getId());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id逻辑删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "问题单-通过id逻辑删除")
	@ApiOperation(value="问题单-通过id逻辑删除", notes="问题单-通过id逻辑删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		RunningQuestion question = runningQuestionService.getById(id);
		question.setId(id);
		question.setDelFlag(1);
		runningQuestionService.updateById(question);
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
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "问题单-通过id查询")
	@ApiOperation(value="问题单-通过id查询", notes="问题单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningQuestion runningQuestion = runningQuestionService.getById(id);
		if(runningQuestion==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningQuestion);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningQuestion
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningQuestion runningQuestion) {

    	LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
    	
    	//获取导出数据
    	List<RunningQuestionVO> pageList = new ArrayList<>();
    	String caseId =request.getParameter("caseId");
    	pageList = runningQuestionService.getRunningQuestionData(caseId);
    	List<RunningQuestionVO> exportList = null;

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
        mv.addObject(NormalExcelConstants.FILE_NAME, "问题单列表");
        mv.addObject(NormalExcelConstants.CLASS, RunningQuestionVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("问题单数据", "导出人:"+sysUser.getRealname(), "问题单数据"));
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
				 	RunningQuestion runningQuestion=runningQuestionService.getById(mainId);
				 	//RunningQuestionHistory runningQuestionHistory1=historyService.getById(mainId);
				 	RunningQuestionHistory newEdit =new RunningQuestionHistory();
				 // 备份最新数据
					 BeanUtils.copyProperties(originData,newEdit);
					 newEdit.setId(null);
					 newEdit.setCaseId(runningQuestion.getCaseId());
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

	 /**
	  * 根据caseId查询问题单记录
	  * @param caseId
	  * @return
	  */
	 @AutoLog(value = "问题单记录-通过caseId查询")
	 @ApiOperation(value = "问题单记录-通过caseId查询", notes = "问题单记录-通过caseId查询")
	 @GetMapping(value = "/queryByCaseId")
	 public Result<?> queryByCaseId(@RequestParam(name="caseId",required=true) String caseId){
	 	List<RunningQuestionVO> runningQuestionVOList = runningQuestionService.queryByCaseId(caseId);
	 	return Result.ok(runningQuestionVOList);
	 }



}
