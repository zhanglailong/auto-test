package org.jeecg.modules.parameter.service;

import org.jeecg.modules.parameter.entity.ScriptParameter;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;


/**
 * 2021/8/19
 * @author yeyl
 */
public interface IScriptParameterService extends IService<ScriptParameter> {


    /**
     * 增加或者修改参数
     * @param scriptParameters 脚本参数
     */
    void addOrEditParameter(List<ScriptParameter> scriptParameters);



}
