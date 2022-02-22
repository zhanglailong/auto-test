package org.jeecg.modules.script.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
 * @author yeyl
 */
@Data
@ApiModel(value="脚本文件可视化对象", description="脚本文件可视化对象")
public class ScriptViewVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "对象")
    private String object;

    @ApiModelProperty(value = "操作")
    private String option;

    @ApiModelProperty(value = "值")
    private String value;

    @JSONField(serialize=false)
    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    @JSONField(serialize=false)
    @ApiModelProperty(value = "可视化文件名")
    private String viewFileName;

    @JSONField(serialize=false)
    @ApiModelProperty(value = "图片名")
    private String imgName;


    @ApiModelProperty(value = "深度")
    private int depth;


    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "class")
    @JSONField(name = "class")
    private String classs;

    @ApiModelProperty(value = "注释")
    private String notes;

}
