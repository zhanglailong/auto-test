package org.jeecg.modules.task.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.modules.task.entity.AuToRunningTask;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.entity.RunningTaskHistory;
import org.jeecg.modules.task.entity.RunningTaskReport;
import org.jeecg.modules.task.service.*;
import org.jeecg.modules.task.vo.RunningTaskVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description: 自动化任务管理
 * @Author: jeecg-boot
 * @Date: 2021-11-17
 * @Version: V1.0
 */
@Api(tags = "自动化任务管理")
@RestController
@RequestMapping("/task/autoRunningTask")
@Slf4j
public class AutoRunningTaskController extends JeecgController<AuToRunningTask, IAuToRunningTaskService> {
    @Autowired
    private IAuToRunningTaskService auToRunningTaskService;
    @Autowired
    private IAuToRunningTaskHistoryService auToRunningTaskHistoryService;

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
        pageList = auToRunningTaskService.queryPageList(pageList, projectId, taskName, priority, taskCode, taskSoftName);
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
        pageList = auToRunningTaskService.queryFileTaskPageList(pageList, projectId, taskName, priority, taskCode, taskSoftName);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }


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
        AuToRunningTask auToRunningTask = new AuToRunningTask();
        BeanUtils.copyProperties(runningTask,auToRunningTask);
        auToRunningTask.setType(CommonConstant.STATUS_1);
        auToRunningTaskService.save(auToRunningTask);

        // 添加历史记录
        insertHistory(auToRunningTask,"0",auToRunningTask.getId());

        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param auToRunningTask
     * @return
     */
    @AutoLog(value = "任务管理-编辑")
    @ApiOperation(value = "任务管理-编辑", notes = "任务管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AuToRunningTask auToRunningTask) {
        // 获取当前用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        auToRunningTaskService.updateById(auToRunningTask);
        // 添加历史记录
        if(auToRunningTask.getIsModified()){
            insertHistory(auToRunningTask,"1",auToRunningTask.getId());
        }
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
        AuToRunningTask auToRunningTask = auToRunningTaskService.getById(id);
        // 逻辑删除任务信息，任务下的所有测试用例信息、问题信息
        auToRunningTaskService.updateTask(id);
        // 添加历史记录
        insertHistory(auToRunningTask,"2",auToRunningTask.getId());
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
        List<AuToRunningTask> runningTasks=auToRunningTaskService.listByIds(Arrays.asList(ids.split(",")));
        for (AuToRunningTask record: runningTasks){
            record.setDelFlag(1);
            record.setUpdateTime(new Date());
            insertHistory(record,"2",record.getId());
        }
        auToRunningTaskService.updateBatchById(runningTasks);
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
        AuToRunningTask runningTask = auToRunningTaskService.getById(id);
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
        String sjwjurl = auToRunningTaskService.getCjUrlByTaskId(id);
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
        List<String> uutListIds = auToRunningTaskService.getUutListId(projectId);
        List<String> userIds = new ArrayList<>();
        if(uutListIds != null){
            for(String uutListId : uutListIds){
                //多数据源,根据uut_list_id查询user_id
                List<String> userId = auToRunningTaskService.getUserId(uutListId);
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
                String realName = auToRunningTaskService.getUserRealName(userId);
                if(!realName.isEmpty()){
                    Map<String, Object> map = new HashMap<>(2000);
                    map.put("label", realName);
                    map.put("value", userId);
                    resultList.add(map);
                }
            }
        }

        return Result.ok(resultList);
    }

    @GetMapping("/getOne")
    public Result<?> getOne(String id){
        return Result.ok(auToRunningTaskService.getById(id));
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

        List<AuToRunningTask> list = auToRunningTaskService.getTaskInfoByProjectId(projectId);
        return Result.OK(list);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param runningTask
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AuToRunningTask runningTask) {
        return super.exportXls(request, runningTask, AuToRunningTask.class, "任务管理");
    }


    /**
     * 添加历史操作记录

     */
    public void insertHistory(AuToRunningTask originData, String operationType, String mainId)
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
            auToRunningTaskHistoryService.save(runningTaskHistory);
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
            auToRunningTaskHistoryService.save(newEdit);
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
        String turnNum = auToRunningTaskService.getTurnNum(turnId);
        return turnNum;
    }
}
