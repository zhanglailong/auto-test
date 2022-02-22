package org.jeecg.modules.serve.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author yeyl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="上传脚本参数", description="上传脚本参数")
public class ServerUpScriptPara implements Serializable {
    private static final long serialVersionUID = 1L;
    /**项目名*/
    @NotBlank
    @ApiModelProperty(value = "项目名")
    private String projectName;

    /**
     * 方案名
     */
    @NotBlank
    @ApiModelProperty(value = "方案名")
    private String scheme;


    @ApiModelProperty(value = "脚本文件编号")
    @NotBlank
    private String content;

    @ApiModelProperty(value = "脚本视频文件编号")
    @NotBlank
    private String video;

    @ApiModelProperty(value = "脚本名称")
    @NotBlank
    private String scriptName;

    @ApiModelProperty(value = "视频名称")
    @NotBlank
    private String videoName;

    @ApiModelProperty(value = "测试项名称")

    private String taskName;

    @ApiModelProperty(value = "测试用例")
    private String testName;

    @ApiModelProperty(value = "脚本唯一标识（没有值可以是0）")
    //@NotBlank
    private String scriptCode;
}
