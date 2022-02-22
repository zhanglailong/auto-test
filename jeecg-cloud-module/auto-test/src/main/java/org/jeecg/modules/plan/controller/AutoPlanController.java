package org.jeecg.modules.plan.controller;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.IscTools;
import org.jeecg.modules.plan.entity.AutoPlan;
import org.jeecg.modules.plan.service.IAutoPlanService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 方案管理
 * @Author: zll
 * @Date: 2021-08-17
 * @Version: V1.0
 */
@Api(tags = "方案管理")
@RestController
@RequestMapping("/plan/autoPlan")
@Slf4j
public class AutoPlanController extends JeecgController<AutoPlan, IAutoPlanService> {
    @Autowired
    private IAutoPlanService autoPlanService;
    @Autowired
    private IAutoScriptService autoScriptService;

    /**
     * 分页列表查询
     *
     * @param autoPlan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "方案管理-分页列表查询")
    @ApiOperation(value = "方案管理-分页列表查询", notes = "方案管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AutoPlan autoPlan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<AutoPlan> queryWrapper = QueryGenerator.initQueryWrapper(autoPlan, req.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        if(StringUtils.isNotBlank(autoPlan.getProjectId())){
            queryWrapper.eq(CommonConstant.DATA_STRING_PROJECT_ID,autoPlan.getProjectId());
        }
        Page<AutoPlan> page = new Page<AutoPlan>(pageNo, pageSize);
        IPage<AutoPlan> pageList = autoPlanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 添加
     *
     * @param autoPlan
     * @return
     */
    @AutoLog(value = "方案管理-添加")
    @ApiOperation(value = "方案管理-添加", notes = "方案管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AutoPlan autoPlan) {
        try {
            if (autoPlan == null) {
                return Result.error("对象不能为空");
            }
            autoPlan.setIdel(CommonConstant.DATA_INT_IDEL_0);
            autoPlanService.save(autoPlan);
            return Result.OK("添加成功！");
        } catch (Exception e) {
            log.error("添加失败" + e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 编辑
     *
     * @param autoPlan
     * @return
     */
    @AutoLog(value = "方案管理-编辑")
    @ApiOperation(value = "方案管理-编辑", notes = "方案管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AutoPlan autoPlan) {
        try {
            if (StringUtils.isEmpty(autoPlan.getId())) {
                return Result.OK("缺少参数");
            }
            autoPlanService.edit(autoPlan);
            return Result.OK("修改成功!");
        } catch (Exception e) {
            return Result.error("修改失败异常:" + e.getMessage());
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "方案管理-通过id删除")
    @ApiOperation(value = "方案管理-通过id删除", notes = "方案管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                return Result.error("缺少参数");
            }
            autoPlanService.delete(id);
            return Result.OK("删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "方案管理-批量删除")
    @ApiOperation(value = "方案管理-批量删除", notes = "方案管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.autoPlanService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "方案管理-通过id查询")
    @ApiOperation(value = "方案管理-通过id查询", notes = "方案管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AutoPlan autoPlan = autoPlanService.getById(id);
        if (autoPlan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(autoPlan);
    }

    /**
     * 绑定脚本
     *
     * @param planId    方案id
     * @param scriptIds 绑定的脚本ids
     * @return
     */
//    @AutoLog(value = "方案中绑定脚本")
//    @ApiOperation(value = "方案中绑定脚本", notes = "方案中绑定脚本")
//    @GetMapping(value = "/bindingScript")
//    public Result<?> bindingScript(
//            @RequestParam(name = "planId", required = true) String planId,
//            @RequestParam(name = "scriptIds", required = true) String scriptIds) {
//
//        if (autoScriptService.bindingScript(planId, scriptIds)) {
//            return Result.OK("方案绑定脚本成功");
//        } else {
//            return Result.error("方案绑定脚本失败");
//        }
//    }

    /**
     * 导出excel
     *
     * @param request
     * @param autoPlan
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AutoPlan autoPlan) {
        return super.exportXls(request, autoPlan, AutoPlan.class, "方案管理");
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
        return super.importExcel(request, response, AutoPlan.class);
    }


    /**
     *
     * @param plan 1方案2脚本
     * @param id 唯一键
     * @param type 1回放2执行
     * @return Result
     */
    @AutoLog(value = "执行/回放方案/脚本")
    @ApiOperation(value = "执行/回放方案/脚本", notes = "执行/回放方案/脚本")
    @GetMapping(value = "/do/{plan}/{id}/{type}")
    public Result<?> doPlanOrScript(
            @PathVariable("plan") Integer plan,
            @PathVariable("id") String id,@PathVariable("type") Integer type) {
        //方案
        if (plan.equals(CommonConstant.DATA_INT_1)){
            QueryWrapper<AutoScript> criptQueryWrapper = new QueryWrapper<>();
            criptQueryWrapper.eq("plan_id",id);
            List<AutoScript> scripts = autoScriptService.list(criptQueryWrapper);
            if (IscTools.isNotCollection(scripts)){
                scripts.forEach(n->{
                    if (type.equals(CommonConstant.DATA_INT_1)){
                        //运行在录制节点
                    }
                    if (type.equals(CommonConstant.DATA_INT_2)){
                        //运行在执行节点
                    }
                });
            }
        }
        //脚本
        if (plan.equals(CommonConstant.DATA_INT_2)){
            AutoScript script = autoScriptService.getById(id);
            if (script != null){
                if (type.equals(CommonConstant.DATA_INT_1)){
                    //运行在录制节点
                }
                if (type.equals(CommonConstant.DATA_INT_2)){
                    //运行在执行节点
                }
            }
        }

        return Result.OK("执行/回放方案/脚本成功");
    }


    /**
     * 通过项目id查询
     *
     * @param projectId 项目id
     * @return
     */
    @AutoLog(value = "方案列表-通过项目id查询")
    @ApiOperation(value = "方案列表-通过项目id查询", notes = "方案列表-通过项目id查询")
    @GetMapping(value = "/getPlan")
    public Result<?> getPlanByProjectId(@RequestParam(name = "projectId", required = true) String projectId) {
        QueryWrapper<AutoPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id",projectId);
        List<AutoPlan> list = autoPlanService.list(queryWrapper);
        return Result.OK(list);
    }

}
