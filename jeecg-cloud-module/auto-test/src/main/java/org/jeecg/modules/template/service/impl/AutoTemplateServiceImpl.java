package org.jeecg.modules.template.service.impl;

import com.alibaba.fastjson.JSON;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.template.entity.AutoTemplate;
import org.jeecg.modules.template.entity.TemplateJson;
import org.jeecg.modules.template.mapper.AutoTemplateMapper;
import org.jeecg.modules.template.service.IAutoTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 模板列表
 * @Author: zll
 * @Date:   2021-11-02
 * @Version: V1.0
 */
@Service
public class AutoTemplateServiceImpl extends ServiceImpl<AutoTemplateMapper, AutoTemplate> implements IAutoTemplateService {

    @Resource
    private IAutoTemplateService autoTemplateService;


    @Override
    public boolean addTemplate(TemplateJson templateJson) {
        AutoTemplate autoTemplate = new AutoTemplate();
        BeanUtils.copyProperties(templateJson,autoTemplate);
        autoTemplate.setNodeList(JSON.toJSONString(templateJson.getNodeList()));
        autoTemplate.setIdel(CommonConstant.DATA_INT_0);
        return autoTemplateService.saveOrUpdate(autoTemplate);
    }

    @Override
    public void delete(String id) {
        try {
//            AutoTemplate autoTemplate = autoTemplateService.getById(id);
//            autoTemplate.setIdel(CommonConstant.DATA_INT_1);
            autoTemplateService.removeById(id);
        } catch (Exception e) {
            log.error("模板删除失败，原因："+e.getMessage());
        }
    }

    @Override
    public boolean updateTemplate(TemplateJson templateJson) {
        AutoTemplate autoTemplate = new AutoTemplate();
        BeanUtils.copyProperties(templateJson,autoTemplate);
        autoTemplate.setNodeList(JSON.toJSONString(templateJson.getNodeList()));
        autoTemplate.setIdel(CommonConstant.DATA_INT_0);
        return autoTemplateService.updateById(autoTemplate);
    }


}
