package org.jeecg.modules.nodeserve.service.ext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.autoResult.entity.AutoResult;
import org.jeecg.modules.autoResult.service.IAutoResultService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.nodeserve.entity.NodeIssueScriptPara;
import org.jeecg.modules.nodeserve.entity.NodeStartScriptPara;
import org.jeecg.modules.nodeserve.service.INodeLogService;
import org.jeecg.modules.restTemplate.RestTemplateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yeyl
 * @version V1.0
 * @date 2021/8/17
 */
@Service
@Slf4j
public class NodeServiceExt extends RestTemplateService {

    @Resource
    private INodeLogService iNodeLogService;

    @Resource
    private IAutoResultService autoResultService;

    /**
     * 下发脚本
     */
    @Value(value = "${node.issuedScript}")
    private String issuedScript;

    /**
     * 启动脚本接口
     */
    @Value(value = "${node.startScript}")
    private String startScript;

    /**
     * 启动脚本接口
     */
    @Value(value = "${node.startRecord}")
    private String startRecord;

    /**
     * 修改断言
     */
    @Value(value = "${node.modifyAssert}")
    private String modifyAssert;

    /**
     * 可视化文件转py脚本
     */
    @Value(value = "${node.visualization}")
    private String visualization;

    /**
     * 下发脚本
     * @param nodeIssueScriptPara 脚本下发参数
     * @return true 成功 false  不成功
     */
    public Boolean issuedScript(NodeIssueScriptPara nodeIssueScriptPara){
        Map<String, Object> pMap = new HashMap<>(5);
        pMap.put("projectName", nodeIssueScriptPara.getProjectName());
        pMap.put("taskName", nodeIssueScriptPara.getTaskName());
        pMap.put("testName", nodeIssueScriptPara.getTestName());
        pMap.put("scriptCode", nodeIssueScriptPara.getScriptCode());
        pMap.put("scriptName", nodeIssueScriptPara.getScriptName());
        pMap.put("content", nodeIssueScriptPara.getContent());
        pMap.put("scriptType", nodeIssueScriptPara.getScriptType());
        pMap.put("dataContent", nodeIssueScriptPara.getDataContent());
        pMap.put("dataFile", nodeIssueScriptPara.getDataFile());
        pMap.put("uniqueCode", nodeIssueScriptPara.getUniqueCode());
        String resultData = postHeadersT(issuedScript.replace("{ip}",nodeIssueScriptPara.getRecordNodeIp()),getHeaders(CommonConstant.DATA_INT_1),pMap,HttpMethod.POST);
        iNodeLogService.saveOne(issuedScript, JSON.toJSONString(nodeIssueScriptPara),HttpMethod.POST.toString());
        if (StringUtils.isNotBlank(resultData)){
            return getIsSuccess(resultData);
        }
        return false;
    }


    /**
     * 修改断言值
     * @param scriptName 脚本名称
     * @param project 项目名
     * @param assertName 断言名称
     * @param assertValue 断言值
     * @return true 成功 false  不成功
     */
    public Boolean modifyAssert(String scriptName, String project, String assertName ,String assertValue) {
        Map<String, Object> pMap = new HashMap<>(5);
        pMap.put("scriptName", scriptName);
        pMap.put("project", project);
        pMap.put("assertName", assertName);
        pMap.put("assertValue", assertValue);
        String resultData = postHeadersT(modifyAssert, getHeaders(CommonConstant.DATA_INT_1), pMap, HttpMethod.POST);
        iNodeLogService.saveOne(modifyAssert, JSON.toJSONString(pMap),HttpMethod.POST.toString());
        if (StringUtils.isNotBlank(resultData)) {
            return getIsSuccess(resultData);
        }
        return false;
    }
    /**
     * 启动暂停停止脚本
     *  project  项目名
     *  status 状态 停STOP 暂停SUSPEND  启动START
     *  scriptName 脚本名
     * @return true 成功 false  不成功
     */
    public Boolean startScript(NodeStartScriptPara nodeStartScriptPara) {
        Map<String, Object> pMap = new HashMap<>(4);
        pMap.put("projectName", nodeStartScriptPara.getProjectName());
        pMap.put("status", nodeStartScriptPara.getStatus());
        pMap.put("scriptName", nodeStartScriptPara.getScriptName());
        pMap.put("scriptCode", nodeStartScriptPara.getScriptCode());
        pMap.put("runRecordId", nodeStartScriptPara.getRunRecordId());
        pMap.put("taskName", nodeStartScriptPara.getTaskName());
        pMap.put("testName", nodeStartScriptPara.getTestName());
        String resultData = postHeadersT(startScript.replace("{ip}",nodeStartScriptPara.getRecordNodeIp()), getHeaders(CommonConstant.DATA_INT_1), pMap, HttpMethod.POST);
        iNodeLogService.saveOne(startScript, JSON.toJSONString(pMap),HttpMethod.POST.toString());
        if (StringUtils.isNotBlank(resultData)) {
            return getIsSuccess(resultData);
        }
        return false;
    }


    /**
     *开始结束录制脚本
     * @param projectName 测试项目名称
     * @param taskName 测试项名称
     * @param testName 测试用例名称
     * @param scriptName 脚本名称
     * @param scriptCode 脚本唯一标识
     * @param recordNodeIp 录制节点IP
     * @param type 0为开始1为停止
     * @return true 成功 false  不成功
     */
    public Boolean startRecord(String projectName,String taskName,String testName,String scriptName,String scriptCode,String recordNodeIp,Integer type ) {
        Map<String, Object> pMap = new HashMap<>(4);
        pMap.put("projectName", projectName);
        pMap.put("taskName", taskName);
        pMap.put("testName", testName);
        pMap.put("scriptName", scriptName);
        pMap.put("scriptCode", scriptCode);
        pMap.put("type", type);
        String resultData = postHeadersT(startRecord.replace("{ip}",recordNodeIp), getHeaders(CommonConstant.DATA_INT_1), pMap, HttpMethod.POST);
        log.info("resultData:"+resultData);
        iNodeLogService.saveOne(startScript, JSON.toJSONString(pMap),HttpMethod.POST.toString());
        if (StringUtils.isNotBlank(resultData)) {
            return getIsSuccess(resultData);
        }
        return false;
    }

    private Boolean getIsSuccess(String resultData) {
        JSONObject parseObject = JSONObject.parseObject(resultData);
        if (parseObject != null && !parseObject.isEmpty()
                && CommonConstant.DATA_STR_200.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())) {
            return true;
        }
        return false;
    }

    /**
     * 可视化文件转py脚本
     * @param scriptName  脚本名称
     * @param uniqueCode  唯一标识
     * @param content  可视化文件字符串
     */
    public String viewToPy(String uniqueCode,String scriptName,String content) {
        Map<String, Object> pMap = new HashMap<>(4);
        pMap.put("content", content);
        pMap.put("uniqueCode", uniqueCode);
        pMap.put("scriptName", scriptName);
        String resultData = postHeadersT(visualization, getHeaders(CommonConstant.DATA_INT_1), pMap, HttpMethod.POST);
        iNodeLogService.saveOne(visualization, JSON.toJSONString(pMap),HttpMethod.POST.toString());
        if (StringUtils.isNotBlank(resultData)) {
            JSONObject parseObject = JSONObject.parseObject(resultData);
            return parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_MESSAGE).toString();
        }else {
            return null;
        }
    }

}
