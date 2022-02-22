package org.jeecg.modules.project.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.webservice.SoapUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.project.entity.AutoProject;
import org.jeecg.modules.project.service.IAutoProjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * @Description: 项目管理
 * @Author: zll
 * @Date: 2021-08-17
 * @Version: V1.0
 */
@Api(tags = "项目管理")
@RestController
@RequestMapping("/project/autoProject")
@Slf4j
public class AutoProjectController extends JeecgController<AutoProject, IAutoProjectService> {
    @Autowired
    private IAutoProjectService autoProjectService;

    /**
     * 分页列表查询
     *
     * @param autoProject
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "项目管理-分页列表查询")
    @ApiOperation(value = "项目管理-分页列表查询", notes = "项目管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AutoProject autoProject,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<AutoProject> queryWrapper = QueryGenerator.initQueryWrapper(autoProject, req.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        Page<AutoProject> page = new Page<AutoProject>(pageNo, pageSize);
        IPage<AutoProject> pageList = autoProjectService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param autoProject
     * @return
     */
    @AutoLog(value = "项目管理-添加")
    @ApiOperation(value = "项目管理-添加", notes = "项目管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AutoProject autoProject) {
        try {
        	if(autoProject ==  null){
				return Result.error("对象不能为空");
			}
            autoProject.setIdel(CommonConstant.DATA_INT_IDEL_0);
            autoProjectService.save(autoProject);
            return Result.OK("添加成功！");
        } catch (Exception e) {
            log.error("添加失败" + e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 编辑
     *
     * @param autoProject
     * @return
     */
    @AutoLog(value = "项目管理-编辑")
    @ApiOperation(value = "项目管理-编辑", notes = "项目管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AutoProject autoProject) {
        try {
            if (StringUtils.isEmpty(autoProject.getId())) {
                return Result.OK("缺少参数");
            }
            autoProjectService.edit(autoProject);
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
    @AutoLog(value = "项目管理-通过id删除")
    @ApiOperation(value = "项目管理-通过id删除", notes = "项目管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
            if (StringUtils.isEmpty(id)) {
                return Result.error("缺少参数");
            }
            if(autoProjectService.delete(id)){
                return Result.OK("删除成功!");
            }else {
                return Result.error("删除失败");
            }
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
        this.autoProjectService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
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
        AutoProject autoProject = autoProjectService.getById(id);
        if (autoProject == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(autoProject);
    }


    /**
     * 查询项目列表
     */
    @AutoLog(value = "查询项目列表")
    @ApiOperation(value = "查询项目列表", notes = "查询项目列表")
    @GetMapping(value = "/projectList")
    public Result<?> getProjectList() {
        List<AutoProject> list = autoProjectService.getProjectList();
        return Result.OK(list);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param autoProject
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AutoProject autoProject) {
        return super.exportXls(request, autoProject, AutoProject.class, "项目管理");
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
        return super.importExcel(request, response, AutoProject.class);
    }

}
