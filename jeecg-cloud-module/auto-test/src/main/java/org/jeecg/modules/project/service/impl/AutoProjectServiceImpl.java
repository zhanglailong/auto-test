package org.jeecg.modules.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.plan.entity.AutoPlan;
import org.jeecg.modules.plan.service.IAutoPlanService;
import org.jeecg.modules.project.entity.AutoProject;
import org.jeecg.modules.project.mapper.AutoProjectMapper;
import org.jeecg.modules.project.service.IAutoProjectService;
import org.jeecg.modules.scriptandplan.entity.AutoScriptPlan;
import org.jeecg.modules.scriptandplan.service.IAutoScriptPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 项目管理
 * @Author: zll
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Service
@Slf4j
public class AutoProjectServiceImpl extends ServiceImpl<AutoProjectMapper, AutoProject> implements IAutoProjectService {

    @Resource
    AutoProjectMapper autoProjectMapper;
    @Autowired
    IAutoPlanService autoPlanService;
    @Autowired
    IAutoScriptPlanService autoScriptPlanService;
    @Override
    public List<AutoProject> getProjectList() {
        QueryWrapper<AutoProject> queryWrapper = new QueryWrapper<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean delete(String id) {
        try {
            AutoProject autoProject = autoProjectMapper.selectById(id);
            autoProject.setIdel(CommonConstant.DATA_INT_IDEL_1);
            int flag = autoProjectMapper.updateById(autoProject);
            if(flag == CommonConstant.DATA_INT_1){
                //删除项目下的方案
                List<AutoPlan> planList = autoPlanService.list(new QueryWrapper<AutoPlan>()
                        .eq(CommonConstant.DATA_STRING_PROJECT_ID, id)
                        .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0));
                if(CollectionUtils.isNotEmpty(planList)){
                    planList.forEach(ap->{
                        ap.setIdel(CommonConstant.DATA_INT_IDEL_1);
                        //删除方案下的脚本
                        if(autoPlanService.updateById(ap)){
                            List<AutoScriptPlan> asplist = autoScriptPlanService.list(new QueryWrapper<AutoScriptPlan>()
                                    .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                                    .eq(CommonConstant.DATA_STRING_PLANID, ap.getId()));
                            if(CollectionUtils.isNotEmpty(asplist)){
                                asplist.forEach(asp->{
                                    asp.setIdel(CommonConstant.DATA_INT_IDEL_1);
                                    autoScriptPlanService.updateById(asp);
                                });
                            }
                        }
                    });
                }
                return true;
            }
        } catch (Exception e) {
            log.info("项目删除失败，原因是"+e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public boolean edit(AutoProject autoProject) {
        try {
            int flag = autoProjectMapper.updateById(autoProject);
            if (flag == CommonConstant.DATA_INT_1) {
                //修改方案下的项目名称
                List<AutoPlan> autoPlanList = autoPlanService.list(new QueryWrapper<AutoPlan>()
                        .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                        .eq(CommonConstant.DATA_STRING_PROJECT_ID, autoProject.getId())
                );
                if(CollectionUtils.isNotEmpty(autoPlanList)){
                    ArrayList<AutoPlan> planList = Lists.newArrayList();
                    autoPlanList.parallelStream().peek(result->{
                        AutoPlan autoPlan = new AutoPlan();
                        BeanUtils.copyProperties(result,autoPlan);
                        autoPlan.setProjectName(autoProject.getProjectName());
                        planList.add(autoPlan);
                    }).collect(Collectors.toList());
                    autoPlanService.updateBatchById(planList);
                }
            }
            return true;
        } catch (Exception e) {
            log.info("项目修改失败，原因是 "+e.getMessage());
            return false;
        }
    }
}
