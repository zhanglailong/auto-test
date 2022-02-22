package org.jeecg.modules.template.service;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.template.entity.AutoTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.template.entity.TemplateJson;

/**
 * @Description: 模板列表
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
public interface IAutoTemplateService extends IService<AutoTemplate> {

    /**
     * 添加模板
     * @param templateJson 前台传来的整串json
     * @return
     */
    boolean addTemplate(TemplateJson templateJson);

    /**
     * 删除模板
     * @param id
     */
    void delete(String id);

    /**
     * 修改模板
     * @param templateJson   前台传来的整串json
     */
    boolean updateTemplate(TemplateJson templateJson);
}
