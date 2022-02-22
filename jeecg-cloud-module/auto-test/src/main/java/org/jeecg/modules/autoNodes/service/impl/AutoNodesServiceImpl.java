package org.jeecg.modules.autoNodes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.modules.autoNodes.entity.AutoNodes;
import org.jeecg.modules.autoNodes.mapper.AutoNodesMapper;
import org.jeecg.modules.autoNodes.service.IAutoNodesService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.jeecg.modules.scriptandplan.entity.AutoScriptPlan;
import org.jeecg.modules.scriptandplan.mapper.AutoScriptPlanMapper;
import org.jeecg.modules.scriptandplan.service.IAutoScriptPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 节点管理
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Service
@Slf4j
public class AutoNodesServiceImpl extends ServiceImpl<AutoNodesMapper, AutoNodes> implements IAutoNodesService {

    @Resource
    AutoNodesMapper autoNodesMapper;
    @Autowired
    IAutoNodesService autoNodesService;
    @Autowired
    IAutoScriptService autoScriptService;
    @Autowired
    IAutoScriptPlanService autoScriptPlanService;
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void delete(String id) {
        try {
            //删除节点
            AutoNodes autoNodes = autoNodesMapper.selectById(id);
            autoNodes.setIdel(CommonConstant.DATA_INT_IDEL_1);
            int flag = autoNodesMapper.updateById(autoNodes);
//            if (flag == CommonConstant.DATA_INT_1) {
//                //删除节点下的脚本
//                QueryWrapper<AutoScript> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq(CommonConstant.DATA_STRING_NODEID,id);
//                queryWrapper.eq(CommonConstant.DATA_STRING_IDEL,CommonConstant.DATA_INT_IDEL_0);
//                List<AutoScript> scriptList = autoScriptService.list(queryWrapper);
//                if(CollectionUtils.isNotEmpty(scriptList)){
//                    scriptList.forEach(s->{
//                        s.setIdel(CommonConstant.DATA_INT_IDEL_1);
//                        autoScriptService.updateById(s);
//                    });
//                }
//                //删除绑定的脚本
//                QueryWrapper<AutoScriptPlan> aspQueryWrapper = new QueryWrapper<>();
//                aspQueryWrapper.eq(CommonConstant.DATA_STRING_NODEID,id);
//                aspQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL,CommonConstant.DATA_INT_IDEL_0);
//                List<AutoScriptPlan> asplist = autoScriptPlanService.list(aspQueryWrapper);
//                if(CollectionUtils.isNotEmpty(asplist)){
//                    for (AutoScriptPlan autoScriptPlan : asplist) {
//                        autoScriptPlan.setIdel(CommonConstant.DATA_INT_IDEL_1);
//                        autoScriptPlanService.updateById(autoScriptPlan);
//                    }
//                }
//            }else {
//                throw new UnsupportedOperationException("节点删除失败!");
//            }
        } catch (Exception e) {
            log.info("节点删除失败,原因是："+ e.getMessage());
        }

    }

    @Override
    public boolean updateNode(AutoNodes autoNodes) {
        try {
            if (autoNodesService.updateById(autoNodes)) {
                //查询节点所对应的脚本
                List<AutoScript> autoScriptList = autoScriptService.list(new QueryWrapper<AutoScript>()
                        .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                        .eq(CommonConstant.DATA_STRING_NODEID, autoNodes.getId()));
                if(CollectionUtils.isNotEmpty(autoScriptList)){
                    ArrayList<AutoScript> atList = Lists.newArrayList();
                    autoScriptList.parallelStream().peek(result->{
                        AutoScript autoScript = new AutoScript();
                        BeanUtils.copyProperties(result,autoScript);
                        autoScript.setRecordNodeName(autoNodes.getNodeName());
                        autoScript.setRecordNodeIp(autoNodes.getClientIp());
                        atList.add(autoScript);
                    }).collect(Collectors.toList());
                    autoScriptService.updateBatchById(atList);
                }
            }
            return true;
        } catch (Exception e) {
            log.info("节点修改失败，原因 "+e.getMessage());
            return  false;
        }
    }

    @Override
    public List<AutoNodes> getAutoNodesByNodeIds(String nodeIds) {
        QueryWrapper<AutoNodes> autoNodesQueryWrapper = new QueryWrapper<>();
        autoNodesQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        autoNodesQueryWrapper.in(CommonConstant.DATA_STRING_ID,nodeIds.split(","));
        return this.list(autoNodesQueryWrapper);
    }
}
