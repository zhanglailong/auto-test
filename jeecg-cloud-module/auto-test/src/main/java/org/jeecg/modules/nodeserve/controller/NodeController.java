package org.jeecg.modules.nodeserve.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.autoNodes.entity.AutoNodes;
import org.jeecg.modules.autoNodes.service.IAutoNodesService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.nodeserve.entity.NodeIssueScriptPara;
import org.jeecg.modules.nodeserve.entity.NodeModifyAssertPara;
import org.jeecg.modules.nodeserve.service.INodeLogService;
import org.jeecg.modules.nodeserve.service.ext.NodeServiceExt;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.jeecg.modules.task.service.IAuToRunningCaseService;
import org.jeecg.modules.task.service.IRunningCaseService;
import org.jeecg.modules.task.vo.RunningCaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author yeyl
 */
@Api(tags="节点接口")
@RestController
@RequestMapping("/node")
@Slf4j
@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
public class NodeController {

    @Resource
    private NodeServiceExt nodeServiceExt;
    @Resource
    private IRunningCaseService runningCaseService;
    @Resource
    private IAuToRunningCaseService auToRunningCaseService;
    @Resource
    private IAutoNodesService autoNodesService;
    @Autowired
    private IAutoScriptService autoScriptService;
    @Autowired
    private INodeLogService nodeLogService;


    /**
     * 脚本下发
     *
     * @param nodeIssueScriptParas 脚本下发参数
     * @return 成功或者失败
     */
    @AutoLog(value = "脚本批量下发")
    @ApiOperation(value = "脚本批量下发", notes = "脚本批量下发")
    @PostMapping(value = "/issue/script")
    public Result<?> issuedScript(@RequestBody List<NodeIssueScriptPara> nodeIssueScriptParas) {
        try {
            nodeIssueScriptParas.forEach(n->nodeServiceExt.issuedScript(n));
            return Result.OK("脚本批量下发成功");
        } catch (Exception e) {
            log.info("脚本批量下发异常，原因:" + e.getMessage());
            return Result.error("脚本批量下发异常，原因:" + e.getMessage());
        }
    }


//    /**
//     *
//     * @param caseId caseId-测试用例id
//     * @param type 0为开始1为停止
//     * @param nodeIds 节点id集合
//     * @return 结果
//     */
//    @AutoLog(value = "启动/暂停/停止脚本")
//    @ApiOperation(value = "启动/暂停/停止脚本", notes = "启动/暂停/停止脚本")
//    @PostMapping(value = "/start/script")
//    public Result<?> startScript(String caseId,Integer type,String nodeIds) {
//        try {
//            if (StringUtils.isEmpty(caseId)){
//                return Result.error("测试项id不能为空");
//            }
//            if (StringUtils.isEmpty(nodeIds)){
//                return Result.error("节点id集合能为空");
//            }
//            //获取测试用例信息
//            RunningCaseVO runningCaseVO = runningCaseService.getRunningCaseVoByCaseId(caseId);
//
//            if (runningCaseVO==null || StringUtils.isEmpty(runningCaseVO.getId())){
//                return Result.error("没有找到选择的测试用例");
//            }
//            //查找节点集合信息
//            List<AutoNodes> autoNodesList = autoNodesService.getAutoNodesByNodeIds(nodeIds);
//            if (autoNodesList == null || autoNodesList.size() <=0){
//                return Result.error("没有找到选择的节点信息");
//            }
//            //查看节点是否有正在录制中的状态
//            StringBuffer nodeNameBuffer = new StringBuffer();
//            autoNodesList.forEach(node->{
//                if (node.getState().equals(CommonConstant.DATA_INT_1)){
//                    if (StringUtils.isEmpty(nodeNameBuffer.toString())){
//                        nodeNameBuffer.append(node.getNodeName());
//                    }else{
//                        nodeNameBuffer.append(",").append(node.getNodeName());
//                    }
//                }
//            });
//            if (StringUtils.isNotBlank(nodeNameBuffer.toString())){
//                Result.error("节点:"+nodeNameBuffer.toString()+"正在录制状态，请暂停之后在启动!");
//            }
//            if (autoScriptService.updateNodesAddScript(runningCaseVO, type, autoNodesList).equals(CommonConstant.DATA_STR_0)) {
//                if (type.equals(CommonConstant.DATA_INT_0)){
//                    return Result.OK("启动/脚本成功");
//                }
//                if (type.equals(CommonConstant.DATA_INT_1)){
//                    return Result.OK("停止脚本成功");
//                }
//            }
//        } catch (Exception e) {
//            log.info("启动/暂停/停止脚本异常，原因:" + e.getMessage());
//            return Result.error("启动/暂停/停止脚本异常，原因:" + e.getMessage());
//        }
//        return Result.error("启动/暂停/停止脚本失败");
//    }

