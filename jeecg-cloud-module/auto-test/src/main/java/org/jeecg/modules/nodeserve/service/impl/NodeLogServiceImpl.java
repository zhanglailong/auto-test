package org.jeecg.modules.nodeserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.autoNodes.entity.AutoNodes;
import org.jeecg.modules.autoNodes.service.IAutoNodesService;
import org.jeecg.modules.autoResult.service.IAutoResultService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.nodeserve.entity.NodeIssueScriptPara;
import org.jeecg.modules.nodeserve.entity.NodeLog;
import org.jeecg.modules.nodeserve.mapper.NodeLogMapper;
import org.jeecg.modules.nodeserve.service.INodeLogService;
import org.jeecg.modules.nodeserve.service.ext.NodeServiceExt;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.jeecg.modules.task.service.IAuToRunningCaseService;
import org.jeecg.modules.task.vo.RunningCaseVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;


/**
 * @author yeyl
 */
@Service
@Slf4j
public class NodeLogServiceImpl extends ServiceImpl<NodeLogMapper, NodeLog> implements INodeLogService {

    @Resource
    private IAuToRunningCaseService autoRunningCaseService;
    @Resource
    private IAutoResultService autoResultService;
    @Resource
    NodeServiceExt nodeServiceExt;
    @Resource
    IAutoScriptService autoScriptService;
    @Resource
    IAutoNodesService autoNodesService;

    @Override
    public void saveOne(String url, String data, String method) {
        try {
            NodeLog nodeLog = new NodeLog();
            nodeLog.setUrl(url);
            nodeLog.setData(data);
            nodeLog.setMethod(method);
            nodeLog.setIp(InetAddress.getLocalHost().getHostAddress());
            nodeLog.setState(CommonConstant.DATA_INT_0);
            this.save(nodeLog);
        }catch (Exception e){
            log.error("调用统一接口日志表保存异常:"+e.getMessage());
        }
    }

    @Override
    @Async
    public void ascyncUpdateNodesAddScript(AutoScript autoScript,RunningCaseVO runningCaseVO,int type){
        try {
            UpdateWrapper<AutoNodes> autoNodesUpdateWrapper = new UpdateWrapper<>();
            autoNodesUpdateWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
            autoNodesUpdateWrapper.eq(CommonConstant.DATA_STRING_ID,autoScript.getRecordNodeId());
            autoNodesUpdateWrapper.set(CommonConstant.DATA_STRING_STATE,CommonConstant.DATA_INT_0);
            if (CommonConstant.DATA_INT_0.equals(type)) {
                if (nodeServiceExt.startRecord(runningCaseVO.getProjectName(), runningCaseVO.getTaskName(), runningCaseVO.getTestName(), autoScript.getScriptName(), autoScript.getId(), autoScript.getRecordNodeIp(), type)) {
                    autoScript.setState(CommonConstant.DATA_STR_1);
                } else {
                    autoScript.setState(CommonConstant.DATA_STR_4);
                    autoNodesService.update(autoNodesUpdateWrapper);
                    log.error("启动脚本" + autoScript.getScriptName() + "失败!");
                }
            }
            if (CommonConstant.DATA_INT_1.equals(type)) {
                if (nodeServiceExt.startRecord(runningCaseVO.getProjectName(), runningCaseVO.getTaskName(), runningCaseVO.getTestName(), autoScript.getScriptName(), autoScript.getId(), autoScript.getRecordNodeIp(), type)) {
                    autoScript.setState(CommonConstant.DATA_STR_0);
                } else {
                    autoScript.setState(CommonConstant.DATA_STR_5);
                    autoNodesService.update(autoNodesUpdateWrapper);
                    log.error("停止脚本" + autoScript.getScriptName() + "失败!");
                }
            }
            autoScriptService.saveOrUpdate(autoScript);
        }catch (Exception e){
            log.error("多线程修改节点状态新增脚本异常，原因:" + e.getMessage());
        }
    }


    @Override
    @Async
    public void ascyncPlayBackScript(RunningCaseVO runningCaseVo, AutoScript autoScript, Integer type,String autoResultId) {
        try {
            NodeIssueScriptPara nodeIssueScriptPara = new NodeIssueScriptPara();
            nodeIssueScriptPara.setProjectName(runningCaseVo.getProjectName());
            nodeIssueScriptPara.setTaskName(runningCaseVo.getTaskName());
            nodeIssueScriptPara.setTestName(runningCaseVo.getTestName());
            nodeIssueScriptPara.setScriptCode(autoScript.getId());
            nodeIssueScriptPara.setScriptName(autoScript.getScriptName());
            nodeIssueScriptPara.setContent(autoScript.getScriptContent());
            nodeIssueScriptPara.setRecordNodeIp(autoScript.getRecordNodeIp());
            nodeIssueScriptPara.setUniqueCode(autoResultId);
            if (CommonConstant.DATA_INT_1.equals(type)) {
                String runRecordScriptId = autoResultService.addRecord(autoScript.getId(), CommonConstant.DATA_INT_1);
                nodeIssueScriptPara.setUniqueCode(runRecordScriptId);
            }
            //TODO 测试阶段默认成功
            if (nodeServiceExt.issuedScript(nodeIssueScriptPara)) {
                autoScript.setState(CommonConstant.DATA_STR_2);
            } else {
                autoScript.setState(CommonConstant.DATA_STR_6);
                log.error("执行脚本：" + autoScript.getScriptName() + "失败");
            }
            autoScriptService.saveOrUpdate(autoScript);
        } catch (Exception e) {
            log.error("多线程下发脚本列表异常，原因:" + e.getMessage());
        }
    }
}
