package org.jeecg.modules.nodeserve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.autoNodes.entity.AutoNodes;
import org.jeecg.modules.nodeserve.entity.NodeLog;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.task.vo.RunningCaseVO;

import java.util.List;

/**
 * @author yeyl
 */
public interface INodeLogService extends IService<NodeLog> {

    /**
     * 记录统一接口发送记录
     * @param url 请求地址
     * @param data 请求数据
     * @param method 请求方式
     */
    void saveOne(String url,String data,String method);

    /**
     * 多线程修改节点状态新增脚本
     * @param runningCaseVO 测试用例信息
     * @param type 0为开始1为停止
     * @param autoScript 脚本信息
     *
     */
    void ascyncUpdateNodesAddScript(AutoScript autoScript,RunningCaseVO runningCaseVO,int type);
    /**
     * 执行下发脚本列表
     * @param runningCaseVo 测试用例信息
     * @param autoScript  脚本信息
     * @param type 0 测试用例 1 脚本
     * @param autoResultId 结果id
     *
     */
    void ascyncPlayBackScript(RunningCaseVO runningCaseVo, AutoScript autoScript, Integer type,String autoResultId);
}