    @AutoLog(value = "修改断言状态")
    @ApiOperation(value = "修改断言状态", notes = "修改断言状态")
    @PostMapping(value = "/modify/assert")
    public Result<?> modifyAssert(@RequestBody NodeModifyAssertPara modifyAssertPara) {
        try {
            if (nodeServiceExt.modifyAssert(modifyAssertPara.getScriptName(), modifyAssertPara.getProject(), modifyAssertPara.getAssertName(), modifyAssertPara.getAssertValue())) {
                return Result.OK("修改断言状态成功");
            }
        } catch (Exception e) {
            log.info("修改断言状态异常，原因:" + e.getMessage());
            return Result.error("修改断言状态异常，原因:" + e.getMessage());
        }
        return Result.error("启修改断言状态失败");
    }

    /**
     *
     * @param caseId caseId-测试用例id
     * @param type 0为开始1为停止
     * @param nodeIds 节点id集合
     * @return 结果
     */
    @AutoLog(value = "开始/停止录制")
    @ApiOperation(value = "开始/停止录制", notes = "开始/停止录制")
    @PostMapping(value = "/server/script/record")
    public Result<?> startRecord(@RequestParam(name = "caseId")String caseId,@RequestParam(name = "type")Integer type,@RequestParam(name = "nodeIds")String nodeIds) {
        try {
            if (StringUtils.isEmpty(caseId)){
                return Result.error("测试项id不能为空");
            }
            if (StringUtils.isEmpty(nodeIds)){
                return Result.error("节点id集合能为空");
            }
            //获取测试用例信息
            RunningCaseVO runningCaseVO = auToRunningCaseService.getRunningCaseVoByCaseId(caseId);
            if (runningCaseVO==null || StringUtils.isEmpty(runningCaseVO.getId())){
                return Result.error("没有找到选择的测试用例");
            }
            if (CommonConstant.DATA_INT_1.equals(type)){
                nodeIds = runningCaseVO.getNodeIds();
            }
            //查找节点集合信息
            List<AutoNodes> autoNodesList = autoNodesService.getAutoNodesByNodeIds(nodeIds);
            if (autoNodesList == null || autoNodesList.size() <=0){
                return Result.error("没有找到选择的节点信息");
            }
            if (CommonConstant.DATA_INT_0.equals(type)){
                //查看节点是否有正在录制中的状态
                StringBuffer nodeNameBuffer = new StringBuffer();
                autoNodesList.forEach(node->{
                    if (node.getState().equals(CommonConstant.DATA_INT_1)){
                        if (StringUtils.isEmpty(nodeNameBuffer.toString())){
                            nodeNameBuffer.append(node.getNodeName());
                        }else{
                            nodeNameBuffer.append(",").append(node.getNodeName());
                        }
                    }
                });
                if (StringUtils.isNotBlank(nodeNameBuffer.toString())){
                    return Result.error("节点:"+nodeNameBuffer.toString()+"正在录制状态，请暂停之后在启动!");
                }
            }
            List<AutoScript> autoScripts = autoScriptService.updateNodesAddScript(runningCaseVO, type, autoNodesList);
            if (autoScripts != null && autoScripts.size() >0){
                autoScripts.forEach(script -> {
                    //给客户端发送启动/暂停指令
                    if(type.equals(CommonConstant.DATA_INT_0)&&script.getState().equals(CommonConstant.DATA_STR_0)){
                        nodeLogService.ascyncUpdateNodesAddScript(script,runningCaseVO,type);
                    }
                    //只有录制状态的脚本才可以停止
                    if(type.equals(CommonConstant.DATA_INT_1)&&script.getState().equals(CommonConstant.DATA_STR_1)){
                        nodeLogService.ascyncUpdateNodesAddScript(script,runningCaseVO,type);
                    }
                });
                if (type.equals(CommonConstant.DATA_INT_0)){
                    return Result.OK("启动录制成功");
                }
                if (type.equals(CommonConstant.DATA_INT_1)){
                    return Result.OK("停止录制成功");
                }
            }
        } catch (Exception e) {
            log.info("开始/停止录制异常，原因:" + e.getMessage());
            return Result.error("开始/停止录制异常，原因:" + e.getMessage());
        }
        return Result.error("开始/停止录制失败");
    }

