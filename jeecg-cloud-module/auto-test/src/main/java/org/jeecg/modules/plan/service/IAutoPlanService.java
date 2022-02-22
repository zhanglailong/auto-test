package org.jeecg.modules.plan.service;

import org.jeecg.modules.plan.entity.AutoPlan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 方案管理
 * @Author: zll
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface IAutoPlanService extends IService<AutoPlan> {


    void delete(String id);

    void edit(AutoPlan autoPlan);
}
