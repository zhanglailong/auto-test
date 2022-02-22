package org.jeecg.modules.scriptandplan.service;

import org.jeecg.modules.scriptandplan.entity.AutoScriptPlan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 方案脚本对应表
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
public interface IAutoScriptPlanService extends IService<AutoScriptPlan> {
    /**
     * 绑定脚本
     * @param planId
     * @param scriptIds
     * @return
     */
    boolean bindingScript(String planId, String scriptIds);

    void delete(String id);

    /**
     * 方案下脚本的上移
     * @param autoScriptPlan
     */
    void scriptUp(AutoScriptPlan autoScriptPlan);

    /**
     * 方案下脚本的下移
     * @param autoScriptPlan
     */
    void scriptdown(AutoScriptPlan autoScriptPlan);
}
