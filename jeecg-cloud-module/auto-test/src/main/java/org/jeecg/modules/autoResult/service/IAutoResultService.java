package org.jeecg.modules.autoResult.service;

import org.jeecg.modules.autoResult.entity.AutoResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.nfunk.jep.function.Str;

/**
 * @Description: 测试结果管理
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface IAutoResultService extends IService<AutoResult> {

    /**
     * 回放时候，添加单个脚本记录和用例点击的记录
     * @param id  0 是用例id或者是1是脚本id
     * @param type  回放标识
     */
    public String addRecord(String id, Integer type);


}
