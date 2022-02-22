package org.jeecg.modules.parameter.service.impl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.parameter.entity.ScriptParameter;
import org.jeecg.modules.parameter.mapper.ScriptParameterMapper;
import org.jeecg.modules.parameter.service.IScriptParameterService;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 2021-08-19
 *
 * @author yeyl
 */
@Service
@AllArgsConstructor
@Slf4j
public class ScriptParameterServiceImpl extends ServiceImpl<ScriptParameterMapper, ScriptParameter> implements IScriptParameterService {
    @Resource
    IAutoScriptService autoScriptService;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addOrEditParameter(List<ScriptParameter> scriptParameters) {
        List<ScriptParameter> parameters = scriptParameters.stream().filter(scriptParameter -> !CommonConstant.DATA_INT_IDEL_1.equals(scriptParameter.getIdel())).collect(Collectors.toList());
        Map<String, Long> mapGroup = parameters.stream().collect(Collectors.groupingBy(ScriptParameter::getParameterKey, Collectors.counting()));
        // 这个脚本重复的参数 提示用户key不能重复
        List<String> keys = mapGroup.entrySet().stream().filter(entry -> entry.getValue() > CommonConstant.DATA_INT_1).map(Map.Entry::getKey).collect(Collectors.toList());
        if (keys.size()!=0){
            throw new UnsupportedOperationException(keys+"键值存在重复! 键值不可以重复");
        }
        //看是否有脚本名和脚本文件编号
        String scriptId = scriptParameters.get(0).getScriptId();
        AutoScript script = autoScriptService.getById(scriptId);
        if (script==null
                ||StringUtils.isBlank(script.getScriptContent())
                ||StringUtils.isBlank(script.getScriptName())
        ){
            log.info(script+"脚本相关文件不能为空");
            throw new UnsupportedOperationException("脚本相关文件不能为空");
        }
        //如果有id修改 如果无id增加
        List<ScriptParameter> addScriptParameters=new ArrayList<>();
        List<ScriptParameter> editScriptParameters=new ArrayList<>();
        scriptParameters.forEach(scriptParameter -> {

            if (StringUtils.isNotBlank(scriptParameter.getId())){
                //修改
                ScriptParameter parameter = this.getById(scriptParameter.getId());
                parameter.setParameterKey(scriptParameter.getParameterKey());
                parameter.setParameterValue(scriptParameter.getParameterValue());
                parameter.setIdel(scriptParameter.getIdel()==null?CommonConstant.DATA_INT_IDEL_0:scriptParameter.getIdel());
                editScriptParameters.add(parameter);
            }else{
                ScriptParameter add=new ScriptParameter();
                add.setIdel(CommonConstant.DATA_INT_IDEL_0);
                add.setParameterKey(scriptParameter.getParameterKey());
                add.setParameterValue(scriptParameter.getParameterValue());
                add.setScriptId(scriptParameter.getScriptId());
                add.setScriptName(scriptParameter.getScriptName());
                addScriptParameters.add(add);
            }
        });
        if (addScriptParameters.size()!=0){
            if (!this.saveBatch(addScriptParameters)){
                throw new UnsupportedOperationException("脚本参数批量新增异常");
            }
        }
        if (editScriptParameters.size()!=0){
            if (!this.updateBatchById(editScriptParameters)){
                throw new UnsupportedOperationException("脚本参数批量修改异常");
            }
        }

    }


}
