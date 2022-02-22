package org.jeecg.modules.script.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.autoNodes.entity.AutoNodes;
import org.jeecg.modules.script.entity.AutoScript;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.script.entity.AutoScriptVo;
import org.jeecg.modules.script.entity.ScriptViewVo;
import org.jeecg.modules.task.vo.RunningCaseVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description: 脚本管理
 * @Author: zll
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface IAutoScriptService extends IService<AutoScript> {

    /**
     * 脚本修改
     * @param autoScript
     */
    boolean edit(AutoScriptVo autoScript);

    /**
     * 脚本删除
     * @param id
     */
    boolean delete(String id);
    /**
     * 绑定脚本树结构
     */
    List<AutoScript> treeScriptList();

    /**
     * 绑定脚本
     * @param testCaseId
     * @param scriptIds
     * @return
     */
    String bindingScript(String testCaseId, String scriptIds);
    /**
     * 脚本的上移
     */
    void scriptUp(AutoScript autoScript);

    /**
     * 脚本的下移
     */
    void scriptdown(AutoScript autoScript);

    /**
     * 当前脚本的数量
     * @return
     */
    Integer getScriptList(QueryWrapper queryWrapper);

    /**
     * 添加脚本
     * @return
     */
    boolean inportScript(AutoScriptVo autoScript);


    /**
     * 保存脚本文件
     * @param scriptViewVos 可视化文件列表
     * @param scriptId 脚本Id
     * @return true或false
     */
    boolean saveScriptText(List<ScriptViewVo> scriptViewVos, String scriptId);


    /**
     * 脚本拷贝
     * @param desScript 目标脚本 输入信息
     * @param sourceScript 源脚本
     */
    void copyScript(AutoScript desScript, AutoScript sourceScript);

    /**
     * 获取脚本可视化文件内容转成集合
     * @param scriptId 脚本id
     * @return 脚本可视化集合
     */
    List<ScriptViewVo> getViewContentByScriptId(String scriptId);

    boolean addScript(AutoScriptVo autoScript);

    /**
     * 修改节点状态新增脚本
     * @param runningCaseVO 测试用例信息
     * @param type 0为开始1为停止
     * @param autoNodesList 节点信息集合
     * @return 0
     */
    List<AutoScript> updateNodesAddScript(RunningCaseVO runningCaseVO,Integer type,List<AutoNodes> autoNodesList);

    /**
     * 执行下发脚本列表
     * @param autoScriptList 脚本集合
     * @param id  测试用例或者脚本id
     * @param type 0 测试用例 1 脚本
     *
     */
    void playBackScript(List<AutoScript> autoScriptList,String id,Integer type);

    /**
     * 启动脚本
     * @param scriptCode  脚本id
     * @return 0
     */
    String playBackStartScript(String scriptCode);


    void removeBind(String id);

    boolean setSort(AutoScriptVo autoScript);
}
