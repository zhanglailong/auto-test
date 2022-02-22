package org.jeecg.modules.scriptandplan.mapper;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.scriptandplan.entity.AutoScriptPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 方案脚本对应表
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
public interface AutoScriptPlanMapper extends BaseMapper<AutoScriptPlan> {



    /**
     * 方案下脚本上移
     * @param autoScriptPlan  绑定的脚本
     *
     */
    AutoScriptPlan aspMoveUp(AutoScriptPlan autoScriptPlan);

    /**
     * 方案下脚本下移
     * @param autoScriptPlan 绑定的脚本
     */
    AutoScriptPlan aspMoveDown(AutoScriptPlan autoScriptPlan);
}