    /**
     *
     * @param id 测试用例id或者脚本id
     * @param type 0测试用例1脚本
     * @return Result
     */
    @AutoLog(value = "执行/回放方案/脚本")
    @ApiOperation(value = "执行/回放方案/脚本", notes = "执行/回放方案/脚本")
    @PostMapping(value = "/play/back/script")
    public Result<?> playBackScript(@RequestParam(name = "id")String id,@RequestParam(name = "type") Integer type) {
        try {
            if (StringUtils.isEmpty(id) || type == null){
                return Result.error("编号或者类型不能为空!");
            }

            QueryWrapper<AutoScript> autoScriptQueryWrapper = new QueryWrapper<>();
            autoScriptQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL,CommonConstant.DATA_INT_IDEL_0);
            //测试用例
            if (CommonConstant.DATA_INT_0.equals(type)){
                autoScriptQueryWrapper.eq(CommonConstant.DATA_STRING_TEST_CASE_ID,id);
            }
            //脚本
            if (CommonConstant.DATA_INT_1.equals(type)){
                autoScriptQueryWrapper.eq(CommonConstant.DATA_STRING_ID,id);
            }
            List<AutoScript> autoScriptList = autoScriptService.list(autoScriptQueryWrapper);
            if (autoScriptList.size() <= 0){
                return Result.error("找不到对应执行脚本");
            }
            //判断脚本是否在录制或者执行中
            StringBuffer scriptRecord = new StringBuffer();
            StringBuffer scriptPlay = new StringBuffer();
            autoScriptList.forEach(n->{
                if (CommonConstant.DATA_STR_1.equals(n.getState())) {
                    if (scriptRecord.toString().isEmpty()) {
                        scriptRecord.append(n.getScriptName());
                    }else {
                        scriptRecord.append(",").append(n.getScriptName());
                    }
                }
                if (CommonConstant.DATA_STR_2.equals(n.getState()) || CommonConstant.DATA_STR_3.equals(n.getState())) {
                    if (scriptPlay.toString().isEmpty()) {
                        scriptPlay.append(n.getScriptName());
                    }else {
                        scriptPlay.append(",").append(n.getScriptName());
                    }
                }
            });
            if (StringUtils.isNotBlank(scriptRecord.toString())){
                return Result.error("脚本:"+scriptRecord.toString()+"正在录制中");
            }
            if (StringUtils.isNotBlank(scriptPlay.toString())){
                return Result.error("脚本:"+scriptPlay.toString()+"正在执行中");
            }
            //下发脚本  执行脚本
            autoScriptService.playBackScript(autoScriptList,id,type);
            return Result.OK("执行成功");
        }catch (Exception e){
            return Result.error("执行/回放方案/脚本异常，原因:"+e.getMessage());
        }
    }

}
